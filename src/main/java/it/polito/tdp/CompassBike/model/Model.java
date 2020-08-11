package it.polito.tdp.CompassBike.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.dataImport.DataImport;

public class Model {
	
	private Simulator simulator;
	private Map<Integer, Station> stations;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private Double variation;
	
	
	public Model() {
		this.simulator = new Simulator();
	}
	
	
	public void loadFileStations(String directory) {
		DataImport.parseJSONStations(directory);
	}
	
	
	public void loadFileRentals(String directory) {
		DataImport.parseCSVRentals(directory);
	}
	
	
	public void setParametersSimulation(LocalDate startDate, LocalDate endDate, Double variation) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.variation = variation;
		this.stations = StationsDAO.getAllStationsSimulator();
	}
	
	
	public void runSimulation() {
		this.simulator.init(this.startDate, this.endDate, this.variation, this.stations);
		this.simulator.run();
	}
	
	
	public Map<Integer, Station> getStationsResult() {
		return this.stations;
	}
	
	
	public List<BikeRent> getCompletedRent() {
		return this.simulator.getCompletedRent();
	}
	
	
	public List<BikeRent> getCanceledRent() {
		return this.simulator.getCanceledRent();
	}
	
	
	public Integer getNumRent() {
		return this.simulator.getNumRent();
	}
	
}
