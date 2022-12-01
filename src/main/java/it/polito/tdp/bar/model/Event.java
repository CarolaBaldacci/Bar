package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event implements Comparable<Event>{

	//2 tipi di eventi
	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI,
		TAVOLO_LIBERATO	
	}
	
	//attributi degli eventi
	private EventType type; 
	private Duration time; //o int time
	private int nPersone;
	private Duration durata;  //o int durata
	private double tolleranza;
	private Tavolo tavolo;
	
	public Event(Duration time, EventType type, int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
		super();
		this.type = type;
		this.time = time;
		this.nPersone = nPersone;
		this.durata = durata;
		this.tolleranza = tolleranza;
		this.tavolo = tavolo;
	}

	public EventType getType() {
		return type;
	}

	public Duration getTime() {
		return time;
	}

	public int getnPersone() {
		return nPersone;
	}

	public Duration getDurata() {
		return durata;
	}

	public double getTolleranza() {
		return tolleranza;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.getDurata());
	}

	
	
}
