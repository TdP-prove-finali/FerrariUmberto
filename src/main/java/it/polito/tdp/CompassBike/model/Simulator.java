package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.LocalDateTime;
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
import it.polito.tdp.CompassBike.model.Bike.BikeStatus;
import it.polito.tdp.CompassBike.model.BikeRent.BikeRentStatus;
import it.polito.tdp.CompassBike.model.Event.EventType;

public class Simulator {
	
	// TODO Algoritmo per redistribuzione delle bici durante la giornata
	
	// Coda degli eventi
	private Queue<Event> queue;
	
	// Parametri di simulazione
	private EventsGenerator generator;
	
	private final Double probabilityNewStation = 0.60; // TODO La faccio variare?
	
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
		this.generator.setVariation(-10.0);
		this.generator.loadParameters(); // TODO Forse è meglio spostare questi tre comandi nel model e passare il Generator
		
		this.queue = new PriorityQueue<>();
		this.queue.addAll(this.generator.generateEvents());
		
		this.graph = this.generator.getGraph();
		//System.out.println("Vertici: "+this.graph.vertexSet().size()+"\nArchi: "+this.graph.edgeSet().size());
		
		this.stations = this.generator.getStationsGen();
		
		this.bikes = BikesDAO.getAllBikesSimulator();
		this.initBike();
		
		this.completedRentals = new ArrayList<>();
		this.canceledRentals = new ArrayList<>();
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
		
		for(Integer id : this.stations.keySet()) {
			Station st = this.stations.get(id);
			Integer num = (int) (st.getNumDocks() * percentage);
			
			for(int i = distribuited; i < distribuited + num && i < numBikes; i++) {
				Bike bk = listBikes.get(i);
				bk.setStatus(BikeStatus.STAZIONE);
				bk.setStation(st);
				st.addBike(bk);
			}
			
			distribuited += num;
			st.decreaseNumEmpityDocks(num);
			st.increaseNumBike(num);
		}
		
		if(distribuited < numBikes) {
			List<Station> stationsSort = new ArrayList<>(this.stations.values());
			stationsSort.sort(new Comparator<Station>() {
		    public int compare(Station o1, Station o2) {
		    	return o2.getNumDocks() - o1.getNumDocks();
		    }});
			
			do {
				for(Station st : stationsSort) {
					st.increaseNumBike(1);
					st.decreaseNumEmpityDocks(1);
					
					Bike bk = listBikes.get(distribuited++);
					bk.setStatus(BikeStatus.STAZIONE);
					bk.setStation(st);
					
					st.addBike(bk);
					
					if(distribuited >= numBikes)
						break;
				}
			} while(distribuited < numBikes);
		}

		//System.out.println("Distribuite "+distribuited+" Bici "+numBikes);
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


