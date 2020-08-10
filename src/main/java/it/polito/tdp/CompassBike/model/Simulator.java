package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.model.Bike.BikeStatus;
import it.polito.tdp.CompassBike.model.BikeRent.BikeRentStatus;
import it.polito.tdp.CompassBike.model.Event.EventType;

public class Simulator {
	
	// TODO Algoritmo per redistribuzione delle bici durante la giornata
	
	// Coda degli eventi
	private Queue<Event> queue;
	
	// Parametri di simulazione
	private EventsGenerator generator;
	
	private final Double probabilityNewStation = 0.60;
	
	// Modello del mondo
	private Graph<Station, RouteEdge> graph;
	
	private Map<Integer, Station> stations;
	private Map<Integer, Bike> bikes;
	
	
	// Valori da calcolare
	private List<BikeRent> completedRentals;
	private List<BikeRent> canceledRentals;
	private List<BikeRent> emptyStationRent;
	private List<BikeRent> fullStationRent;
	private Integer numRent;
	
	
	/**
	 * Inizializza i parametri di simulazione.
	 */
	public void init() { // TODO Passare periodo di tempo
		this.generator = new EventsGenerator();
		this.generator.loadParameters(); // TODO Forse è meglio spostare questi due comandi nel model
		
		this.queue = new PriorityQueue<>();
		this.queue.addAll(this.generator.generateEvents());
		
		this.graph = this.generator.getGraph();
		System.out.println("Vertici: "+this.graph.vertexSet().size()+"\nArchi: "+this.graph.edgeSet().size());
		
		this.stations = StationsDAO.getAllStationsSimulator();
		
		this.bikes = BikesDAO.getAllBikesSimulator();
		this.initBike();
		
		this.completedRentals = new ArrayList<>();
		this.canceledRentals = new ArrayList<>();
		// TODO Clonare rent per aggiungerli alle due liste sotto
		this.emptyStationRent = new ArrayList<>();
		this.fullStationRent = new ArrayList<>();
		this.numRent = 0;
	}


