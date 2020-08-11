package it.polito.tdp.CompassBike.dataImport;

public class TestDataImport {

	public static void main(String[] args) {
		Long inizio = System.currentTimeMillis();
		
		System.out.println("INIZIO");
		
		DataImport di = new DataImport();
		di.parseJSONStations("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		di.parseCSVRentals("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Viaggi\\last.csv");
		
		System.out.println("FINE");
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println(durata / 1000.0);
	}

}
