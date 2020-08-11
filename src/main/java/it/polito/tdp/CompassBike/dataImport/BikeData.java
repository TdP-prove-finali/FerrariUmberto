package it.polito.tdp.CompassBike.dataImport;

public class BikeData {
	
	private Integer bikeId;
	private Integer stationId;
	
	
	public BikeData(Integer bikeId, Integer stationId) {
		this.bikeId = bikeId;
		this.stationId = stationId;
	}


	public Integer getBikeId() {
		return bikeId;
	}


	public Integer getStationId() {
		return stationId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
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
		BikeData other = (BikeData) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		return true;
	}
	
	
	
}
