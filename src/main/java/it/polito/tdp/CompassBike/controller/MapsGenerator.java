package it.polito.tdp.CompassBike.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Station;

public class MapsGenerator {
	
	private final String HEAD = "<!DOCTYPE html>\r\n" + 
								"<html>\r\n" + 
								"<head>\r\n" + 
								"    <meta charset=\"utf-8\" />\r\n" + 
								"    <title>Mappa stazioni</title>\r\n" + 
								"    <meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,user-scalable=no\" />\r\n" + 
								"    <script src=\"https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.js\"></script>\r\n" + 
								"    <link href=\"https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css\" rel=\"stylesheet\" />\r\n" + 
								"    <link rel=\"icon\" href=\"favicon.png\" type=\"image/png\" />\r\n" +
								"    <style>\r\n" + 
								"        body { margin: 0; padding: 0; }\r\n" + 
								"        #map { position: absolute; top: 0; bottom: 0; width: 100%; }\r\n" + 
								"		 .legend {\r\n" + 
								"            background-color: #fff;\r\n" + 
								"            border-radius: 3px;\r\n" + 
								"            top: 30px;\r\n" + 
								"            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);\r\n" + 
								"            font: 14px/20px 'Helvetica Neue', Arial, Helvetica, sans-serif;\r\n" + 
								"            padding: 10px;\r\n" + 
								"            position: absolute;\r\n" + 
								"            right: 20px;\r\n" + 
								"            z-index: 1;\r\n" + 
								"        }\r\n" + 
								"        .legend h2 {\r\n" + 
								"            margin: 0 0 10px;\r\n" + 
								"        }\r\n" + 
								"        .legend div {\r\n" + 
								"            margin: 0 0 5px;\r\n" + 
								"        }\r\n" + 
								"        .legend div span {\r\n" + 
								"            border-radius: 50%;\r\n" + 
								"            display: inline-block;\r\n" + 
								"            height: 12px;\r\n" + 
								"            margin-right: 5px;\r\n" + 
								"            width: 12px;\r\n" + 
								"        }" +
								".mapboxgl-popup-close-button {\r\n" + 
								"  display: none;\r\n" + 
								"}\r\n" + 
								"    </style>\r\n" + 
								"</head>";
	private final String BODY_START = "<body>\r\n" + 
								"<div id=\"map\"></div>\r\n";							
	private final String BODY_END = "</script>\r\n" + 
								"</body>\r\n" + 
								"</html>";
	
	private final String SCRIPT = "<script>\r\n" +
								"mapboxgl.accessToken = ";
	private final String MAP = "var map = new mapboxgl.Map({\r\n" + 
								"        container: 'map',\r\n" + 
								"        style: 'mapbox://styles/mapbox/streets-v11',\r\n" + 
								"        center: ";

	private final String KEY = "'pk.eyJ1IjoidW1mZXJyYXJpIiwiYSI6ImNrZHQ5YnFvZzB6Zmcyd2xpejQxMTlqcmgifQ.4qzD5w0tVvXER5iNPd7Puw'";
	private final String ZOOM = "11.8";
	private final String POPUP = "var popup = new mapboxgl.Popup({ offset: 25 }).setHTML(";
	private final String MARKER = " var marker = new mapboxgl.Marker({color: ";
	
	private final String LEGEND_STATIONS = "<div id=\"legend\" class=\"legend\">\r\n" + 
								"    <h2>Legenda</h2>\r\n" + 
								"    <div><span style=\"background-color: blue\"></span>Stazioni installate sul territorio</div>\r\n" + 
								"    <div><span style=\"background-color: red\"></span>Stazioni aggiunte manualmente dall'utente</div>\r\n" + 
								"    <br \\>" +
								"    <div><i>Selezionare una stazione per visualizzarne le informazioni</i></div>\r\n" +
								"</div>\r\n";
	private final String LEGEND_RESULT = "<div id=\"legend\" class=\"legend\">\r\n" + 
								"    <h2>Legenda</h2>\r\n" + 
								"    <div><span style=\"background-color: green\"></span>Stazioni che non presentano problemi rilevanti</div>\r\n" + 
								"    <div><span style=\"background-color: red\"></span>Stazioni ad alto traffico con problemi rilevanti</div>\r\n" + 
								"    <div><span style=\"background-color: orange\"></span>Stazioni spesso vuote</div>\r\n" + 
								"    <div><span style=\"background-color: blue\"></span>Stazioni spesso piene</div>\r\n" + 
								"    <br \\>" +
								"    <div><i>Selezionare una stazione per visualizzarne le informazioni</i></div>\r\n" +
								"</div>\r\n";

	
	
