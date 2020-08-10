package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.CompassBike.dataImport.StationData;
import it.polito.tdp.CompassBike.model.Station;

public class StationsDAO {
	
	/**
	 * Permette di aggiungere una nuova stazione al db.
	 * Nel caso sia già presente una stazione con lo stesso ID verranno aggiornati i parametri ad essa associati.
	 * 
	 * @param station La {@link StationData stazione} da aggiungere.
	 */
	public static void addStation(StationData station) {
		String sql = "INSERT INTO stations VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, station.getId());
			st.setInt(15, station.getId());
			
			st.setString(2, station.getCommonName());
			st.setInt(3, station.getTerminalName());
			st.setBoolean(4, station.isInstalled());
			st.setBoolean(5, station.isLocked());
			if(station.getInstallDate() != null)
				st.setDate(6, Date.valueOf(station.getInstallDate()));
			else
				st.setDate(6, null);
			if(station.getRemovalDate() != null)
				st.setDate(7, Date.valueOf(station.getRemovalDate()));
			else
				st.setDate(7, null);
			st.setBoolean(8, station.isTemporary());
			st.setInt(9, station.getNumBikes());
			st.setInt(10, station.getNumEmptyDocks());
			st.setInt(11, station.getNumDocks());
			st.setDouble(12, station.getLatitude());
			st.setDouble(13, station.getLongitude());
			st.setBoolean(14, station.isBroken());
			
			st.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Permette di ottenere tutte le stazioni attualmente memorizzate nel db, sotto forma di mappa.
	 * 
	 * @return La {@link Map mappa} con l'ID come chiave e l'oggetto {@link StationData stazione} come valore.
	 */
	public static Map<Integer, Station> getAllStations() {
		Map<Integer, Station> result = new HashMap<>();
		String sql = "SELECT * " +
				"FROM stations " + 
				"WHERE installed = 1 " +
				"ORDER BY station_id";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Station station = new Station(res.getInt("station_id"), res.getString("common_name"),
						res.getInt("num_bikes"), res.getInt("num_empty_docks"), res.getInt("num_docks"), res.getDouble("latitude"), res.getDouble("longitude"));
				
				result.put(station.getId(), station);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// TODO Forse ridondate
	/**
	 * Dati sulle stazioni per la simulazione
	 */
	public static Map<Integer, Station> getAllStationsSimulator() {
		Map<Integer, Station> result = new HashMap<>();
		// TODO Esclude tutte le stazioni con installed_date = null da pensarci
		String sql = "SELECT * " +
				"FROM stations " + 
				"WHERE installed = 1 AND installed_date < NOW() AND (removal_date IS NULL OR removal_date > NOW() + INTERVAL 1 MONTH) " +
				"ORDER BY station_id";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Station station = new Station(res.getInt("station_id"), res.getString("common_name"),
						0, res.getInt("num_docks"), res.getInt("num_docks"), res.getDouble("latitude"), res.getDouble("longitude"));
				
				result.put(station.getId(), station);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}