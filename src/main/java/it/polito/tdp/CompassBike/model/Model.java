package it.polito.tdp.CompassBike.model;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.dataImport.DataImport;

public class Model {
	
	private Simulator simulator;
	private Map<Integer, Station> stations;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Double variation;
	private Double probabilityNewStation;
	
	private Integer numBikes;
	private boolean redistribution;

	private LocalDate startDatePrint;
	private LocalDate endDatePrint;
	
	private Integer dayBetween;
	
	
	private List<GroupRentals> groupsRentals;
	private Integer numBikesDB;
	private Integer numDocks;
	
	
	public Model() {
		this.simulator = new Simulator();
		
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
		this.stations = StationsDAO.getAllStations();
		
		this.startDatePrint = startDatePrint;
		this.endDatePrint = endDatePrint;
		
		this.dayBetween = (int) ChronoUnit.DAYS.between(this.startDate, this.startDatePrint);
	}
	
	
	public void setProbabilityNewStartStation(Double probability) {
		this.probabilityNewStation = probability;
		this.simulator.setProbabilityNewStartStation(probability);
	}
	
	
	public void setNumBikes(Integer numBikes) {
		if(numBikes > this.numDocks)
			numBikes = this.numDocks;
		this.numBikes = numBikes;
		this.simulator.setNumBikes(numBikes);
	}
	
	
	public void setRedistribution(boolean redistribution) {
		this.redistribution = redistribution;
		this.simulator.setRedistributionType(redistribution);
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


	public boolean getRedistribution() {
		return redistribution;
	}
	
	public Map<LocalDate, Integer> getNumCompletedRentDay() {
		Map<LocalDate, Integer> mapResult = new LinkedHashMap<>();
		Map<LocalDate, Integer> map = this.simulator.getNumCompletedRentDay();
		
		for(LocalDate date : map.keySet()) {
			LocalDate newDate = date.plusDays(this.dayBetween);
			mapResult.put(newDate, map.get(date));
		}
		return mapResult;
	}


	public Map<LocalDate, Integer> getNumCanceledRentDay() {
		Map<LocalDate, Integer> mapResult = new LinkedHashMap<>();
		Map<LocalDate, Integer> map = this.simulator.getNumCanceledRentDay();

		for(LocalDate date : map.keySet()) {
			LocalDate newDate = date.plusDays(this.dayBetween);
			mapResult.put(newDate, map.get(date));
		}
		return mapResult;
	}


	public Map<LocalDate, Integer> getNumEmptyRentDay() {
		Map<LocalDate, Integer> mapResult = new LinkedHashMap<>();
		Map<LocalDate, Integer> map = this.simulator.getNumEmptyRentDay();

		for(LocalDate date : map.keySet()) {
			LocalDate newDate = date.plusDays(this.dayBetween);
			mapResult.put(newDate, map.get(date));
		}
		return mapResult;
	}


	public Map<LocalDate, Integer> getNumFullRentDay() {
		Map<LocalDate, Integer> mapResult = new LinkedHashMap<>();
		Map<LocalDate, Integer> map = this.simulator.getNumFullRentDay();

		for(LocalDate date : map.keySet()) {
			LocalDate newDate = date.plusDays(this.dayBetween);
			mapResult.put(newDate, map.get(date));
		}
		return mapResult;
	}
	
}
