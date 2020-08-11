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
			LocalDateTime randomTime = null;
			
			Map<LocalDateTime, Double> tempPercentageTime = this.percentageTime.get(randomDay);
			for(LocalDateTime time : tempPercentageTime.keySet()) {
				cumulative += tempPercentageTime.get(time);
				randomTime = time;
				if(cumulative > percentage) {
					break;
				}
			}
			
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
			this.percentageTime.put(day, RentalsDAO.percentageTimeDay(day));
		}
		
		this.numRentals = RentalsDAO.getNumRentalsPeriod(this.startDate, this.endDate);
		//System.out.println("Num rentals DB "+this.numRentals);
		
		this.buildGraph();
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

}
