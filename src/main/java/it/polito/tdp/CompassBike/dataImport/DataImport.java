package it.polito.tdp.CompassBike.dataImport;

import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Station;

public class DataImport {
	
	public void parseJSONStations(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<StationData> stations = ParseJSONStations.parse(directory);
		for(StationData station : stations)
			StationsDAO.addStation(station);
	}
	
	public void parseCSVRentals(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<Rental> rentals = ParseCSVRentals.parse(directory);
		Map<Integer, Station> stationsIdMap = StationsDAO.getAllStations();
		for(Rental rental : rentals) {
			if(stationsIdMap.containsKey(rental.getStartStationId()) && stationsIdMap.containsKey(rental.getEndStationId())) {
				BikesDAO.addBike(rental);
				RentalsDAO.addRental(rental);
			}
		}
	}

}
