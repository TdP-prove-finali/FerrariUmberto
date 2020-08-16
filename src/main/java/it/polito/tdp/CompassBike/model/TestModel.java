package it.polito.tdp.CompassBike.model;

import java.time.LocalDate;
import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		
		LocalDate startDate = LocalDate.of(2020, 05, 20);
		LocalDate endDate = LocalDate.of(2020, 05, 26);
		
		Long inizio = System.currentTimeMillis();
		
		model.setParametersSimulation(startDate, endDate, 10.0);
		model.runSimulation();
		
		Map<Integer, Station> res = model.getStationsResult();
		for(Integer id : res.keySet()) {
			Station st = res.get(id);
			System.out.println(st.getCommonName()+" "+st.getCompletedRent().size()+" "+st.getCanceledRent().size()+" "+st.getEmptyStationRent().size()+" "+st.getFullStationRent().size());
		}
		
		System.out.println("\n\n");
		System.out.println("COMPLETATI "+model.getCompletedRent().size());
		System.out.println("CANCELLATI "+model.getCanceledRent().size());
		System.out.println("NUM RENT "+model.getNumRent());
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
