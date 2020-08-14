package it.polito.tdp.CompassBike.dataImport;

import java.io.File;

import it.polito.tdp.CompassBike.model.Model;

public class TestDataImport {

	public static void main(String[] args) {
		Model model = new Model();
		
		Long inizio = System.currentTimeMillis();
		
		System.out.println("INIZIO");
		
		File stationsFile = new File("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		File rentalsFile = new File("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Viaggi\\last.csv");
		model.loadFileStations(stationsFile);
		model.loadFileRentals(rentalsFile);
		
		System.out.println("FINE");
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
