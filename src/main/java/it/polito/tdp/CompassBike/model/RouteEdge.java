package it.polito.tdp.CompassBike.model;

import java.time.Duration;

import org.jgrapht.graph.DefaultWeightedEdge;

/*
 * Arco personalizzato
 */
public class RouteEdge extends DefaultWeightedEdge implements Comparable<RouteEdge> {
	
	private static final long serialVersionUID = 1L;
	private Duration minDuration;
	private Duration maxDuration;
	
	public RouteEdge(Duration minDuration, Duration maxDuration) {
		this.minDuration = minDuration;
		this.maxDuration = maxDuration;
	}

	public Duration getMinDuration() {
		return minDuration;
	}

	public Duration getMaxDuration() {
		return maxDuration;
	}
	
	public String toString() {
		return getSource() + " " + getWeight();
	}

	public void setMinDuration(Duration minDuration) {
		this.minDuration = minDuration;
	}

	public void setMaxDuration(Duration maxDuration) {
		this.maxDuration = maxDuration;
	}

	@Override
	public int compareTo(RouteEdge o) {
		return this.minDuration.compareTo(o.minDuration);
	}

	
}
