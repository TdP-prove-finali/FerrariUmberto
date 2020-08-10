package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class BikeRent {
	
	public enum BikeRentStatus {
		IN_CORSO, COMPLETATO, CANCELLATO, CAMBIO_STAZIONE, STAZIONE_VUOTA, STAZIONE_PIENA
	}
	
	private Integer id;
	private BikeRentStatus status;
	private Station startStation;
	private Station endStation;
	private Duration duration;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	
	
	public BikeRent(Integer id, BikeRentStatus status, Station startStation, Station endStation, Duration duration,
			LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.id = id;
		this.status = status;
		this.startStation = startStation;
		this.endStation = endStation;
		this.duration = duration;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}


	public BikeRent(Integer id, BikeRentStatus status, Station startStation, LocalDateTime startDateTime) {
		this.id = id;
		this.status = status;
		this.startStation = startStation;
		this.startDateTime = startDateTime;
	}

	
	public Integer getId() {
		return id;
	}
	

	public Station getStartStation() {
		return startStation;
	}


	public void setStartStation(Station startStation) {
		this.startStation = startStation;
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


	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}


	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}


	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}


	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}


	public BikeRentStatus getStatus() {
		return status;
	}


	public void setStatus(BikeRentStatus status) {
		this.status = status;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BikeRent other = (BikeRent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public BikeRent clone() {
		BikeRent clone = null;
		if(this.endStation != null && this.endDateTime != null)
			clone = new BikeRent(this.id, this.status, this.startStation, this.endStation, this.duration, this.startDateTime, this.endDateTime);
		else
			clone = new BikeRent(this.id, this.status, this.startStation, this.startDateTime);
		
		return clone;
	}
	
	
	
	
}
