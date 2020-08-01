package it.polito.tdp.CompassBike.model;

import java.time.LocalDate;

public class Station {
	
	private Integer id;
	private String commonName;
	private Integer terminalName;
	private boolean isInstalled;
	private boolean isLocked;
	private LocalDate installDate;
	private LocalDate removalDate;
	private boolean isTemporary;
	private Integer numBikes;
	private Integer numEmptyDocks;
	private Integer numDocks;
	private Double latitude;
	private Double longitude;
	private boolean isBroken;
	
	
	public Station(Integer id, String commonName, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.commonName = commonName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Integer getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(Integer terminalName) {
		this.terminalName = terminalName;
	}

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public LocalDate getInstallDate() {
		return installDate;
	}

	public void setInstallDate(LocalDate installDate) {
		this.installDate = installDate;
	}

	public LocalDate getRemovalDate() {
		return removalDate;
	}

	public void setRemovalDate(LocalDate removalDate) {
		this.removalDate = removalDate;
	}

	public boolean isTemporary() {
		return isTemporary;
	}
	
	public void setTemporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
	}

	public Integer getNumBikes() {
		return numBikes;
	}

	public void setNumBikes(Integer numBikes) {
		this.numBikes = numBikes;
	}

	public Integer getNumEmptyDocks() {
		return numEmptyDocks;
	}

	public void setNumEmptyDocks(Integer numEmpityDocks) {
		this.numEmptyDocks = numEmpityDocks;
	}

	public Integer getNumDocks() {
		return numDocks;
	}

	public void setNumDocks(Integer numDocks) {
		this.numDocks = numDocks;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public Integer getId() {
		return id;
	}

	public String getCommonName() {
		return commonName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	
	public String toString() {
		return this.id+" - "+this.commonName;
	}

}
