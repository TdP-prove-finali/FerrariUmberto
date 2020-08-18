package it.polito.tdp.CompassBike.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.model.Event.EventType;

public class EventsGenerator {
	
	private Graph<Station, RouteEdge> graph;
	
	private Map<Station, Double> percentageStartStations;
	private Map<LocalDate, Double> percentageDayPeriod;
	private Map<LocalDate, Map<LocalDateTime, Double>> percentageTime;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Integer numRentals;
	
	// Incertezza sul numero di noleggi da generare, default 2%.
	private Double uncertainty = 0.02;
	
	// Aumento o diminuzione del numero di noleggi da generare, default 0%.
	private Double variation = 0.0;
	
	private Map<Integer, Station> stationsIdMap;
	
	private final Integer NUM_NEAR_STATIONS = 5;
	
	
	/**
	 * Genera gli {@link Event eventi} sulla base dei parametri caricati.
	 * @return La {@link List lista} degli eventi generati
	 */
	public List<Event> generateEvents() {
		Long inizio = System.currentTimeMillis();
		List<Event> result = new ArrayList<>();
		
		Random r = new Random();
		Double rangeMin = - this.uncertainty;
		Double rangeMax = this.uncertainty;
		Double randomUncertainty = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		Integer numEvents = (int) (this.numRentals * (1.0 - randomUncertainty + this.variation));
		
		Random rS = new Random();
		Random rD = new Random();
		Random rT = new Random();
		Random rM = new Random();
		
		for(int i = 0; i < numEvents; i++) {
			Double cumulative = 0.0;
			Double percentage = rS.nextDouble() * 100.0;
			Station randomStation = null;
			
			for(Station station : this.percentageStartStations.keySet()) {
				cumulative += this.percentageStartStations.get(station);	
				randomStation = station;
				if(cumulative > percentage) {
					break;
				}
			}
			
			cumulative = 0.0;
			percentage = rD.nextDouble() * 100.0;
			LocalDate randomDay = null;
			
			for(LocalDate day : this.percentageDayPeriod.keySet()) {
				cumulative += this.percentageDayPeriod.get(day);
				randomDay = day;
				if(cumulative > percentage) {
					break;
				}
			}
			
			cumulative = 0.0;
			percentage = rT.nextDouble() * 100.0;
			LocalDateTime randomHalf = null;
			
			Map<LocalDateTime, Double> tempPercentageTime = this.percentageTime.get(randomDay);
			for(LocalDateTime time : tempPercentageTime.keySet()) {
				cumulative += tempPercentageTime.get(time);
				randomHalf = time;
				if(cumulative > percentage) {
					break;
				}
			}
			
			LocalDateTime randomTime = null;
			Integer minutes = rM.nextInt(30);
			randomTime = randomHalf.plusMinutes(minutes);
			
			if(randomStation != null && randomTime != null) {
				result.add(new Event(EventType.NOLEGGIO, randomStation, randomTime));
			}
			else
				i--;
		}
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println("TEMPO GENERAZIONE "+durata / 1000.0+"\n");

		return result;
	}
	
	
	/**
	 * Carica tutti i parametri necessari a generare casualmente i noleggi necessari alla {@link Simulator simulazione}.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 */
	public void loadParameters(LocalDate startDate, LocalDate endDate, Map<Integer, Station> stationsIdMap) {
		Long inizio = System.currentTimeMillis();
		this.startDate = startDate;
		this.endDate = endDate;
		
		this.stationsIdMap = stationsIdMap;
		
		this.percentageStartStations = RentalsDAO.percentageStartStationsPeriod(this.startDate, this.endDate, this.stationsIdMap);
		
		this.percentageDayPeriod = RentalsDAO.percentageDayPeriod(this.startDate, this.endDate);
		this.percentageTime = new HashMap<>();
		for(LocalDate day = this.startDate; day.isBefore(this.endDate.plusDays(1)); day = day.plusDays(1)) {
			this.percentageTime.put(day, RentalsDAO.percentageHalfHourDay(day));
		}
		
		this.numRentals = RentalsDAO.getNumRentalsPeriod(this.startDate, this.endDate);
		
		this.buildGraph();
		
		this.setPercentageUserStation();
		
		Long fine = System.currentTimeMillis();
		Long durata = fine - inizio;
		System.out.println("TEMPO LOAD "+durata / 1000.0+"\n");
	}
	
	
	/**
	 * Costruisce il grafo.
	 */
	private void buildGraph() {
		this.graph = new DefaultDirectedWeightedGraph<Station, RouteEdge>(RouteEdge.class);
		
		Graphs.addAllVertices(this.graph, this.stationsIdMap.values());
		
		List<Route> route = RentalsDAO.getAllRoutePeriod(this.startDate, this.endDate, this.stationsIdMap);
		Map<Integer, Map<Integer, Double>> percentageEndStations = RentalsDAO.percentageEndStationsPeriod(this.startDate, this.endDate, this.stationsIdMap);
		
		for(Route r : route) {
			if(this.graph.containsVertex(r.getStartStation()) && this.graph.containsVertex(r.getEndStation())) {
				r.getStartStation().getId();
				r.getEndStation().getId();
				percentageEndStations.get(r.getStartStation().getId()).get(r.getEndStation().getId());
				RouteEdge edge = new RouteEdge(r.getMinDuration(), r.getMaxDuration());
				this.graph.addEdge(r.getStartStation(), r.getEndStation(), edge);
				this.graph.setEdgeWeight(edge, percentageEndStations.get(r.getStartStation().getId()).get(r.getEndStation().getId()));
			}
		}
	}
	
	
	/**
	 * Setta le percentuali relative alle stazioni inserite manualmente dall'utente, sulla base dei dati delle 5 stazioni più vicine.
	 */
	private void setPercentageUserStation() {
		for(Integer id : this.stationsIdMap.keySet()) {
			if(this.percentageStartStations.get(this.stationsIdMap.get(id)) == null) {
				Station userSt = this.stationsIdMap.get(id);
				
				List<DistanceStations> listDistance = new ArrayList<>();
				for(Station st : this.stationsIdMap.values()) {
					if(this.percentageStartStations.get(st) != null)
						listDistance.add(new DistanceStations(userSt, st, this.distanceBetween(userSt.getLatitude(), userSt.getLongitude(), st.getLatitude(), st.getLongitude())));
				}
				
				listDistance.sort(null);
				Double percentageUserStation = 0.0;
				for(int i = 0; i < this.NUM_NEAR_STATIONS; i++) {
					Station st = listDistance.get(i).getEndStation();
					Double percentageSt = this.percentageStartStations.get(st);
					
					percentageUserStation += percentageSt * 0.2;
					percentageSt *= 0.8;
					
					this.percentageStartStations.remove(st);
					this.percentageStartStations.put(st, percentageSt);
					
					
					for(RouteEdge edge : this.graph.outgoingEdgesOf(st)) {
						Station endStation = this.graph.getEdgeTarget(edge);
						if(!this.graph.containsEdge(userSt, endStation)) {
							RouteEdge newEdge = new RouteEdge(edge.getMinDuration().plusSeconds(listDistance.get(i).getDistance().intValue()), edge.getMaxDuration().plusSeconds(listDistance.get(i).getDistance().intValue()));
							this.graph.addEdge(userSt, endStation, newEdge);
							this.graph.setEdgeWeight(newEdge, this.graph.getEdgeWeight(edge));
						}
					}
					
					
					for(Station startSt : this.stationsIdMap.values()) {
						Double percentageEndUserSt = 0.0;
						Integer count = 0;
						Integer first = null;
						Double percTotal = 0.0;
						for(RouteEdge edge : this.graph.outgoingEdgesOf(startSt)) {
							for(int j = 0; j < this.NUM_NEAR_STATIONS; j++) {
								if(this.graph.getEdgeTarget(edge).equals(listDistance.get(j).getEndStation())) {
									if(first == null)
										first = j;
									count++;
								}
							}
						}
						
						if(first != null && count >= this.NUM_NEAR_STATIONS - 2) {
							for(RouteEdge edge : this.graph.outgoingEdgesOf(startSt)) {
								for(int j = 0; j < this.NUM_NEAR_STATIONS; j++) {
									if(this.graph.getEdgeTarget(edge).equals(listDistance.get(j).getEndStation())) {
										Double edgeWeight = this.graph.getEdgeWeight(edge);
										percTotal += edgeWeight;
										percentageEndUserSt += edgeWeight * 0.2;
										edgeWeight *= 0.8;
										this.graph.setEdgeWeight(edge, edgeWeight);
									}
								}
							}
							
							RouteEdge edge = this.graph.getEdge(startSt, listDistance.get(first).getEndStation());
							RouteEdge newEdge = new RouteEdge(edge.getMinDuration().plusSeconds(listDistance.get(i).getDistance().intValue()), edge.getMaxDuration().plusSeconds(listDistance.get(i).getDistance().intValue()));
							this.graph.addEdge(startSt, userSt, newEdge);
							this.graph.setEdgeWeight(newEdge, percentageEndUserSt);
						}
						
					}
				}
				this.percentageStartStations.remove(userSt);
				this.percentageStartStations.put(userSt, percentageUserStation);
				
			}
		}
		
	}
	
	
	public Graph<Station, RouteEdge> getGraph() {
		return this.graph;
	}
	
	
	/**
	 * Permette di inserire un valore percentuale di variazione del numero di noleggi da generare scelto dall'utente.
	 * @param variation Valore scelto
	 */
	public void setVariation(Double variation) {
		this.variation = variation / 100.0;
	}
	
	
	/**
	 * Permette di variare il valore percentuale di incertezza sul numero di noleggi da generare.
	 * @param uncertainty Valore di incertezza scelto
	 */
	public void setUncertainty(Double uncertainty) {
		this.uncertainty = uncertainty;
	}
	
	
	/**
	 * Permette di calcolare la distanza (in metri) tra due punti, espressi in latitudine e longitudine.
	 */
	private Double distanceBetween(Double lat1, Double long1, Double lat2, Double long2) {
	    Double earthRadius = (double) 6371000;
	    Double dLat = Math.toRadians(lat2-lat1);
	    Double dLong = Math.toRadians(long2-long1);
	    Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLong/2) * Math.sin(dLong/2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    Double dist = (Double) (earthRadius * c);

	    return dist;
	}

}
