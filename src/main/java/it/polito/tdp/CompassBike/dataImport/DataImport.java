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
	 * Permette di leggere il file (in formato JSON) e aggiungere le stazioni lette correttamente al db.
	 * @param Il file da leggere
	 * @return Un vettore di interi, nella prima posizione si trova il codice di errore, nella seconda il numero di righe lette correttamente e nella terza il numero di quelle che contengono errori di formato.
	 */
	public static Integer[] parseJSONStations(File file) {
		Integer[] result = new Integer[3];
		result[0] = 0;
		
		if(!file.isFile()) {
			result[0] = 1;
			return result;
		} 
		
		if(!Files.isReadable(file.toPath())) {
			result[0] = 2;
			return result;
		}
		
		
		ParseJSONStations parse = new ParseJSONStations();
		List<StationData> stations = parse.parse(file);
		StationsDAO.addStation(stations);
		
		result[1] = parse.getNumCorrectLine();
		result[2] = parse.getNumErrorLine();
		
		return result;
	}
	
	
	/**
	 * Permette di leggere il file (in formato CSV) e aggiungere i noleggi letti correttamente al db.
	 * @param Il file da leggere
	 * @return Un vettore di interi, nella prima posizione si trova il codice di errore, nella seconda il numero di righe lette correttamente e nella terza il numero di quelle che contengono errori di formato.
	 */
	public static Integer[] parseCSVRentals(File file) {
		Integer[] result = new Integer[3];
		result[0] = 0;
		
		if(!file.isFile()) {
			result[0] = 1;
			return result;
		} 
		
		if(!Files.isReadable(file.toPath())) {
			result[0] = 2;
			return result;
		}
		
		
		ParseCSVRentals parse = new ParseCSVRentals();
		List<RentalData> allRentals = parse.parse(file);
		Map<Integer, Station> stationsIdMap = StationsDAO.getAllStations();
					
		List<BikeData> bikes = new ArrayList<>();
		List<RentalData> rentals = new ArrayList<>();
		for(RentalData rental : allRentals) {
			if(stationsIdMap.containsKey(rental.getStartStationId()) && stationsIdMap.containsKey(rental.getEndStationId())) {
				rentals.add(rental);
				BikeData bike = new BikeData(rental.getBikeId(), rental.getEndStationId());
				if(!bikes.contains(bike))
					bikes.add(bike);
			}
		}
					
		BikesDAO.addBike(bikes);
		RentalsDAO.addRental(rentals);
		
		result[1] = parse.getNumCorrectLine();
		result[2] = parse.getNumErrorLine();
		
		return result;
	}

}