	/**
	 * Effettua la distribuzione iniziale delle bici nel sistema.
	 * Le bici vengono distribuite in maniera uniforme tra le varie stazioni dando precedenza alle stazioni con il maggior numero di docks.
	 */
	private void initBike() {
		List<Bike> listBikes = new ArrayList<>(this.bikes.values());
		Integer numBikes = listBikes.size();
		
		Integer numDocks = 0;
		for(Integer id : this.stations.keySet()) {
			numDocks += this.stations.get(id).getNumDocks();
		}
		
		Integer distribuited = 0;
		
		Double percentage = ((double) numBikes) / ((double) numDocks);
		System.out.println("Percentuale "+percentage);
		
		for(Integer id : this.stations.keySet()) {
			Station st = this.stations.get(id);
			Integer num = (int) (st.getNumDocks() * percentage);
			st.setNumEmptyDocks(st.getNumEmptyDocks() - num);
			
			for(int i = distribuited; i < distribuited + num && i < numBikes; i++) {
				Bike bk = listBikes.get(i);
				bk.setStatus(BikeStatus.STAZIONE);
				bk.setStation(st);
			}
			
			distribuited += num;
			st.setNumBikes(st.getNumBikes() + num);
		}
		
		if(distribuited < numBikes) {
			List<Station> stationsSort = new ArrayList<>(this.stations.values());
			stationsSort.sort(new Comparator<Station>() {
		    public int compare(Station o1, Station o2) {
		    	return o2.getNumDocks() - o1.getNumDocks();
		    }});
			
			do {
				for(Station st : stationsSort) {
					st.setNumBikes(st.getNumBikes() + 1);
					st.setNumEmptyDocks(st.getNumEmptyDocks() - 1);
					distribuited++;
					if(distribuited >= numBikes)
						break;
				}
			} while(distribuited < numBikes);
		}
	}
	
	
	/**
	 * Esegue la simulazione.
	 */
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
	}


	private void processEvent(Event e) {
		// TODO Variabili stesso nome
		// TODO Aggiornare la capienza delle stazioni
		Station startStation = e.getStartStation();
		Station endStation = e.getEndStation(); // NON sono sicuro di questo
		BikeRent bikeRent = e.getBikeRent();
		Bike bike = e.getBike();
		
		switch(e.getType()) {
		case NOLEGGIO:
			// Nuovo noleggio con stato IN_CORSO
			if(startStation.getNumBikes() > 0) {
				bikeRent = new BikeRent(this.numRent++, BikeRentStatus.IN_CORSO, startStation, e.getTime());
				this.queue.add(new Event(EventType.PRELIEVO, startStation, e.getTime(), bikeRent));
			} else {
				bikeRent = new BikeRent(this.numRent++, BikeRentStatus.CAMBIO_STAZIONE, startStation, e.getTime());
				this.queue.add(new Event(EventType.STAZIONE_VUOTA, startStation, e.getTime(), bikeRent));
			}
			break;
			
		case PRELIEVO:
			// Scelgo la bici
			Random r = new Random();
			
			bike = startStation.getBikes().get(r.nextInt(startStation.getBikes().size()));
			bike.setStatus(BikeStatus.NOLEGGIATA);
			bike.setStation(null);
			
			startStation.removeBike(e.getBike());
			startStation.decreaseNumBike(1);
			startStation.increaseNumEmpityDocks(1);
			
			// Scelta della stazione di arrivo
			r = new Random();
			Double percentage = r.nextDouble() * 100.0;
			Double cumulative = 0.0;
			for(Station end : Graphs.successorListOf(this.graph, startStation)) {
				if(cumulative < percentage)
					cumulative += this.graph.getEdgeWeight(this.graph.getEdge(startStation, end));
				else {
					endStation = end;
					break;
				}
			}
			
			// Scelta della durata
			r = new Random();
			Long rangeMin = this.graph.getEdge(startStation, endStation).getMinDuration().toMinutes();
			Long rangeMax = this.graph.getEdge(startStation, endStation).getMaxDuration().toMinutes();
			Long randomMinutes = (long) (rangeMin + (rangeMax - rangeMin) * r.nextDouble());
			Duration randomDuration = Duration.of(randomMinutes, ChronoUnit.MINUTES);
			
			// Aggiorno i dati del noleggio
			bikeRent.setEndStation(endStation);
			bikeRent.setDuration(randomDuration);
			bikeRent.setEndDateTime(e.getTime().plus(randomDuration));
			
			this.queue.add(new Event(EventType.RILASCIO, startStation, bikeRent.getEndDateTime(), endStation, e.getBike(), bikeRent));
			break;
			
		case RILASCIO:
			if(endStation.getNumEmptyDocks() > 0) {
				bike.setStation(endStation);
				bike.setStatus(BikeStatus.STAZIONE);
				
				endStation.addBike(bike);
				endStation.increaseNumBike(1);
				endStation.decreaseNumEmpityDocks(1);
				
				bikeRent.setStatus(BikeRentStatus.COMPLETATO);
				
				this.completedRentals.add(bikeRent);
			} else {
				bikeRent.setStatus(BikeRentStatus.CAMBIO_STAZIONE);
				this.queue.add(new Event(EventType.STAZIONE_PIENA, startStation, e.getTime(), endStation, e.getBike(), bikeRent));
			}
			break;
			
		case STAZIONE_PIENA:
			// TODO Se voglio implementare una casualità per cui l'utente abbandoni la bici nel caso di stazione piena
			// Aggiorno dati del noleggio
			BikeRent cloneFull = bikeRent.clone();
			cloneFull.setStatus(BikeRentStatus.STAZIONE_PIENA);
			endStation.addFullStationRent(cloneFull);
			this.fullStationRent.add(cloneFull);
			
			Station newEndStation = null;
			Duration minDuration = null;
			for(Station end : Graphs.successorListOf(this.graph, endStation)) {
				if(end.getNumEmptyDocks() > 0) {
					RouteEdge edge = this.graph.getEdge(endStation, end);
					if(minDuration == null) {
						minDuration = edge.getMinDuration();
						newEndStation = end;
					}
					else if(edge.getMinDuration().toMinutes() < minDuration.toMinutes()) {
						minDuration = edge.getMinDuration();
						newEndStation = end;
					}
				}
			}
			bikeRent.setEndStation(newEndStation);
			bikeRent.setEndDateTime(e.getTime().plus(minDuration));
			bikeRent.setStatus(BikeRentStatus.IN_CORSO);
			
			this.queue.add(new Event(EventType.RILASCIO, startStation, bikeRent.getEndDateTime(), newEndStation, bike, bikeRent));
			break;
			
		case STAZIONE_VUOTA:
			BikeRent cloneEmpty = bikeRent.clone();
			cloneEmpty.setStatus(BikeRentStatus.STAZIONE_VUOTA);
			startStation.addEmptyStationRent(cloneEmpty);
			this.emptyStationRent.add(cloneEmpty);
			
			r = new Random();
			if(this.probabilityNewStation < r.nextDouble()) {
				Station newStartStation = null;
				minDuration = null;
				for(Station start : Graphs.successorListOf(this.graph, startStation)) {
					if(start.getNumBikes() > 0) {
						RouteEdge edge = this.graph.getEdge(startStation, start);
						if(minDuration == null) {
							minDuration = edge.getMinDuration();
							newStartStation = start;
						}
						else if(edge.getMinDuration().toMinutes() < minDuration.toMinutes()) {
							minDuration = edge.getMinDuration();
							newStartStation = start;
						}
					}
				}
				
				bikeRent.setStartStation(newStartStation);
				bikeRent.setStartDateTime(e.getTime().plus(minDuration));
				bikeRent.setStatus(BikeRentStatus.IN_CORSO);
				
				this.queue.add(new Event(EventType.NOLEGGIO, newStartStation, bikeRent.getStartDateTime(), bikeRent));
				
			} else {
				// Annullamento noleggio
				bikeRent.setStatus(BikeRentStatus.CANCELLATO);
				this.canceledRentals.add(bikeRent);
				startStation.addCanceledRent(bikeRent);
			}
			break;
		}
	}
	

}
