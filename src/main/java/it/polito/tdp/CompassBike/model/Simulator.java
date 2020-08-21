package it.polito.tdp.CompassBike.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
import it.polito.tdp.CompassBike.model.Station.ProblemType;

public class Simulator {
	
	// Coda degli eventi
	private Queue<Event> queue;
	
	// Parametri di simulazione
	private EventsGenerator generator;
	
	private boolean redistribution;
	
	// Probabilità che l'utente si rechi alla stazione più vicina nel caso in cui quella in cui si trova sia piena
	private Double probabilityNewStartStation = 0.60;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private Double variation;
	
	private Integer numBikes;
	
	private final LocalTime SAVE_DATA_TIME = LocalTime.of(23, 59);
	
	// Modello del mondo
	private Graph<Station, RouteEdge> graph;
	
	private Map<Integer, Station> stations;
	private Map<Integer, Bike> bikes;
	
	// Valori da calcolare
	private List<BikeRent> completedRent;
	private List<BikeRent> canceledRent;
	private List<BikeRent> emptyStationRent;
	private List<BikeRent> fullStationRent;
	private Integer numRent;
	
	private Map<LocalDate, Integer> numCompletedRentDay;
	private Map<LocalDate, Integer> numCanceledRentDay;
	private Map<LocalDate, Integer> numEmptyRentDay;
	private Map<LocalDate, Integer> numFullRentDay;
	
	
	
	public Simulator() {
		this.generator = new EventsGenerator();
	}
	
	
	/**
	 * Inizializza i parametri di simulazione.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @param variation Variazione percentuale sul numero di noleggi da generare nel periodo di tempo
	 */
	public void init(LocalDate startDate, LocalDate endDate, Double variation, Map<Integer, Station> stations) {	
		this.startDate = startDate;
		this.endDate = endDate;
		this.variation = variation;
		
		this.stations = stations;
		
		this.generator.setVariation(this.variation);
		this.generator.loadParameters(this.startDate, this.endDate, this.stations);
		
		this.queue = new PriorityQueue<>();
		this.queue.addAll(this.generator.generateEvents());
		
		if(this.redistribution) {
			this.MAX_MOVEMENTS = (int) (this.stations.size() * 0.05);
			this.MAX_BIKES = (int) (this.numBikes * 0.05);
			for (LocalDate date = this.startDate; date.isBefore(this.endDate.plusDays(1)); date = date.plusDays(1)) {
				this.queue.add(new Event(EventType.RIDISTRIBUZIONE, null, LocalDateTime.of(date, this.TIME_REDISTRIBUTION)));
			}
		}
		
		for(LocalDate date = this.startDate; date.isBefore(this.endDate.plusDays(1)); date = date.plusDays(1)) {
			this.queue.add(new Event(EventType.SALVA_DATI, null, LocalDateTime.of(date, this.SAVE_DATA_TIME)));
		}
		
		this.graph = this.generator.getGraph();
		
		this.bikes = BikesDAO.getAllBikesSimulator();
		this.initBike();
		
		this.completedRent = new ArrayList<>();
		this.canceledRent = new ArrayList<>();
		this.emptyStationRent = new ArrayList<>();
		this.fullStationRent = new ArrayList<>();
		this.numRent = 0;
		
		this.numCompletedRentDay = new LinkedHashMap<>();
		this.numCanceledRentDay = new LinkedHashMap<>();
		this.numEmptyRentDay = new LinkedHashMap<>();
		this.numFullRentDay = new LinkedHashMap<>();
	}