	/**
	 * Processa il singolo evento.
	 */
	private void processEvent(Event e) {
		Station startStation = e.getStartStation();
		Station endStation = e.getEndStation();
		BikeRent bikeRent = e.getBikeRent();
		
		switch(e.getType()) {
		case NOLEGGIO:
			if(startStation.getNumBikes() > 0) {
				if(bikeRent == null)
					bikeRent = new BikeRent(this.numRent++, BikeRentStatus.IN_CORSO, startStation, e.getTime());
				this.queue.add(new Event(EventType.PRELIEVO, startStation, e.getTime(), bikeRent));
			} else {
				if(bikeRent == null)
					bikeRent = new BikeRent(this.numRent++, BikeRentStatus.CAMBIO_STAZIONE, startStation, e.getTime());
				this.queue.add(new Event(EventType.STAZIONE_VUOTA, startStation, e.getTime(), bikeRent));
			}
			break;
			
		case PRELIEVO:
			Bike randomBike = this.getRandomBike(startStation);
			
			Station randomEndStation = this.getRandomEndStation(startStation);
			
			Duration randomDuration = this.getRandomDuration(startStation, randomEndStation);
			
			// Aggiorno i dati del noleggio
			LocalDateTime endTime = e.getTime().plus(randomDuration);
			bikeRent.setEndStation(randomEndStation);
			bikeRent.setDuration(randomDuration);
			bikeRent.setEndDateTime(endTime);
			
			this.queue.add(new Event(EventType.RILASCIO, startStation, endTime, randomEndStation, randomBike, bikeRent));
			break;
			
		case RILASCIO:
			if(endStation.getNumEmptyDocks() > 0) {
				// Riconsegna della bici
				Bike bike = e.getBike();
				bike.setStation(endStation);
				bike.setStatus(BikeStatus.STAZIONE);
				
				endStation.addBike(bike);
				endStation.increaseNumBike(1);
				endStation.decreaseNumEmpityDocks(1);
				
				bikeRent.setStatus(BikeRentStatus.COMPLETATO);
				
				this.completedRentals.add(bikeRent);
				startStation.addCompletedRent(bikeRent);
				endStation.addCompletedRent(bikeRent);
			} else {
				//Stazione piena
				bikeRent.setStatus(BikeRentStatus.CAMBIO_STAZIONE);
				this.queue.add(new Event(EventType.STAZIONE_PIENA, startStation, e.getTime(), endStation, e.getBike(), bikeRent));
			}
			break;
			
		case STAZIONE_PIENA:
			// TODO Se voglio implementare una casualità per cui l'utente abbandoni la bici nel caso di stazione piena
			
			// Salvo l'informazione che c'è stato un problema dovuto alla stazione piena
			BikeRent cloneFull = bikeRent.clone();
			cloneFull.setStatus(BikeRentStatus.STAZIONE_PIENA);
			endStation.addFullStationRent(cloneFull);
			this.fullStationRent.add(cloneFull);
			
			Station newEndStation = this.getNearestEmptyStation(endStation);
			
			Duration durationToNewEndStation = this.graph.getEdge(endStation, newEndStation).getMinDuration();
			
			LocalDateTime newEndTime = e.getTime().plus(durationToNewEndStation);
			bikeRent.setEndStation(newEndStation);
			bikeRent.setEndDateTime(newEndTime);
			bikeRent.setStatus(BikeRentStatus.IN_CORSO);
			
			this.queue.add(new Event(EventType.RILASCIO, startStation, newEndTime, newEndStation, e.getBike(), bikeRent));
			break;
			
		case STAZIONE_VUOTA:
			// Salvo l'informazione che c'è stato un problema dovuto alla stazione vuota
			BikeRent cloneEmpty = bikeRent.clone();
			cloneEmpty.setStatus(BikeRentStatus.STAZIONE_VUOTA);
			startStation.addEmptyStationRent(cloneEmpty);
			this.emptyStationRent.add(cloneEmpty);
			
			Random r = new Random();
			if(this.probabilityNewStation < r.nextDouble()) {
				// Nuova stazione per prelevare la bici
				Station newStartStation = this.getNearestFullStation(startStation);
				Duration durationToNewStartStation = this.graph.getEdge(startStation, newStartStation).getMinDuration();
				
				LocalDateTime newStartTime = e.getTime().plus(durationToNewStartStation);
				
				bikeRent.setStartStation(newStartStation);
				bikeRent.setStartDateTime(newStartTime);
				bikeRent.setStatus(BikeRentStatus.IN_CORSO);
				
				this.queue.add(new Event(EventType.NOLEGGIO, newStartStation, newStartTime, bikeRent));
				
			} else {
				// Annullamento noleggio
				bikeRent.setStatus(BikeRentStatus.CANCELLATO);
				this.canceledRentals.add(bikeRent);
				startStation.addCanceledRent(bikeRent);
			}
			break;
		}
	}


