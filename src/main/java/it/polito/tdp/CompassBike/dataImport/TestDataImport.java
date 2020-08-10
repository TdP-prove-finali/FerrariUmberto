package it.polito.tdp.CompassBike.dataImport;

import java.time.LocalDate;

import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;

public class TestDataImport {

	public static void main(String[] args) {
		Long inizio = System.currentTimeMillis();
		
		System.out.println("INIZIO");
		DataImport di = new DataImport();
		di.parseJSONStations("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		di.parseCSVRentals("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Viaggi\\last.csv");
		
		RentalsDAO.getAllRouteDay(LocalDate.of(2020, 5, 20), StationsDAO.getAllStations());
		System.out.println("FINE");
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
