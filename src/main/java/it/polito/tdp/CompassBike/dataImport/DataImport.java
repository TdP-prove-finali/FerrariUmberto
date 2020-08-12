package it.polito.tdp.CompassBike.dataImport;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Station;

public class DataImport {
	
	/**
	 * Permette di leggere il file (in formato JSON) che si trova alla directory passata come parametro e aggiungere le stazioni lette correttamente al db.
	 * @param directory La directory del file
	 */
	public static void parseJSONStations(String directory) {
		File file = new File(directory);
		
		if(!file.isFile()) {
			System.out.println("File stazioni non trovato!");
			return;
		} 
		
		if(!Files.isReadable(file.toPath())) {
			System.out.println("Impossibile leggere il file stazioni!");
			return;
		}
		
		
		List<StationData> stations = ParseJSONStations.parse(directory);
		StationsDAO.addStation(stations);
	}
	
	
	/**
	 * Permette di leggere il file (in formato CSV) che si trova alla directory passata come parametro e aggiungere i noleggi lette correttamente al db.
	 * @param directory La directory del file
	 */
	public static void parseCSVRentals(String directory) {
		File file = new File(directory);
		
		if(!file.isFile()) {
			System.out.println("File noleggi non trovato!");
			return;
		} 
		
		if(!Files.isReadable(file.toPath())) {
			System.out.println("Impossibile leggere il file noleggi!");
			return;
		}
		
		
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
