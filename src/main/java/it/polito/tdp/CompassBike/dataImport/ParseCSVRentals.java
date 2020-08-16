package it.polito.tdp.CompassBike.dataImport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ParseCSVRentals {
	
	private Integer correctLine = 0;
	private Integer errorLine = 0;

	public List<RentalData> parse(File file) {
        String line = "";
        List<RentalData> rentals = new ArrayList<>();

        Integer errorLine = 0;
        
        
        try {
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	br.readLine(); //Leggo la prima riga con le intestazioni
           
        	while ((line = br.readLine()) != null) {
        		
        		if(line.charAt(0) == '\"')
        			line = line.substring(1, line.length());
        		if(line.charAt(line.length() - 1) == '\"')
        			line = line.substring(0, line.length() - 1);
        		
        		line = line.replace("\"\"", "\"");
            	
            	List<String> values = new ArrayList<String>();

            	int start = 0;
            	boolean inQuotes = false;
            	for(int i = 0; i < line.length(); i++) {
            		if(line.charAt(i) == '\"')
            			inQuotes = !inQuotes;
            		if(i == line.length() - 1)
            			values.add(line.substring(start).replace("\"", ""));
            		else if(line.charAt(i) == ',' && !inQuotes) {
            			values.add(line.substring(start, i).replace("\"", ""));
            			start = i + 1;
            		}
            	}
            	
            	
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
                
                try {
	                Integer id = Integer.parseInt(values.get(0));
	                Duration duration = Duration.of(Integer.parseInt(values.get(1)), ChronoUnit.SECONDS);
	                Integer bikeId = Integer.parseInt(values.get(2));
	                LocalDateTime endDate = LocalDateTime.parse(values.get(3),formatter);
	                Integer endStationId = Integer.parseInt(values.get(4));
	                LocalDateTime startDate = LocalDateTime.parse(values.get(6), formatter);
	                Integer startStationId = Integer.parseInt(values.get(7));
	                
	                rentals.add(new RentalData(id, duration, bikeId, endDate, endStationId, startDate, startStationId));
                } catch(NumberFormatException e) {errorLine++;}
                  catch(DateTimeParseException e) {errorLine++;}
            }
        	
        	this.correctLine = rentals.size();
    		this.errorLine = errorLine;
            
            br.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return rentals;
	}
	
	
	public Integer getNumCorrectLine() {
		return this.correctLine;
	}
	
	
	public Integer getNumErrorLine() {
		return this.errorLine;
	}

}