package it.polito.tdp.CompassBike.model;

public class Bike {
	
	private Integer id;
	private Integer stationId;
	private boolean isBroken;
	
	public Bike(Integer id, Integer stationId, boolean isBroken) {
		super();
		this.id = id;
		this.stationId = stationId;
		this.isBroken = isBroken;
	}

	public Bike(Integer id) {
		super();
		this.id = id;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public Integer getId() {
		return id;
	}
	
	
	public String toString() {
		return this.id+" "+this.stationId;
	}

}
