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
	 * Permette di aggiungere una nuova stazione al db, inserita manualmente dall'utente.
	 * Nel caso sia già presente una stazione con lo stesso ID verranno aggiornati i parametri ad essa associati.
	 *  
	 * @param station La {@link Station stazione} da aggiungere.
	 */
	public static void addStationUser(Station station) {
		String sql = "INSERT INTO stations VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, station.getId());
			st.setInt(15, station.getId());
			
			st.setString(2, station.getCommonName());
			st.setInt(3, 0);
			st.setBoolean(4, true);
			st.setBoolean(5, false);
			st.setDate(6, null);
			st.setDate(7, null);
			st.setBoolean(8, false);
			st.setInt(9, station.getNumBikes());
			st.setInt(10, station.getNumEmptyDocks());
			st.setInt(11, station.getNumDocks());
			st.setDouble(12, station.getLatitude());
			st.setDouble(13, station.getLongitude());
			st.setBoolean(14, false);

			st.execute();

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Permette di eliminare una stazione inserita manualmente dall'utente dal db.
	 *  
	 * @param station La {@link Station stazione} da eliminare.
	 */
	public static void deleteStationUser(Station station) {
		String sql = "DELETE FROM stations WHERE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, station.getId());

			st.execute();

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Permette di aggiornare i parametri di una stazione
	 *  
	 * @param station La {@link Station stazione} a cui modificare i parametri.
	 */
	public static void updateStation(Station station) {
		String sql = "UPDATE stations SET num_docks = ?, num_bikes = ? WHERE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, station.getNumDocks());
			st.setInt(2, station.getNumBikes());
			
			st.setInt(3, station.getId());

			st.execute();

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
	 * Permette di ottenere tutte le stazioni inserite manualmente dall'utente attualmente memorizzate nel db, sotto forma di mappa.
	 * @return La {@link Map mappa} con l'ID come chiave e l'oggetto {@link StationData stazione} come valore.
	 */
	public static Map<Integer, Station> getAllStationsUser() {
		Map<Integer, Station> result = new HashMap<>();
		String sql = "SELECT * " +
				"FROM stations " + 
				"WHERE installed = 1 AND station_id > 9000 " +
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
				"WHERE installed = 1 " + 
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
	
	
	/**
	 * Permette di ottenere il numero di stazioni attualmente memorizzate nel db.
	 * @return Il numero di stazioni
	 */
	public static Integer getNumStations() {
		Integer result = null;
		String sql = "SELECT COUNT(station_id) AS num FROM stations";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next())
				result = res.getInt("num");
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Permette di ottenere l'ultimo ID delle stazioni inserite manualmente dall'utente.
	 * ID oltre 9000 sono quelli utilizzati per le stazioni inserite manualmente dall'utente.
	 * @return L'ultimo ID utilizzato
	 */
	public static Integer getLastIdUserStation() {
		//Id oltre 9000 sono quelli utilizzati per le stazioni inserite manualmente dall'utente
		String sql = "SELECT MAX(station_id) AS id " + 
				"FROM stations " + 
				"WHERE station_id > 9000";
		Integer result = null;
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next())
				result = res.getInt("id");
			
			if(result == 0)
				result = 9000;
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static boolean isInsideArea(Double lat, Double lon) {
		String sql = "SELECT MAX(latitude) AS maxLat, MIN(latitude) AS minLat, MAX(longitude) AS maxLon, MIN(longitude) AS minLon " + 
				"FROM stations " +
				"WHERE station_id < 9000";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next()) {
				Double maxLat = res.getDouble("maxLat");
				Double minLat = res.getDouble("minLat");
				Double maxLon = res.getDouble("maxLon");
				Double minLon = res.getDouble("minLon");
				
				System.out.println(maxLat+" "+minLat);
				System.out.println(maxLon+" "+minLon);
				System.out.println(lat+" "+lon);
				
				if(lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon) {
					System.out.println("Entra qui");
					return true;
				}
					
			}
			
			conn.close();
			return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static Double[] getCenterArea() {
		String sql = "SELECT MAX(latitude) AS maxLat, MIN(latitude) AS minLat, MAX(longitude) AS maxLon, MIN(longitude) AS minLon " + 
				"FROM stations " +
				"WHERE station_id < 9000";
		Double[] result = new Double[2];
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next()) {
				Double maxLat = res.getDouble("maxLat");
				Double minLat = res.getDouble("minLat");
				Double maxLon = res.getDouble("maxLon");
				Double minLon = res.getDouble("minLon");
				
				Double centerLat = (maxLat - minLat) / 2.0 + minLat;
				Double centerLon = (maxLon - minLon) / 2.0 + minLon;
				
				result[0] = centerLat;
				result[1] = centerLon;
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
