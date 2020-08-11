package it.polito.tdp.CompassBike.dataImport;

import java.time.Duration;
import java.time.LocalDateTime;

public class RentalData {
	
	private Integer id;
	private Duration duration;
	private Integer bikeId;
	private LocalDateTime endDate;
	private Integer endStationId;
	private LocalDateTime startDate;
	private Integer startStationId;
	
	public RentalData(Integer id, Duration duration, Integer bikeId, LocalDateTime endDate, Integer endStationId, LocalDateTime startDate, Integer startStationId) {
		this.id = id;
		this.duration = duration;
		this.bikeId = bikeId;
		this.endDate = endDate;
		this.endStationId = endStationId;
		this.startDate = startDate;
		this.startStationId = startStationId;
	}
	

	public RentalData(Integer id, Integer bikeId, LocalDateTime startDate, Integer startStationId,
			String startStationCommonName) {
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
		RentalData other = (RentalData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String toString() {
		return this.id+" "+this.bikeId+" "+this.startStationId+" "+this.endStationId;
	}
	

}
