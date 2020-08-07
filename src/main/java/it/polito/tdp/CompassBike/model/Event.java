package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		NOLEGGIO, PRELIEVO, RILASCIO, STAZIONE_VUOTA, STAZIONE_PIENA
	}
	
	private EventType type;
	private Station startStation;
	private LocalDateTime time;
	
	private Station endStation;
	private Duration duration;
	
	private Bike bike;

	
	public Event(EventType type, Station startStation, LocalDateTime time) {
		this.type = type;
		this.startStation = startStation;
		this.time = time;
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


	public Duration getDuration() {
		return duration;
	}


	public void setDuration(Duration duration) {
		this.duration = duration;
	}


	public Bike getBike() {
		return bike;
	}


	public void setBike(Bike bike) {
		this.bike = bike;
	}


	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.time);
	}
	
}
