package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.CompassBike.dataImport.Rental;
import it.polito.tdp.CompassBike.model.Bike;
import it.polito.tdp.CompassBike.model.Bike.BikeStatus;

public class BikesDAO {
	
	/**
	 * Aggiunge una nuova bici al db.
	 * @param rental
	 */
	public static void addBike(Rental rental) {
		String sql = "INSERT INTO bike VALUES(?, ?) ON DUPLICATE KEY UPDATE bike_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, rental.getBikeId());
			st.setInt(2, rental.getEndStationId());
			st.setInt(3, rental.getBikeId());
			
			st.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Permette di ottenere il numero di bici presenti nel sistema.
	 * @return
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
	 * Ottenere il numero di poste considerando tutte le stazioni.
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
	 * Dati sulle bici per la simulazione
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
