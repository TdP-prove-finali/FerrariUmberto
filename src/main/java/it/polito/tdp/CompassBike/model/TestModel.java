package it.polito.tdp.CompassBike.model;

import java.time.LocalDate;
import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		LocalDate startDate = LocalDate.of(2020, 5, 20);
		LocalDate endDate = LocalDate.of(2020, 05, 26);
		
		Long inizio = System.currentTimeMillis();
		
		Simulator sim = new Simulator();
		sim.init(startDate, endDate, -10.0);
		Long m = System.currentTimeMillis();
		Long d = m - inizio;
		System.out.println(d / 1000.0+"\n");
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
