package it.polito.tdp.CompassBike.dataImport;

import java.io.File;
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
	
	private Integer correctLine = 0;
	private Integer errorLine = 0;
	
	/**
	 * Metodo di utilità per leggere un file JSON che contiene le informazioni sulle stazioni
	 * @return La lista di {@link StationData stazioni}
	 */
	public List<StationData> parse(File file) {
		Path path = Paths.get(file.getAbsolutePath());
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
						case "Installed": 
							station.setInstalled(Boolean.parseBoolean(property.getString("value")));
							break;
						case "InstallDate":
							if(!property.getString("value").isEmpty())
								station.setInstallDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "RemovalDate":
							if(!property.getString("value").isEmpty())
								station.setRemovalDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "NbDocks":
							station.setNumDocks(Integer.parseInt(property.getString("value")));
							break;
					}
				}
				
				stations.add(station);
			} catch(JSONException e) {errorLine++;}
			  catch(NumberFormatException e) {errorLine++;}
			  catch(DateTimeException e) {errorLine++;}
			
		}
		
		this.correctLine = stations.size();
		this.errorLine = errorLine;
	    
	    return stations;
	}
	
	
	public Integer getNumCorrectLine() {
		return this.correctLine;
	}
	
	
	public Integer getNumErrorLine() {
		return this.errorLine;
	}

}
