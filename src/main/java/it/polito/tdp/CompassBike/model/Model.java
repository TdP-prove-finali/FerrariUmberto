package it.polito.tdp.CompassBike.model;

import java.io.File;
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
	
	private MapsGenerator mapsGenerator;
	
	
	public Model() {
		this.simulator = new Simulator();
		this.mapsGenerator = new MapsGenerator();
	}
	
	
	public void loadFileStations(File file) {
		DataImport.parseJSONStations(file);
	}
	
	
	public void loadFileRentals(File file) {
		DataImport.parseCSVRentals(file);
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
	
	
	public Integer getNumCompletedRent() {
		return this.simulator.getCompletedRent().size();
	}
	
	
	public Integer getNumCanceledRent() {
		return this.simulator.getCanceledRent().size();
	}
	
	
	public Integer getNumEmptyRent() {
		return this.simulator.getNumEmptyRent();
	}
	
	
	public Integer getNumFullRent() {
		return this.simulator.getNumFullRent();
	}
	
	
	public File getMapsStations() {
		return this.mapsGenerator.generateMapStations();
	}
	
	
	public File getMapsResult() {
		return this.mapsGenerator.generateMapResult(this.stations);
	}
	
}
