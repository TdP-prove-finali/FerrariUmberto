package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.CompassBike.dataImport.Rental;
import it.polito.tdp.CompassBike.model.Route;
import it.polito.tdp.CompassBike.model.Station;

public class RentalsDAO { // TODO Tutta la documentazione
	
	// TODO Considerare tutte le restrizioni sul singolo giorno se corrette o da fare sul periodo

	/**
	 * Permette di aggiungere un nuovo noleggio al database, si tratta di noleggi effettivi del servizio.
	 * @param rental noleggio da aggiungere
	 */
	public static void addRental(Rental rental) { // TODO Aggiungere più noleggi con una sola connessione
		String sql = "INSERT INTO rentals VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE rental_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, rental.getId());
			st.setInt(8, rental.getId());
			st.setLong(2, rental.getDuration().toMillis());
			st.setInt(3, rental.getBikeId());
			st.setTimestamp(4, Timestamp.valueOf(rental.getEndDate()));
			st.setInt(5, rental.getEndStationId());
			st.setTimestamp(6, Timestamp.valueOf(rental.getStartDate()));
			st.setInt(7, rental.getStartStationId());
			
			st.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Restituisce il numero di noleggio del giorno passato come parametro.
	 * @param day
	 * @return
	 */
	public static Integer getNumRentalsDay(LocalDate day) {
		Integer result = null;
		String sql = "SELECT COUNT(*) AS num " + 
				"FROM rentals " +
				"WHERE DATE(end_date) = ? AND DATE(start_date) = ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(day));
			st.setDate(2, Date.valueOf(day));
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
	 * Percentuali stazioni di partenza (ristretto ad un giorno).
	 * @param day
	 * @return
	 */
	public static Map<Station, Double> percentageStartStationsDay(LocalDate day, Map<Integer, Station> stationsIdMap) {
		Map<Station, Double> result = new HashMap<>();
		String sql = "SELECT start_station_id, COUNT(*)/(SUM(COUNT(*)) OVER())*100 AS perc " + 
				"FROM rentals, stations " + 
				"WHERE DATE(end_date) = ? AND DATE(start_date) = ? AND rentals.start_station_id = stations.station_id " + 
				"GROUP BY start_station_id " + 
				"ORDER BY perc DESC";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(day));
			st.setDate(2, Date.valueOf(day));
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer id = res.getInt("start_station_id");
				if(stationsIdMap.containsKey(id))
					result.put(stationsIdMap.get(id), res.getDouble("perc"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Percentuale per ogni fascia oraria della giornata.
	 * @param day
	 * @return
	 */
	public static Map<LocalDateTime, Double> percentageTimeDay(LocalDate day) {
		Map<LocalDateTime, Double> result = new HashMap<>();
		String sql = "SELECT start_date, COUNT(*)/(SUM(COUNT(*)) OVER())*100 AS perc " + 
				"FROM rentals " + 
				"WHERE DATE(start_date) = ? AND DATE(end_date) = ? " + 
				"GROUP BY start_date " + 
				"ORDER BY perc DESC";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(day));
			st.setDate(2, Date.valueOf(day));
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.put(res.getTimestamp("start_date").toLocalDateTime(), res.getDouble("perc"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Durata massima e minima per ogni itinerario.
	 * @param stationsIdMap
	 * @return
	 */
	public static List<Route> getAllRouteDay(LocalDate day, Map<Integer, Station> stationsIdMap) {
		String sql = "SELECT start_station_id, end_station_id, MIN(duration) AS min, MAX(DURATION) AS max " + 
				"FROM rentals " + 
				"WHERE DATE(start_date) = ? AND DATE(end_date) = ? " + 
				"GROUP BY start_station_id, end_station_id " + 
				"ORDER BY start_station_id";
		List<Route> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(day));
			st.setDate(2, Date.valueOf(day));
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer startId = res.getInt("start_station_id");
				Integer endId = res.getInt("end_station_id");
				
				if(stationsIdMap.containsKey(startId) && stationsIdMap.containsKey(endId)) {
					// TODO Capire l'uso di Duration con il DB, se passare/leggere Long è corretto
					Route route = new Route(stationsIdMap.get(startId), stationsIdMap.get(endId), Duration.of(res.getLong("min"), ChronoUnit.MINUTES), Duration.of(res.getLong("max"), ChronoUnit.MINUTES));
					result.add(route);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Percentuale stazioni arrivo diviso per stazioni di partenza.
	 * @return
	 */
	public static Map<Integer, Map<Integer, Double>> percentageEndStationsDay(LocalDate day, Map<Integer, Station> stationsIdMap) {
		String sql = "SELECT start_station_id, end_station_id, COUNT(*)/(SUM(COUNT(*)) OVER (PARTITION BY start_station_id))*100 AS perc " + 
				"FROM rentals " +
				"WHERE DATE(end_date) = ? AND DATE(start_date) = ? " +
				"GROUP BY start_station_id, end_station_id " + 
				"ORDER BY start_station_id";
		Map<Integer, Map<Integer, Double>> result = new HashMap<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(day));
			st.setDate(2, Date.valueOf(day));
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Integer startId = res.getInt("start_station_id");
				Integer endId = res.getInt("end_station_id");
				Double perc = res.getDouble("perc");
				if(result.containsKey(startId)) {
					Map<Integer, Double> temp = result.get(startId);
					if(stationsIdMap.containsKey(endId)) {
						temp.put(endId, perc);
						result.remove(startId);
						result.put(startId, temp);
					}
				} else {
					if(stationsIdMap.containsKey(endId)) {
						Map<Integer, Double> temp = new HashMap<>();
						temp.put(endId, perc);
						result.put(startId, temp);
					}
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
