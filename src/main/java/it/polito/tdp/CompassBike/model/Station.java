package it.polito.tdp.CompassBike.model;

import java.util.ArrayList;
import java.util.List;

public class Station implements Comparable<Station> {
	
	public enum ProblemType {
		NESSUNO, TRAFFICO, VUOTA, PIENA
	}
	
	private Integer id;
	private String commonName;
	
	private Integer numBikes;
	private Integer numEmptyDocks;
	private Integer numDocks;
	
	private List<Bike> bikes;
	
	private Double latitude;
	private Double longitude;
	
	private List<BikeRent> completedRent;
	private List<BikeRent> canceledRent;
	private List<BikeRent> emptyStationRent;
	private List<BikeRent> fullStationRent;
	
	private Integer numCompletedRent;
	private Integer numCanceledRent;
	private Integer numEmptyStationRent;
	private Integer numFullStationRent;
	
	private ProblemType problemType;
	private String problemString;
	
	
	public Station(Integer id, String commonName, Integer numBikes, Integer numEmptyDocks, Integer numDocks,
			Double latitude, Double longitude) {
		this.id = id;
		this.commonName = commonName;
		this.numBikes = numBikes;
		this.numEmptyDocks = numEmptyDocks;
		this.numDocks = numDocks;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bikes = new ArrayList<>();
		this.completedRent = new ArrayList<>();
		this.canceledRent = new ArrayList<>();
		this.emptyStationRent = new ArrayList<>();
		this.fullStationRent = new ArrayList<>();
		this.numCompletedRent = 0;
		this.numCanceledRent = 0;
		this.numEmptyStationRent = 0;
		this.numFullStationRent = 0;
	}
	
	
	public Integer getId() {
		return id;
	}


	public String getCommonName() {
		return commonName;
	}


	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}


	public Integer getNumBikes() {
		return numBikes;
	}


	public void setNumBikes(Integer numBikes) {
		this.numBikes = numBikes;
	}


	public Integer getNumEmptyDocks() {
		return numEmptyDocks;
	}


	public void setNumEmptyDocks(Integer numEmptyDocks) {
		this.numEmptyDocks = numEmptyDocks;
	}


	public Integer getNumDocks() {
		return numDocks;
	}


	public void setNumDocks(Integer numDocks) {
		this.numDocks = numDocks;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public List<BikeRent> getCompletedRent() {
		return completedRent;
	}


	public List<BikeRent> getCanceledRent() {
		return canceledRent;
	}


	public void increaseNumBike(Integer increase) {
		this.numBikes += increase;
	}
	
	
	public void decreaseNumBike(Integer decrease) {
		this.numBikes -= decrease;
	}
	
	
	public void increaseNumEmpityDocks(Integer increase) {
		this.numEmptyDocks += increase;
	}
	
	
	public void decreaseNumEmpityDocks(Integer decrease) {
		this.numEmptyDocks -= decrease;
	}
	
	
	public void addCompletedRent(BikeRent rent) {
		this.completedRent.add(rent);
		this.numCompletedRent++;
	}
	
	
	public void addCanceledRent(BikeRent rent) {
		this.canceledRent.add(rent);
		this.numCanceledRent++;
	}


	public List<BikeRent> getEmptyStationRent() {
		return emptyStationRent;
	}
	
	
	public void addEmptyStationRent(BikeRent rent) {
		this.emptyStationRent.add(rent);
		this.numEmptyStationRent++;
	}


	public List<BikeRent> getFullStationRent() {
		return fullStationRent;
	}
	
	
	public void addFullStationRent(BikeRent rent) {
		this.fullStationRent.add(rent);
		this.numFullStationRent++;
	}


	public List<Bike> getBikes() {
		return bikes;
	}


	public void setBikes(List<Bike> bikes) {
		this.bikes = bikes;
	}
	
	
	public void addBike(Bike bike) {
		this.bikes.add(bike);
	}
	
	
	public void removeBike(Bike bike) {
		this.bikes.remove(bike);
	}
	
	
	public ProblemType getProblemType() {
		return problemType;
	}


	public void setProblemType(ProblemType problemType) {
		this.problemType = problemType;
		switch(problemType) {
			case NESSUNO:
				this.problemString = "Nessun problema riscontrato";
				break;
			case PIENA:
				this.problemString = "Stazione spesso piena";
				break;
			case TRAFFICO:
				this.problemString = "Stazione ad alto traffico";
				break;
			case VUOTA:
				this.problemString = "Stazione spesso vuota";
				break;
		}
	}


	public Integer getNumCompletedRent() {
		return numCompletedRent;
	}


	public Integer getNumCanceledRent() {
		return numCanceledRent;
	}


	public Integer getNumEmptyStationRent() {
		return numEmptyStationRent;
	}


	public Integer getNumFullStationRent() {
		return numFullStationRent;
	}
	
	
	public String getProblemString() {
		return problemString;
	}
	
	
	public List<BikeRent> getStartCompletedRent() {
		List<BikeRent> result = new ArrayList<>();
		for(BikeRent bk : this.completedRent)
			if(bk.getStartStation().equals(this))
				result.add(bk);
		
		return result;
	}
	
	
	public List<BikeRent> getEndCompletedRent() {
		List<BikeRent> result = new ArrayList<>();
		for(BikeRent bk : this.completedRent)
			if(bk.getEndStation().equals(this))
				result.add(bk);
		
		return result;
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
		Station other = (Station) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	public String toString() {
		return this.id+" - "+this.commonName;
	}


	@Override
	public int compareTo(Station o) {
		return this.id.compareTo(o.id);
	}

	
}
