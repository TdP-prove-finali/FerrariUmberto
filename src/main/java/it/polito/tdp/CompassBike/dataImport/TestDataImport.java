package it.polito.tdp.CompassBike.dataImport;

import it.polito.tdp.CompassBike.model.Model;

public class TestDataImport {

	public static void main(String[] args) {
		Model model = new Model();
		
		Long inizio = System.currentTimeMillis();
		
		System.out.println("INIZIO");
		
		model.loadFileStations("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		model.loadFileRentals("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Viaggi\\last.csv");
		
		System.out.println("FINE");
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
