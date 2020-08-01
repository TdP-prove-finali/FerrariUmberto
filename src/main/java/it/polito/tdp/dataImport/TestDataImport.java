package it.polito.tdp.dataImport;

public class TestDataImport {

	public static void main(String[] args) {
		System.out.println("INIZIO");
		DataImport di = new DataImport();
		di.parseJSONStations("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		di.parseCSVRentals("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Viaggi\\prova.csv");
		System.out.println("FINE");
	}

}
