package it.polito.tdp.CompassBike.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.model.Simulator.RedistributionType;

@SuppressWarnings("unused")
public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		
		LocalDate startDate = LocalDate.of(2020, 05, 20);
		LocalDate endDate = LocalDate.of(2020, 05, 22);
		
		Long inizio = System.currentTimeMillis();
		
		model.setParametersSimulation(startDate, endDate, 0.0, startDate, endDate);
		model.setRedistribution(RedistributionType.NESSUNO);
		model.setProbabilityNewStartStation(60.0);
		model.setNumBikes(14000);
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
		
		File map = model.getMapsResult();
    	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
				Desktop.getDesktop().browse(map.toURI());
			} catch (IOException e) {
				System.out.println("Impossibile aprire il Browser per mostrare la mappa!");
			}
        }
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
		
		
		//USER ST [133, 805, 72, 9002]
		//5.727
	}

}