	/**
	 * Effettua la distribuzione iniziale delle bici nel sistema, eventualmente aggiungendo nuove bici inserite dall'utente.
	 * Le bici vengono distribuite in maniera uniforme tra le varie stazioni dando precedenza alle stazioni con il maggior numero di docks.
	 */
	private void initBike() {
		List<Bike> listBikes = new ArrayList<>(this.bikes.values());
		listBikes.sort(null);
		Integer lastId = null;
		if(listBikes.isEmpty()) 
			lastId = 0;
		else 
			lastId = listBikes.get(listBikes.size()-1).getId();
		
		
		Integer numBikesDB = listBikes.size();
		
		Integer numDocks = BikesDAO.getNumDocks();
		
		
		if(numBikes > numBikesDB) {
			for(int k = numBikesDB; k < numBikes; k++) {
				listBikes.add(new Bike(++lastId, null, false, BikeStatus.DA_DISTRIBUIRE));
			}
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
		    	return -o1.getNumDocks().compareTo(o2.getNumDocks());
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
		
	}
	
	
	/**
	 * Esegue la simulazione.
	 */
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
		
		this.setProblems();
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
				
				this.completedRent.add(bikeRent);
				startStation.addCompletedRent(bikeRent);
				endStation.addCompletedRent(bikeRent);
			} else {
				//Stazione piena
				bikeRent.setStatus(BikeRentStatus.CAMBIO_STAZIONE);
				this.queue.add(new Event(EventType.STAZIONE_PIENA, startStation, e.getTime(), endStation, e.getBike(), bikeRent));
			}
			break;
			
		case STAZIONE_PIENA:
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
			
			// Nuova stazione per prelevare la bici
			Station newStartStation = this.getNearestFullStation(startStation);
			
			Random r = new Random();
			if(this.probabilityNewStartStation > r.nextDouble() && newStartStation != null) {
				Duration durationToNewStartStation = this.graph.getEdge(startStation, newStartStation).getMinDuration();
				
				LocalDateTime newStartTime = e.getTime().plus(durationToNewStartStation);
				
				bikeRent.setStartStation(newStartStation);
				bikeRent.setStartDateTime(newStartTime);
				bikeRent.setStatus(BikeRentStatus.IN_CORSO);
				
				this.queue.add(new Event(EventType.NOLEGGIO, newStartStation, newStartTime, bikeRent));
				
			} else {
				// Annullamento noleggio
				bikeRent.setStatus(BikeRentStatus.CANCELLATO);
				this.canceledRent.add(bikeRent);
				startStation.addCanceledRent(bikeRent);
			}
			break;
		case RIDISTRIBUZIONE:
			this.redistribution();
			break;
		case SALVA_DATI:
			LocalDate day = e.getTime().toLocalDate();
			
			Integer numBefore = 0;
			for(LocalDate date : this.numCompletedRentDay.keySet()) {
				numBefore += this.numCompletedRentDay.get(date);
			}
			this.numCompletedRentDay.put(day, this.completedRent.size() - numBefore);
			
			
			numBefore = 0;
			for(LocalDate date : this.numCanceledRentDay.keySet()) {
				numBefore += this.numCanceledRentDay.get(date);
			}
			this.numCanceledRentDay.put(day, this.canceledRent.size() - numBefore);
			
			
			numBefore = 0;
			for(LocalDate date : this.numEmptyRentDay.keySet()) {
				numBefore += this.numEmptyRentDay.get(date);
			}
			this.numEmptyRentDay.put(day, this.emptyStationRent.size() - numBefore);
			
			
			
			numBefore = 0;
			for(LocalDate date : this.numFullRentDay.keySet()) {
				numBefore += this.numFullRentDay.get(date);
			}
			this.numFullRentDay.put(day, this.fullStationRent.size() - numBefore);
			
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
		
