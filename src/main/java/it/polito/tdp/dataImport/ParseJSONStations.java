package it.polito.tdp.dataImport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import it.polito.tdp.CompassBike.model.Station;

public class ParseJSONStations {
	
	/**
	 * Metodo di utilità per leggere un file JSON che contiene le informazioni sulle stazioni
	 * @return ritorna la lista delle stazioni
	 */
	public static List<Station> parse(String directory) {
		Path path = Paths.get("C:\\Users\\Umberto\\Dropbox\\PoliTO\\Tesi\\Dataset\\Stazioni\\s.txt");
		List<Station> stations = new ArrayList<Station>();
 
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
				
				Station station = new Station(id, commonName, latitude, longitude);
				/*
				JSONArray properties = jsonStation.getJSONArray("additionalProperties");
				for(int j = 0; j < properties.length(); i++) {
					JSONObject property = properties.getJSONObject(j);
					String key = property.getString("key");
					
					switch(key) {
						case "TerminalName":
							station.setTerminalName(property.getString("value"));
							break;
						case "Installed": 
							station.setInstalled(Boolean.parseBoolean(property.getString("value")));
							break;
						case "Locked": 
							station.setLocked(Boolean.parseBoolean(property.getString("value")));
							break;
						case "InstallDate":
							station.setInstallDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "RemovalDate":
							station.setRemovalDate(LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(property.getString("value"))), TimeZone.getDefault().toZoneId()));
							break;
						case "Temporary":
							station.setTemporary(Boolean.parseBoolean(property.getString("value")));
							break;
						case "NbBikes":
							try {
								station.setNumBikes(Integer.parseInt(property.getString("value")));

							} catch(NumberFormatException e) {
								station.setNumBikes(0);
							}
							break;
						case "NbEmpityDocks":
							try {
								station.setNumEmpityDocks(Integer.parseInt(property.getString("value")));

							} catch(NumberFormatException e) {
								station.setNumEmpityDocks(0);
							}
							break;
						case "NbDocks":
							try {
								station.setNumDocks(Integer.parseInt(property.getString("value")));

							} catch(NumberFormatException e) {
								station.setNumDocks(0);
							}
							break;
					}
				}
				if(station.getNumDocks() - (station.getNumBikes() + station.getNumEmpityDocks()) != 0)
					station.setBroken(true);
				*/
				stations.add(station);
			} catch(JSONException e) {}
		}
	    
	    return stations;
	}

}
