package it.polito.tdp.CompassBike.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		NOLEGGIO, PRELIEVO, RILASCIO, STAZIONE_VUOTA, STAZIONE_PIENA, RIDISTRIBUZIONE, SALVA_DATI
	}
	
	private EventType type;
	private Station startStation;
	private LocalDateTime time;
	
	private Station endStation;
	
	private Bike bike;
	private BikeRent bikeRent;

	
	public Event(EventType type, Station startStation, LocalDateTime time) {
		this.type = type;
		this.startStation = startStation;
		this.time = time;
	}
	
	
	public Event(EventType type, Station startStation, LocalDateTime time, BikeRent bikeRent) {
		this.type = type;
		this.startStation = startStation;
		this.time = time;
		this.bikeRent = bikeRent;
	}
	

	public Event(EventType type, Station startStation, LocalDateTime time, Bike bike, BikeRent bikeRent) {
		super();
		this.type = type;
		this.startStation = startStation;
		this.time = time;
		this.bike = bike;
		this.bikeRent = bikeRent;
	}
	

	public Event(EventType type, Station startStation, LocalDateTime time, Station endStation, Bike bike, BikeRent bikeRent) {
		this.type = type;
		this.startStation = startStation;
		this.time = time;
		this.endStation = endStation;
		this.bike = bike;
		this.bikeRent = bikeRent;
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}


	public Station getStartStation() {
		return startStation;
	}


	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}


	public LocalDateTime getTime() {
		return time;
	}


	public void setTime(LocalDateTime time) {
		this.time = time;
	}


	public Station getEndStation() {
		return endStation;
	}


	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}


	public Bike getBike() {
		return bike;
	}


	public void setBike(Bike bike) {
		this.bike = bike;
	}


	public BikeRent getBikeRent() {
		return bikeRent;
	}


	public void setBikeRent(BikeRent bikeRent) {
		this.bikeRent = bikeRent;
	}


	@Override
	public int compareTo(Event o) {
		if(this.time.equals(o.time)) {
			if(this.type == EventType.PRELIEVO)
				return -1;
			else if(o.type == EventType.PRELIEVO)
				return 1;
			else if(this.type == EventType.SALVA_DATI)
				return 1;
			else if(o.type == EventType.SALVA_DATI)
				return -1;
			else 
				return this.time.compareTo(o.time);
		} else
			return this.time.compareTo(o.time);
	}
	
}
