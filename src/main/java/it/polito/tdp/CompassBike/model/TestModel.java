package it.polito.tdp.CompassBike.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Long inizio = System.currentTimeMillis();
		
		RentalsGenerator rg = new RentalsGenerator();
		rg.setVariation(-10.0);
		List<Event> events = rg.generateEvents();
		System.out.println(events.size());
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
