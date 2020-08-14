package it.polito.tdp.CompassBike.model;

import java.time.LocalDate;

public class GroupRentals {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer numRentals;
	
	
	public GroupRentals(LocalDate fromDate, LocalDate toDate, Integer numRentals) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.numRentals = numRentals;
	}


	public LocalDate getFromDate() {
		return fromDate;
	}


	public LocalDate getToDate() {
		return toDate;
	}


	public Integer getNumRentals() {
		return numRentals;
	}
	
	

}