	/**
	 * Sceglie casualmente una stazione di arrivo.
	 * @param station {@link Station Stazione} di partenza
	 * @return La {@link Station stazione} di arrivo scelta
	 */
	private Station getRandomEndStation(Station startStation) {
		Random r = new Random();
		Station result = null;
		
		Double percentage = r.nextDouble() * 100.0;
		Double cumulative = 0.0;
		
		for(Station endStation : Graphs.successorListOf(this.graph, startStation)) {
			cumulative += this.graph.getEdgeWeight(this.graph.getEdge(startStation, endStation));
			result = endStation;
			if(cumulative > percentage) {
				break;
			}
		}
		
		if(result == null) System.out.println("TORNO NULL "+percentage);
		return result;
	}
	
	
	/**
	 * Restituisce una durata casuale per il noleggio, questa durata deve essere compresa tra la durata massima e minima di tutti i noleggi tra le due stazioni.
	 * @param startStation {@link Station Stazione} di partenza
	 * @param endStation {@link Station Stazione} di arrivo
	 * @return La {@link Duration durata} casuale
	 */
	private Duration getRandomDuration(Station startStation, Station endStation) { // TODO Da fare meglio, si potrebbe fare con una probabilità per ogni Duration ma viene complesso
		Random r = new Random();
		if(endStation == null) System.out.println("END NULL");
		Long rangeMin = this.graph.getEdge(startStation, endStation).getMinDuration().toMinutes();
		Long rangeMax = this.graph.getEdge(startStation, endStation).getMaxDuration().toMinutes();
		Long randomMinutes = (long) (rangeMin + (rangeMax - rangeMin) * r.nextDouble());
		
		Duration result = Duration.of(randomMinutes, ChronoUnit.MINUTES);
		
		return result;
	}


	/**
	 * Sceglie casualmente una bicicletta presente nella stazione passata come parametro e la rimuove dalla stazione.
	 * @param station {@link Station Stazione} da cui prelevare la bici
	 * @return La {@link Bike bici} scelta
	 */
	private Bike getRandomBike(Station station) {
		Random r = new Random();
		
		Bike bike = station.getBikes().get(r.nextInt(station.getBikes().size()));
		bike.setStatus(BikeStatus.NOLEGGIATA);
		bike.setStation(null);
		
		station.removeBike(bike);
		station.decreaseNumBike(1);
		station.increaseNumEmpityDocks(1);
		
		return bike;
	}
	
	
	/**
	 * Restituisce la stazione più vicina a quella passata come parametro con almeno una dock libera.
	 * @param station {@link Station Stazione} in cui si trova l'utente
	 * @return La {@link Station stazione} più vicina con almeno un posto libero
	 */
	private Station getNearestEmptyStation(Station station) {
		Station result = null;
		Duration minDuration = null;
		
		for(Station near : Graphs.successorListOf(this.graph, station)) {
			if(near.getNumEmptyDocks() > 0) {
				RouteEdge edge = this.graph.getEdge(station, near);
				
				if(minDuration == null) {
					minDuration = edge.getMinDuration();
					result = near;
				}
				else if(edge.getMinDuration().toMinutes() < minDuration.toMinutes()) {
					minDuration = edge.getMinDuration();
					result = near;
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * Restituisce la stazione più vicina a quella passata come parametro con almeno una bici disponibile.
	 * @param station {@link Station Stazione} in cui si trova l'utente
	 * @return La {@link Station stazione} più vicina con almeno una bici disponibile
	 */
	private Station getNearestFullStation(Station station) {
		Station result = null;
		Duration minDuration = null;
		
		Integer conta = 0;
		for(Station near : Graphs.successorListOf(this.graph, station)) {
			if(near.getNumBikes() > 0) {
				conta++;
				RouteEdge edge = this.graph.getEdge(station, near);
				if(minDuration == null) {
					minDuration = edge.getMinDuration();
					result = near;
				}
				else if(edge.getMinDuration().toMinutes() < minDuration.toMinutes()) {
					minDuration = edge.getMinDuration();
					result = near;
				}
			}
		}
		
		return result;
	}
	
	
	public Integer getNumCompletedRent() {
		return this.completedRentals.size();
	}
	
	public Integer getNumCanceledRent() {
		return this.canceledRentals.size();
	}
	
	public Integer getNumRent() {
		return this.numRent;
	}
	
	// TODO Metodo provvisorio
	public Map<Integer, Station> getStations() {
		return this.stations;
	}
	
	

}
