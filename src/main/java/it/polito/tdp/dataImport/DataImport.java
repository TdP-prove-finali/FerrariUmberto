package it.polito.tdp.dataImport;

import java.util.List;

import it.polito.tdp.CompassBike.DAO.BikeDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Rental;
import it.polito.tdp.CompassBike.model.Station;

public class DataImport {
	
	public void parseJSONStations(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<Station> stations = ParseJSONStations.parse(directory);
		for(Station station : stations)
			StationsDAO.addStation(station);
	}
	
	public void parseCSVRentals(String directory) {
		// TODO Controllo errore impossibile leggere file
		List<Rental> rentals = ParseCSVRentals.parse(directory);
		for(Rental rental : rentals) {
			BikeDAO.addBike(rental);
			RentalsDAO.addRental(rental);
		}
	}

}
