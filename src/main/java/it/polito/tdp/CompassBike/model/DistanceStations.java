package it.polito.tdp.CompassBike.model;

public class DistanceStations implements Comparable<DistanceStations> {
	
	private Station startStation;
	private Station endStation;
	private Double distance;
	
	
	public DistanceStations(Station startStation, Station endStation, Double distance) {
		this.startStation = startStation;
		this.endStation = endStation;
		this.distance = distance;
	}


	public Station getStartStation() {
		return startStation;
	}


	public Station getEndStation() {
		return endStation;
	}


	public Double getDistance() {
		return distance;
	}


	@Override
	public int compareTo(DistanceStations o) {
		return this.distance.compareTo(o.distance);
	}
	
	
}
