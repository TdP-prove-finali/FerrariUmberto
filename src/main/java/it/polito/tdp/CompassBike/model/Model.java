package it.polito.tdp.CompassBike.model;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.dataImport.DataImport;
import it.polito.tdp.CompassBike.model.Simulator.RedistributionType;

public class Model {
	
	private Simulator simulator;
	private Map<Integer, Station> stations;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Double variation;
	private Double probabilityNewStation;
	
	private Integer numBikes;
	private RedistributionType redistributionType;

	private LocalDate startDatePrint;
	private LocalDate endDatePrint;
	
	private MapsGenerator mapsGenerator;
	
	
	private List<GroupRentals> groupsRentals;
	private Integer numBikesDB;
	private Integer numDocks;
	
	
	public Model() {
		this.simulator = new Simulator();
		this.mapsGenerator = new MapsGenerator();
		
		this.setGroupsRentals();
		this.setNumBikesDB();
		this.setNumDocks();
	}
	
	
	public void loadFileStations(File file) {
		DataImport.parseJSONStations(file);
	}
	
	
	public void loadFileRentals(File file) {
		DataImport.parseCSVRentals(file);
	}
	
	
	public void setParametersSimulation(LocalDate startDate, LocalDate endDate, Double variation, LocalDate startDatePrint, LocalDate endDatePrint) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.variation = variation;
		this.stations = StationsDAO.getAllStationsSimulator();
		
		this.startDatePrint = startDatePrint;
		this.endDatePrint = endDatePrint;
	}
	
	
	public void setProbabilityNewStartStation(Double probability) {
		this.probabilityNewStation = probability;
		this.simulator.setProbabilityNewStartStation(probability);
	}
	
	
	public void setNumBikes(Integer numBikes) {
		this.numBikes = numBikes;
		// TODO Settare bici nel simulatore
	}
	
	
	public void setRedistribution(RedistributionType type) {
		this.redistributionType = type;
		this.simulator.setRedistributionType(type);
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


	public List<GroupRentals> getGroupsRentals() {
		return groupsRentals;
	}


	public void setGroupsRentals() {
		this.groupsRentals = RentalsDAO.getGroupRentals();
	}
	
	
	public boolean isDataAvaiable(LocalDate start, LocalDate end) {
    	for(GroupRentals group : this.groupsRentals) {
    		LocalDate from = group.getFromDate();
    		LocalDate to = group.getToDate();
    		if((from.isBefore(start) || from.equals(start)) && (from.isBefore(end) || from.equals(end)) && (to.isAfter(start) || to.equals(start)) && (to.isAfter(end) || to.equals(end)))
    			return true;
    	}
    	
    	return false;
    }

	
	public Integer getNumBikesDB() {
		return numBikesDB;
	}
	
	
	public Integer getNumBikes() {
		return numBikes;
	}


	public void setNumBikesDB() {
		this.numBikesDB = BikesDAO.getNumBike();
	}


	public Integer getNumDocks() {
		return numDocks;
	}


	public void setNumDocks() {
		this.numDocks = BikesDAO.getNumDocks();
	}


	public LocalDate getStartDatePrint() {
		return startDatePrint;
	}


	public LocalDate getEndDatePrint() {
		return endDatePrint;
	}
	
	
	public LocalDate getStartDate() {
		return startDate;
	}


	public LocalDate getEndDate() {
		return endDate;
	}
	
	
	public Double getVariation() {
		return this.variation;
	}
	
	
	public Double getProbabilityNewStation() {
		return this.probabilityNewStation;
	}


	public RedistributionType getRedistributionType() {
		return redistributionType;
	}
	
}
