package it.polito.tdp.CompassBike.dataImport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ParseJSONStations {
	
	/**
	 * Metodo di utilità per leggere un file JSON che contiene le informazioni sulle stazioni
	 * @return ritorna la lista delle stazioni
	 */
	public static List<StationData> parse(String directory) {
		Path path = Paths.get(directory);
		List<StationData> stations = new ArrayList<StationData>();
 
		Integer errorLine = 0;
		
		JSONArray jsonArray = null;
	    try {
	    	String content = Files.readString(path);
			JSONObject jsonObject = new JSONObject(content);
			jsonArray = jsonObject.getJSONArray("stations");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
			
		for(int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonStation = jsonArray.getJSONObject(i);
				
				String stringId = jsonStation.getString("id").replace("BikePoints_", "");
				Integer id = Integer.parseInt(stringId);
				String commonName = jsonStation.getString("commonName");
				Double latitude = jsonStation.getDouble("lat");
				Double longitude = jsonStation.getDouble("lon");
				
				StationData station = new StationData(id, commonName, latitude, longitude);
				
				JSONArray properties = jsonStation.getJSONArray("additionalProperties");
				for(int j = 0; j < properties.length(); j++) {
					JSONObject property = properties.getJSONObject(j);
					String key = property.getString("key");
					
					switch(key) {
						case "TerminalName":
							station.setTerminalName(Integer.parseInt(property.getString("value")));
							break;
						case "Installed": 
							station.setInstalled(Boolean.parseBoolean(property.getString("value")));
							break;
						case "Locked": 
							station.setLocked(Boolean.parseBoolean(property.getString("value")));
							break;
						case "InstallDate":
							if(!property.getString("value").isEmpty())
								station.setInstallDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "RemovalDate":
							if(!property.getString("value").isEmpty())
								station.setRemovalDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "Temporary":
							station.setTemporary(Boolean.parseBoolean(property.getString("value")));
							break;
						case "NbBikes":
							station.setNumBikes(Integer.parseInt(property.getString("value")));
							break;
						case "NbEmptyDocks":
							station.setNumEmptyDocks(Integer.parseInt(property.getString("value")));
							break;
						case "NbDocks":
							station.setNumDocks(Integer.parseInt(property.getString("value")));
							break;
					}
				}
				if(station.getNumDocks() - (station.getNumBikes() + station.getNumEmptyDocks()) != 0)
					station.setBroken(true);
				
				stations.add(station);
			} catch(JSONException e) {errorLine++;}
			  catch(NumberFormatException e) {errorLine++;}
			  catch(DateTimeException e) {errorLine++;}
			
		}
		
		System.out.println("Sono state lette correttamente "+stations.size()+" righe!");
    	System.out.println(errorLine+" righe contengono uno o più errori di formato!");
    	Double percentage = ((double) stations.size()) / (stations.size() + errorLine) * 100.0;
    	System.out.println(String.format("Verrà quindi salvato circa il %.2f%% del file\n", percentage));
	    
	    return stations;
	}

}
