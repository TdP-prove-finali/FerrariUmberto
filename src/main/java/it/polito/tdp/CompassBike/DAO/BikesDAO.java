package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.dataImport.BikeData;
import it.polito.tdp.CompassBike.model.Bike;
import it.polito.tdp.CompassBike.model.Bike.BikeStatus;

public class BikesDAO {
	
	/**
	 * Permette di aggiungere nuove {@link BikeData bici} al db, nel caso in cui esista già un bici con lo stesso ID ne vengono aggiornati i parametri.
	 * @param bikes La {@link List lista} di {@link BikeData bici} da aggiungere
	 */
	public static void addBike(List<BikeData> bikes) {
		String sql = "INSERT INTO bike VALUES(?, ?) ON DUPLICATE KEY UPDATE bike_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
	    	conn.setAutoCommit(false);
			PreparedStatement st = conn.prepareStatement(sql);
			
			Integer i = 0;
			
			for(BikeData bike : bikes) {
				st.setInt(1, bike.getBikeId());
				st.setInt(2, bike.getStationId());
				st.setInt(3, bike.getBikeId());
			
				st.addBatch();
				i++;
				
				if(i % 1000 == 0 || i == bikes.size()) {
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
	 * Restituisce il numero di bici memorizzate nel db.
	 * @return Il numero di bici
	 */
	public static Integer getNumBike() {
		String sql = "SELECT COUNT(bike_id) AS num " +
				"FROM bike";
		Integer result = null;
		
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next()) {
				result = res.getInt("num");
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Restituisce il numero totale di docks presenti nel sistema, considerando tutte le {@link Station stazioni} memorizzate nel db.
	 * @return Il numero di docks
	 */
	public static Integer getNumDocks() {
		String sql = "SELECT SUM(num_docks) AS num " +
				"FROM stations";
		Integer result = null;
		
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if(res.next()) {
				result = res.getInt("num");
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Permette di ottenere i dati sulle {@link Bike bici} necessari per la simulazione.
	 * @return La {@link Map mappa} con l'ID della bici come chiave e l'oggetto {@link Bike bici} come valore.
	 */
	public static Map<Integer, Bike> getAllBikesSimulator() {
		String sql = "SELECT * " +
				"FROM bike " + 
				"ORDER BY bike_id";
		Map<Integer, Bike> result = new HashMap<>();
		
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer id = res.getInt("bike_id");
				result.put(id, new Bike(id, null, false, BikeStatus.DA_DISTRIBUIRE));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
