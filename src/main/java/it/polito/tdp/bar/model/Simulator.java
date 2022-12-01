package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {

	//MODELLO
	private List<Tavolo> tavoli;
	//PARAMENTRI
	private int NUM_EVENTI=2000;
	private int T_ARRIVO_MAX=10;
	private int NUM_PERSONE_MAX=10;
	private int DURATA_MIN=60;
	private int DURATA_MAX=120;
	private double TOLLERANZA_MAX=0.9;
	private double OCCUPAZIONE_MAX=0.5;
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	//RISULTATI=statistiche
	private Statistiche statistiche;
	
//INIZIALIZZAZIONE
	
	public void init() {
		this.queue=new PriorityQueue<Event>();
		this.statistiche= new Statistiche();
		this.tavoli=new LinkedList<Tavolo>();
		creaTavoli();
		creaEventi();
	}

	private void creaEventi() {
		Duration arrivo=Duration.ofMinutes(0);
		for(int i=0; i<this.NUM_EVENTI;i++) {
			int nPersone =(int) Math.random()*this.NUM_PERSONE_MAX+1;
			Duration durata= Duration.ofMinutes(this.DURATA_MIN +
					(int)(Math.random()*(this.DURATA_MAX-this.DURATA_MIN + 1)));
			double tolleranza= (int) Math.random()*this.TOLLERANZA_MAX;
			Event e=new Event(arrivo,EventType.ARRIVO_GRUPPO_CLIENTI,
					nPersone,durata,tolleranza,null);
			this.queue.add(e);
			arrivo=arrivo.plusMinutes((int) (Math.random()*this.T_ARRIVO_MAX+1));
		}
	}
 
	private void creaTavolo(int quantita ,int dimensione) {
    	for(int i=0; i<quantita;i++)
    		this.tavoli.add(new Tavolo(dimensione,false));
    }
    
	private void creaTavoli() {
		creaTavolo(2,10);
		creaTavolo(4,8);
		creaTavolo(4,6);
		creaTavolo(5,4);
		
		Collections.sort(this.tavoli,new Comparator<Tavolo>(){
			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getPosti()-o2.getPosti();
			}
			//oppure definisco nPosti come Integer e uso compareTo() di Integer
		});
	}
    
	
//RUN
 
    public void run() {
    	while(!queue.isEmpty()) {
    		Event e=queue.poll();
    		processaEvento(e);
    	}
    }

   private void processaEvento(Event e) {
	   switch(e.getType()) {
	    case ARRIVO_GRUPPO_CLIENTI:
	    	//conto i clienti totali
	    	this.statistiche.incrementaClienti(e.getnPersone());
	    	//cerco un tavolo
	    	Tavolo tavolo=null;
	    	for(Tavolo t: this.tavoli) {
	    		if(!t.isOccupato() && t.getPosti()>e.getnPersone() &&
	    				t.getPosti()*this.OCCUPAZIONE_MAX<=e.getnPersone()) {
	    			tavolo=t;
	    			break;
	    		}
	    	}
	    	if(tavolo!=null) {
	    		System.out.format("tavolo trovato : da %d per %d persone",tavolo.getPosti(), e.getnPersone());
	    		statistiche.incrementaSoddisfatti(e.getnPersone());
	    		tavolo.setOccupato(true);
	    		//quando i clienti si alzano
	    		queue.add(new Event(e.getTime().plus(e.getDurata()),EventType.TAVOLO_LIBERATO,
	    				e.getnPersone(),e.getDurata(),e.getTolleranza(),tavolo));
	    	}else {//bancone
	    		double bancone= Math.random();
	    		if(bancone<=e.getTolleranza()) {
	    			statistiche.incrementaSoddisfatti(e.getnPersone());
		    		System.out.format("%d persone sono state soddisfatte",e.getnPersone());
	    		}else {
	    			statistiche.incrementaInsoddisfatti(e.getnPersone());
	    			System.out.format("%d persone sono state insoddisfatte",e.getnPersone());
	    		}
	    	}
	    	
		    break;
	    case TAVOLO_LIBERATO:
	    	e.getTavolo().setOccupato(false);
	    	break;
	   }
   }


//RETURN
   public Statistiche getStatistiche() {
    	return this.statistiche;
   }
    
}
