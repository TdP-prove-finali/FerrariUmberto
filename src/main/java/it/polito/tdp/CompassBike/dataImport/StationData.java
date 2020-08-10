package it.polito.tdp.CompassBike.dataImport;

import java.time.LocalDate;

public class StationData {
	
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
	
	
	public StationData(Integer id, String commonName, Double latitude, Double longitude) {
		this.id = id;
		this.commonName = commonName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	

	public StationData(Integer id, String commonName, Integer terminalName, boolean isInstalled, boolean isLocked,
			LocalDate installDate, LocalDate removalDate, boolean isTemporary, Integer numBikes, Integer numEmptyDocks,
			Integer numDocks, Double latitude, Double longitude, boolean isBroken) {
		this.id = id;
		this.commonName = commonName;
		this.terminalName = terminalName;
		this.isInstalled = isInstalled;
		this.isLocked = isLocked;
		this.installDate = installDate;
		this.removalDate = removalDate;
		this.isTemporary = isTemporary;
		this.numBikes = numBikes;
		this.numEmptyDocks = numEmptyDocks;
		this.numDocks = numDocks;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isBroken = isBroken;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StationData other = (StationData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String toString() {
		return this.id+" - "+this.commonName;
	}

}
