package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.dataImport.StationData;
import it.polito.tdp.CompassBike.model.Station;

public class StationsDAO {
	
	/**
	 * Permette di aggiungere nuove stazioni al db.
	 * Nel caso sia già presente una stazione con lo stesso ID verranno aggiornati i parametri ad essa associati.
	 *  
	 * @param stations La {@link List lista} di {@link StationData stazioni} da aggiungere.
	 */
	public static void addStation(List<StationData> stations) {
		String sql = "INSERT INTO stations VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
	    	conn.setAutoCommit(false);
			PreparedStatement st = conn.prepareStatement(sql);
			
			Integer i = 0;
			
			for(StationData station : stations) {
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
				
				
				st.addBatch();
				i++;
				
				if(i % 1000 == 0 || i == stations.size()) {
					st.executeBatch();
				}
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Permette di ottenere tutte le stazioni attualmente memorizzate nel db, sotto forma di mappa.
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
	
	
	/**
	 * Permette di ottenere i dati sulle stazioni necessari per eseguire la simulazione, vengono inizializzati i parametri relativi al numero di docks e bici.
	 * @return La {@link Map mappa} con l'ID come chiave e l'oggetto {@link StationData stazione} come valore.
	 */
	public static Map<Integer, Station> getAllStationsSimulator() {
		Map<Integer, Station> result = new HashMap<>();
		String sql = "SELECT * " +
				"FROM stations " + 
				"WHERE installed = 1 " + //AND installed_date < NOW() AND (removal_date IS NULL OR removal_date > NOW() + INTERVAL 1 MONTH) " + // TODO Esclude tutte le stazioni con installed_date = null da pensarci
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
