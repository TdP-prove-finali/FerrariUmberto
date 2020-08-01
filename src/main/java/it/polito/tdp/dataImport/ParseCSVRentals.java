package it.polito.tdp.dataImport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.CompassBike.model.Rental;

public class ParseCSVRentals {

	public static List<Rental> parse(String directory) {
        String line = "";
        List<Rental> rentals = new ArrayList<>();

        try {
        	BufferedReader br = new BufferedReader(new FileReader(directory));
        	br.readLine(); //Leggo la prima riga con le intestazioni
        	
            while ((line = br.readLine()) != null) {
            	
            	List<String> values = new ArrayList<String>();
            	int start = 0;
            	boolean inQuotes = false;
            	for(int i = 0; i < line.length(); i++) {
            		if(line.charAt(i) == '\"' && i != line.length() -1 && line.charAt(i+1) == '\"')
            			inQuotes = !inQuotes;
            		if(i == line.length() - 1)
            			values.add(line.substring(start));
            		else if(line.charAt(i) == ',' && !inQuotes) {
            			values.add(line.substring(start, i).replace("\"", ""));
            			start = i + 1;
            		}
            	}
            	
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
                
                // TODO Fare controllo errore per la lettura di questi parametri
                Integer id = Integer.parseInt(values.get(0));
                Duration duration = Duration.of(Integer.parseInt(values.get(1)), ChronoUnit.MINUTES);
                Integer bikeId = Integer.parseInt(values.get(2));
                LocalDateTime endDate = LocalDateTime.parse(values.get(3),formatter);
                Integer endStationId = Integer.parseInt(values.get(4));
                LocalDateTime startDate = LocalDateTime.parse(values.get(6), formatter);
                Integer startStationId = Integer.parseInt(values.get(7));
                
                rentals.add(new Rental(id, duration, bikeId, endDate, endStationId, startDate, startStationId));
            }
            
            br.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return rentals;
	}

}