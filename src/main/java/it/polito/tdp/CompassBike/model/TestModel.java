package it.polito.tdp.CompassBike.model;

import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		Long inizio = System.currentTimeMillis();
		
		Simulator sim = new Simulator();
		sim.init();
		sim.run();
		
		Map<Integer, Station> res = sim.getStations();
		for(Integer id : res.keySet()) {
			Station st = res.get(id);
			System.out.println(st.getCommonName()+" "+st.getCompletedRent().size()+" "+st.getCanceledRent().size()+" "+st.getEmptyStationRent().size()+" "+st.getFullStationRent().size());
		}
		
		System.out.println("\n\n");
		System.out.println("COMPLETATI "+sim.getNumCompletedRent());
		System.out.println("CANCELLATI "+sim.getNumCanceledRent());
		System.out.println("NUM RENT "+sim.getNumRent());
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
