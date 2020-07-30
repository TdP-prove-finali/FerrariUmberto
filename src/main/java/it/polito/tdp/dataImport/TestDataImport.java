package it.polito.tdp.dataImport;

public class TestDataImport {

	public static void main(String[] args) {
		System.out.println("INIZIO");
		DataImport di = new DataImport();
		di.parseJSONStations("123");
		System.out.println("FINE");
	}

}
