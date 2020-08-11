package it.polito.tdp.CompassBike.dataImport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Station;

public class DataImport {
	
	public static void parseJSONStations(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<StationData> stations = ParseJSONStations.parse(directory);
		StationsDAO.addStation(stations);
	}
	
	public static void parseCSVRentals(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<RentalData> allRentals = ParseCSVRentals.parse(directory);
		Map<Integer, Station> stationsIdMap = StationsDAO.getAllStations();
		
		List<BikeData> bikes = new ArrayList<>();
		List<RentalData> rentals = new ArrayList<>();
		for(RentalData rental : allRentals) {
			if(stationsIdMap.containsKey(rental.getStartStationId()) && stationsIdMap.containsKey(rental.getEndStationId())) {
				rentals.add(rental);
				bikes.add(new BikeData(rental.getBikeId(), rental.getEndStationId()));
			}
		}
		
		BikesDAO.addBike(bikes);
		RentalsDAO.addRental(rentals);
	}

}
