package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Rental {
	
	private Integer id;
	private Duration duration;
	private Integer bikeId;
	private LocalDateTime endDate;
	private Integer endStationId;
	private LocalDateTime startDate;
	private Integer startStationId;
	
	public Rental(Integer id, Duration duration, Integer bikeId, LocalDateTime endDate, Integer endStationId, LocalDateTime startDate, Integer startStationId) {
		this.id = id;
		this.duration = duration;
		this.bikeId = bikeId;
		this.endDate = endDate;
		this.endStationId = endStationId;
		this.startDate = startDate;
		this.startStationId = startStationId;
	}
	

	public Rental(Integer id, Integer bikeId, LocalDateTime startDate, Integer startStationId,
			String startStationCommonName) {
		super();
		this.id = id;
		this.bikeId = bikeId;
		this.startDate = startDate;
		this.startStationId = startStationId;
	}



	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getEndStationId() {
		return endStationId;
	}

	public void setEndStationId(Integer endStationId) {
		this.endStationId = endStationId;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public Integer getStartStationId() {
		return startStationId;
	}

	public void setStartStationId(Integer startStationId) {
		this.startStationId = startStationId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getBikeId() {
		return bikeId;
	}
	
	
	public String toString() {
		return this.id+" "+this.bikeId+" "+this.startStationId+" "+this.endStationId;
	}
	

}
