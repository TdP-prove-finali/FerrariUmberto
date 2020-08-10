package it.polito.tdp.CompassBike.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Event.EventType;

public class EventsGenerator {
	
	// TODO Implementare la scelta del periodo di tempo e non solo il giorno
	// TODO Da cambiare StationData con Station
	
	private Graph<Station, RouteEdge> graph;
	
	private Map<Station, Double> percentageStartStations;
	private Map<LocalDateTime, Double> percentageTime;
	
	//private LocalDate day = LocalDate.now().minus(2, ChronoUnit.MONTHS).minus(18, ChronoUnit.DAYS);
	private LocalDate day = LocalDate.of(2020, 5, 20);
	
	private Integer numRentals;
	
	// Incertezza sul numero di noleggi da generare, default 2%.
	private final Double uncertainty = 0.02; // TODO Permettere all'utente di variare questo parametro.
	
	// Aumento o diminuzione del numero di noleggi da generare, default 0%.
	private Double variation = 0.0; // TODO Permettere all'utente di inserire questo parametro.
	
	private Map<Integer, Station> stationsIdMap;
	
	
	/**
	 * Genera gli eventi.
	 * @return
	 */
	public List<Event> generateEvents() {
		List<Event> result = new ArrayList<>();
		
		Random r = new Random();
		Double rangeMin = - this.uncertainty;
		Double rangeMax = this.uncertainty;
		Double randomUncertainty = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		Integer numEvents = (int) (this.numRentals * (1.0 - randomUncertainty + this.variation));
		
		Random rS = new Random();
		Random rT = new Random();
		
		for(int i = 0; i < numEvents; i++) {
			Double cumulative = 0.0;
			Double percentage = rS.nextDouble() * 100.0;
			Station randomStation = null;
			
			for(Station station : this.percentageStartStations.keySet()) {
				cumulative += this.percentageStartStations.get(station);	
				if(cumulative > percentage) {
					randomStation = station;
					break;
				}
			}
			
			cumulative = 0.0;
			percentage = rT.nextDouble() * 100.0;
			LocalDateTime randomTime = null;
			
			for(LocalDateTime time : this.percentageTime.keySet()) {
				cumulative += this.percentageTime.get(time);
				if(cumulative > percentage) {
					randomTime = time;
					break;
				}
			}
			
			if(randomStation != null && randomTime != null) {
				result.add(new Event(EventType.NOLEGGIO, randomStation, randomTime));
			}
			else
				i--;
		}
		
		return result;
	}
	
	
	/**
	 * Carica tutti i parametri necessari a generare casualmente i noleggi necessari alla {@link Simulator simulazione}.
	 */
	public void loadParameters() {
		this.stationsIdMap = StationsDAO.getAllStationsSimulator();
		
		this.percentageStartStations = RentalsDAO.percentageStartStationsDay(day, this.stationsIdMap);
		this.percentageTime = RentalsDAO.percentageTimeDay(day);
		
		this.numRentals = RentalsDAO.getNumRentalsDay(day);
		//System.out.println("Num rentals DB "+this.numRentals);
		
		this.buildGraph();
	}
	
	
	/**
	 * Costruisce il grafo.
	 */
	private void buildGraph() {
		this.graph = new DefaultDirectedWeightedGraph<Station, RouteEdge>(RouteEdge.class);
		
		Graphs.addAllVertices(this.graph, this.stationsIdMap.values());
		
		List<Route> route = RentalsDAO.getAllRouteDay(day, this.stationsIdMap);
		Map<Integer, Map<Integer, Double>> percentageEndStations = RentalsDAO.percentageEndStationsDay(day, this.stationsIdMap);
		
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
	 * Restituisce il grafo.
	 * @return
	 */
	public Graph<Station, RouteEdge> getGraph() {
		return this.graph;
	}
	
	
	// TODO Da passare non da qui ma dal model
	public Map<Integer, Station> getStationsGen() {
		return this.stationsIdMap;
	}
	
	
	/**
	 * Permette di inserire un valore percentuale di variazione del numero di noleggi da generare scelto dall'utente.
	 * @param variation Valore scelto
	 */
	public void setVariation(Double variation) {
		this.variation = variation / 100.0;
	}

}
