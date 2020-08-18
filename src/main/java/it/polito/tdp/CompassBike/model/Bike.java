package it.polito.tdp.CompassBike.model;

public class Bike implements Comparable<Bike> {
	
	public enum BikeStatus {
		STAZIONE, NOLEGGIATA, DA_DISTRIBUIRE
	}
	
	private Integer id;
	// Se stationId = null allora la bici è in noleggio oppure in distribuzione
	private Station station;
	private boolean isBroken;
	private BikeStatus status;
	
	public Bike(Integer id, Station station, boolean isBroken, BikeStatus status) {
		this.id = id;
		this.station = station;
		this.isBroken = isBroken;
		this.status = status;
	}
	
	public Bike(Integer id, Station station) {
		this.id = id;
		this.station = station;
	}

	public Bike(Integer id) {
		super();
		this.id = id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
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
	
	public BikeStatus getStatus() {
		return status;
	}

	public void setStatus(BikeStatus status) {
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
		Bike other = (Bike) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	public String toString() {
		return this.id+" "+this.station.getId();
	}

	@Override
	public int compareTo(Bike o) {
		return this.id.compareTo(o.id);
	}

}