	public File	generateMapStations() {
		File map = new File(getClass().getResource("/maps/mapStations.html").getFile());
		Map<Integer, Station> stations = StationsDAO.getAllStations();
		
		Double[] center = StationsDAO.getCenterArea();
		
		try {
			FileWriter fw = new FileWriter(map);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter writer = new PrintWriter(bw);
		    
			writer.write(this.HEAD);
			writer.write(this.BODY_START+this.LEGEND_STATIONS+this.SCRIPT+this.KEY+";\r\n");
			writer.write(this.MAP);
			writer.write("["+center[1]+", "+center[0]+"],\r\n");
			writer.write("zoom: "+this.ZOOM+"});\r\n");
			
			String myPopUp = "";
			String myMarker = "";
			for(Integer id : stations.keySet()) {
				myPopUp = this.POPUP + this.toStringHTMLStation(stations.get(id)) + ");\r\n";
				
				String color = "'blue'";
				if(id > 9000)
					color = "'red'";
				
				myMarker = this.MARKER + color + "})" + ".setLngLat([" + stations.get(id).getLongitude() + ", " + stations.get(id).getLatitude() + "])" + ".setPopup(popup).addTo(map);\r\n";
				
				writer.write(myPopUp);
				writer.write(myMarker);
			}
			
			writer.write(this.BODY_END);
			
			writer.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Impossibile accedere alla mappa!");
		}
		
		return map;
	}
	
	
	public File generateMapResult(Map<Integer, Station> stations) {
		File map = new File(getClass().getResource("/maps/mapResult.html").getFile());
		
		Double[] center = StationsDAO.getCenterArea();
		
		try {
			FileWriter fw = new FileWriter(map);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter writer = new PrintWriter(bw);
		    
			writer.write(this.HEAD);
			writer.write(this.BODY_START+this.LEGEND_RESULT+this.SCRIPT+this.KEY+";\r\n");
			writer.write(this.MAP);
			writer.write("["+center[1]+", "+center[0]+"],\r\n");
			writer.write("zoom: "+this.ZOOM+"});\r\n");
			
			String myPopUp = "";
			String myMarker = "";
			for(Integer id : stations.keySet()) {
				myPopUp = this.POPUP + this.toStringHTMLResult(stations.get(id)) + ");\r\n";
				
				String color = this.colorStation(stations.get(id));
				myMarker = this.MARKER + color + "})" + ".setLngLat([" + stations.get(id).getLongitude() + ", " + stations.get(id).getLatitude() + "])" + ".setPopup(popup).addTo(map);\r\n";
				
				writer.write(myPopUp);
				writer.write(myMarker);
			}
			
			writer.write(this.BODY_END);
			
			writer.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Impossibile accedere alla mappa!");
		}
		
		return map;
	}
	
	
	private String toStringHTMLStation(Station station) {
		String result = "";
		
		result += "\"";
		result += "<b>" + station.getCommonName() + "</b>";
		result += "<br \\>";
		result += "<i>ID: </i>" + station.getId();
		result += "<br \\><br \\>";
		result += "<i>Numero docks: </i>" + station.getNumDocks();
		result += "<br \\>";
		result += "\"";
		
		return result;
	}
	
	
	private String toStringHTMLResult(Station station) {
		String result = "";
		
		result += "\"";
		result += "<b>" + station.getCommonName() + "</b>";
		result += "<br \\>";
		result += "<i>ID: </i>" + station.getId();
		result += "<br \\><br \\>";
		result += "<i>Noleggi completati: </i>" + station.getCompletedRent().size();
		result += "<br \\>";
		result += "<i>Noleggi cancellati: </i>" + station.getCanceledRent().size();
		result += "<br \\>";
		result += "<i>Tentativi di noleggio falliti (bici non disponibili): </i>" + station.getEmptyStationRent().size();
		result += "<br \\>";
		result += "<i>Tentativi di riconsegna falliti (stazione piena): </i>" + station.getFullStationRent().size();
		result += "<br \\>";
		result += "\"";
		
		return result;
	}
	
	
	private String colorStation(Station station) {
		String color = "";
		
		switch(station.getProblemType()) {
			case NESSUNO:
				color = "'green'";
				break;
			case PIENA:
				color = "'blue'";
				break;
			case TRAFFICO:
				color = "'red'";
				break;
			case VUOTA:
				color = "'orange'";
				break;
		}
		
		return color;
	}
	
}