		return result;
	}
	
	
	/**
	 * Restituisce una durata casuale per il noleggio, questa durata deve essere compresa tra la durata massima e minima di tutti i noleggi tra le due stazioni.
	 * @param startStation {@link Station Stazione} di partenza
	 * @param endStation {@link Station Stazione} di arrivo
	 * @return La {@link Duration durata} casuale
	 */
	private Duration getRandomDuration(Station startStation, Station endStation) {
		Random r = new Random();
		
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
		
		List<RouteEdge> listEdge = new ArrayList<>(this.graph.outgoingEdgesOf(station));
		listEdge.sort(null);
		for(RouteEdge edge : listEdge) {
			Station near = this.graph.getEdgeTarget(edge);
			if(near.getNumEmptyDocks() > 0) {
				result = near;
				break;
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

		List<RouteEdge> listEdge = new ArrayList<>(this.graph.outgoingEdgesOf(station));
		listEdge.sort(null);
		for(RouteEdge edge : listEdge) {
			Station near = this.graph.getEdgeTarget(edge);
			if(near.getNumBikes() > 0) {
				result = near;
				break;
			}
		}
		
		return result;
	}
	
	
	/**
	 * Identifica e setta per ogni stazione il problema più rilevante.
	 */
	private void setProblems() {
		Double avgCanceled = ((double) this.canceledRent.size()) / ((double) this.stations.size());
		Double avgEmpty = ((double) this.emptyStationRent.size()) / ((double) this.stations.size());
		Double avgFull = ((double) this.fullStationRent.size()) / ((double) this.stations.size());
		
		Double avgTotal = (((double) this.completedRent.size()) + ((double) this.canceledRent.size()) + ((double) this.emptyStationRent.size()) + ((double) this.fullStationRent.size())) / ((double) this.stations.size());
		
		Double percCanceled = 0.0;
		Double percEmpty = 0.0;
		Double percFull = 0.0;
		Double percTotal = 0.0;
		
		for(Station st : this.stations.values()) {
			percCanceled = st.getNumCanceledRent() / avgCanceled;
			percEmpty = st.getNumEmptyStationRent() / avgEmpty;
			percFull = st.getNumFullStationRent() / avgFull;
			percTotal = (st.getNumCompletedRent() + st.getNumCanceledRent() + st.getNumEmptyStationRent() + st.getNumFullStationRent()) / avgTotal;
			
			
			if(percEmpty > 3)
				st.setProblemType(ProblemType.VUOTA);
			else if(percFull > 3)
				st.setProblemType(ProblemType.PIENA);
			else {
				if(percTotal > 1 && ((percEmpty > 1 && percFull > 1) || (percEmpty > 1 && percCanceled > 1) || (percFull > 1 && percCanceled > 1))) 
					st.setProblemType(ProblemType.TRAFFICO);
				else if(percEmpty > percFull && percEmpty > 1)
					st.setProblemType(ProblemType.VUOTA);
				else if(percFull > percEmpty && percFull > 1)
					st.setProblemType(ProblemType.PIENA);
				else 
					st.setProblemType(ProblemType.NESSUNO);
			}
			
		}

	}
	
	
	/**
	 * Permette di settare un numero di {@link Bike bici} scelto dall'utente.
	 * @param numBikes Numero di bici scelto.
	 */
	public void setNumBikes(Integer numBikes) {
		this.numBikes = numBikes;
	}
	
	
	/**
	 * Permette di inserire la probabilità per cui un utente cerchi una nuova stazioni da cui noleggiare una bici nel caso in cui quella in cui si trova risulti vuota.
	 * @param probability La probabilità scelta
	 */
	public void setProbabilityNewStartStation(Double probability) {
		this.probabilityNewStartStation = probability / 100.0;
	}
	
	
	public List<BikeRent> getCompletedRent() {
		return this.completedRent;
	}
	
	public List<BikeRent> getCanceledRent() {
		return this.canceledRent;
	}
	
	public Integer getNumRent() {
		return this.numRent;
	}
	
	public Integer getNumEmptyRent() {
		return this.emptyStationRent.size();
	}
	
	public Integer getNumFullRent() {
		return this.fullStationRent.size();
	}


	public void setRedistributionType(boolean redistribution) {
		this.redistribution = redistribution;
	}
	
	
	
	private Integer MAX_MOVEMENTS;
	private Integer MAX_BIKES;
	private final LocalTime TIME_REDISTRIBUTION = LocalTime.of(4, 0);
	
	
	/**
	 * Algoritmo di ridistribuzione, bilancia il numero di bici tra le stazioni con un tasso di riempimento maggiore e quelle con un tasso di riempimento minore.
	 */
	public void redistribution() {
		List<Station> listStations = new ArrayList<>(this.stations.values());
		
		listStations.sort(new ComparatorStationsBikes());
		
		Integer numBikesStCollected = 0;
		Integer numBikesStDistribuited = 0;
		Integer numDocksStCollected = 0;
		Integer numDocksStDistribuited = 0;
		Integer numEmptyStDistribuited = 0;
		
		for(int i = 0; i < this.MAX_MOVEMENTS; i++) {
			numBikesStCollected += listStations.get(i).getNumBikes();
			numBikesStDistribuited += listStations.get(listStations.size() - this.MAX_MOVEMENTS + i).getNumBikes();
			
			numDocksStCollected += listStations.get(i).getNumDocks();
			numDocksStDistribuited += listStations.get(listStations.size() - this.MAX_MOVEMENTS + i).getNumDocks();
			
			numEmptyStDistribuited += listStations.get(listStations.size() - this.MAX_MOVEMENTS + i).getNumEmptyDocks();
			
		}
		
		Double fillRate = (((double) numBikesStCollected) + ((double) numBikesStDistribuited)) / (((double) numDocksStCollected) + ((double) numDocksStDistribuited));
		
		List<Bike> collectedBikes = new ArrayList<>();
		Integer[] toCollectSt = new Integer[this.MAX_MOVEMENTS];
		Integer countStop = 0;
		
		do {
			for(int i = 0; i < this.MAX_MOVEMENTS; i++) {
				Station st = listStations.get(i);
				
				if(toCollectSt[i] == null) {
					Integer temp = (int) (fillRate * st.getNumDocks());
					toCollectSt[i] = st.getNumBikes() - temp;
				}
				
				if(toCollectSt[i] > 0 && collectedBikes.size() < this.MAX_BIKES && collectedBikes.size() < numEmptyStDistribuited) {
					Bike bk = st.getBikes().get(st.getNumBikes() - 1);
					st.removeBike(bk);
					
					bk.setStatus(BikeStatus.DA_DISTRIBUIRE);
					bk.setStation(null);
					st.decreaseNumBike(1);
					st.increaseNumEmpityDocks(1);
					
					collectedBikes.add(bk);
					
					toCollectSt[i]--;
				} else {
					countStop++;
				}
			}
		} while(countStop < this.MAX_MOVEMENTS);
		
		
		Collections.reverse(listStations);
		
		Integer[] toDistributeSt = new Integer[this.MAX_MOVEMENTS];
		countStop = 0;
		
		do {
			for(int i = 0; i < this.MAX_MOVEMENTS; i++) {
				Station st = listStations.get(i);
				
				if(toDistributeSt[i] == null) {
					Integer temp = (int) (fillRate * st.getNumDocks());
					toDistributeSt[i] = temp - st.getNumBikes() + 1;
				}
				
				if(toDistributeSt[i] > 0 && !collectedBikes.isEmpty() && st.getNumEmptyDocks() > 0) {
					Bike bk = collectedBikes.get(collectedBikes.size() - 1);
					collectedBikes.remove(bk);
					
					bk.setStation(st);
					bk.setStatus(BikeStatus.STAZIONE);
					st.addBike(bk);
					st.increaseNumBike(1);
					st.decreaseNumEmpityDocks(1);
					
					toDistributeSt[i]--;
				} else {
					countStop++;
				}
			}
		} while(countStop < this.MAX_MOVEMENTS);
		
		
		
		if(!collectedBikes.isEmpty()) {
			do {
				for(int i = 0; i < this.MAX_MOVEMENTS; i++) {
					Station st = listStations.get(i);
					
					if(st.getNumEmptyDocks() > 0) {
						Bike bk = collectedBikes.get(collectedBikes.size() - 1);
						collectedBikes.remove(bk);
						
						bk.setStation(st);
						bk.setStatus(BikeStatus.STAZIONE);
						st.addBike(bk);
						st.increaseNumBike(1);
						st.decreaseNumEmpityDocks(1);
					}
					
					if(collectedBikes.isEmpty())
						break;
				}
			} while(!collectedBikes.isEmpty());
		}
		
	}
	
	
	public Map<LocalDate, Integer> getNumCompletedRentDay() {
		return numCompletedRentDay;
	}


	public Map<LocalDate, Integer> getNumCanceledRentDay() {
		return numCanceledRentDay;
	}


	public Map<LocalDate, Integer> getNumEmptyRentDay() {
		return numEmptyRentDay;
	}


	public Map<LocalDate, Integer> getNumFullRentDay() {
		return numFullRentDay;
	}


	/**
	 * Classe che permette l'ordinamento delle stazioni a seconda del loro tasso di riempimento
	 */
	private class ComparatorStationsBikes implements Comparator<Station> {
		public int compare(Station o1, Station o2) {
			Double o1Div = ((double) o1.getNumBikes()) / ((double) o1.getNumDocks());
			Double o2Div = ((double) o2.getNumBikes()) / ((double) o2.getNumDocks());
        	return -o1Div.compareTo(o2Div);
  		}  
   }	
	
}
