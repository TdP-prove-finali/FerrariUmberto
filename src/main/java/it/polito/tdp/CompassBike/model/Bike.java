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
		Bike other = (Bike) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	public String toString() {
		return this.id+" "+this.stationId;
	}

}
