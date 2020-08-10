package it.polito.tdp.CompassBike.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Long inizio = System.currentTimeMillis();
		
		EventsGenerator rg = new EventsGenerator();
		rg.setVariation(-10.0);
		rg.loadParameters();
		List<Event> events = rg.generateEvents();
		System.out.println("Events size "+events.size());
		
		Simulator sim = new Simulator();
		sim.init();
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
