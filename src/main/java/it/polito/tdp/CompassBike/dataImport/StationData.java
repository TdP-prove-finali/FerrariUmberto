package it.polito.tdp.CompassBike.dataImport;

import java.time.LocalDate;

public class StationData {
	
	private Integer id;
	private String commonName;
	private boolean isInstalled;
	private LocalDate installDate;
	private LocalDate removalDate;
	private Integer numDocks;
	private Double latitude;
	private Double longitude;
	
	
	public StationData(Integer id, String commonName, Double latitude, Double longitude) {
		this.id = id;
		this.commonName = commonName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	

	public StationData(Integer id, String commonName, boolean isInstalled, LocalDate installDate, LocalDate removalDate, 
			Integer numDocks, Double latitude, Double longitude) {
		this.id = id;
		this.commonName = commonName;
		this.isInstalled = isInstalled;
		this.installDate = installDate;
		this.removalDate = removalDate;
		this.numDocks = numDocks;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
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

	public Integer getNumDocks() {
		return numDocks;
	}

	public void setNumDocks(Integer numDocks) {
		this.numDocks = numDocks;
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
