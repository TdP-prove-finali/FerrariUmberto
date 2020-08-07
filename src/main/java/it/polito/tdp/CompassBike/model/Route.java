package it.polito.tdp.CompassBike.model;

import java.time.Duration;

public class Route {
	
	private Station startStation;
	private Station endStation;
	private Duration minDuration;
	private Duration maxDuration;
	
	public Route(Station startStation, Station endStation, Duration minDuration, Duration maxDuration) {
		this.startStation = startStation;
		this.endStation = endStation;
		this.minDuration = minDuration;
		this.maxDuration = maxDuration;
	}

	public Station getStartStation() {
		return startStation;
	}

	public Station getEndStation() {
		return endStation;
	}

	public Duration getMinDuration() {
		return minDuration;
	}

	public Duration getMaxDuration() {
		return maxDuration;
	}
	
}
