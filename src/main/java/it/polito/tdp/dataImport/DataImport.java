package it.polito.tdp.dataImport;

import java.util.List;

import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Station;

public class DataImport {
	
	public void parseJSONStations(String directory) {
		List<Station> stations = ParseJSONStations.parse(directory);
		for(Station station : stations)
			StationsDAO.addStation(station);
	}

}
