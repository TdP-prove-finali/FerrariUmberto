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

public class RentalsDAO {

	/**
	 * Permette di aggiungere nuovi noleggi al db.
	 * @param rentals {@link List lista} di {@link Rental noleggi} da aggiungere
	 */
	public static void addRental(List<Rental> rentals) {
		String sql = "INSERT INTO rentals VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE rental_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
	    	conn.setAutoCommit(false);
			PreparedStatement st = conn.prepareStatement(sql);
			
			Integer i = 0;
			
			for(Rental rental : rentals) {
				st.setInt(1, rental.getId());
				st.setInt(8, rental.getId());
				st.setInt(2, (int) rental.getDuration().toSeconds());
				st.setInt(3, rental.getBikeId());
				st.setTimestamp(4, Timestamp.valueOf(rental.getEndDate()));
				st.setInt(5, rental.getEndStationId());
				st.setTimestamp(6, Timestamp.valueOf(rental.getStartDate()));
				st.setInt(7, rental.getStartStationId());
				
				st.addBatch();
				i++;
				
				if(i % 1000 == 0 || i == rentals.size()) {
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
	 * Restituisce il numero di noleggio relativi all'intervallo di tempo passato.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @return Il numero di noleggi
	 */
	public static Integer getNumRentalsPeriod(LocalDate startDate, LocalDate endDate) {
		Integer result = null;
		String sql = "SELECT COUNT(*) AS num " + 
				"FROM rentals " +
				"WHERE DATE(start_date) >= ? AND DATE(end_date) <= ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(startDate));
			st.setDate(2, Date.valueOf(endDate));
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
	 * Permette di ottenere la percentuale di noleggi iniziati in ogni stazione del sistema, considerando i soli noleggi relativi all'intervallo di tempo passato.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @param stationsIdMap idMap relativa alle stazioni di interesse
	 * @return La {@link Map mappa} con la {@link Station stazione} come chiave e la percentuale come valore.
	 */
	public static Map<Station, Double> percentageStartStationsPeriod(LocalDate startDate, LocalDate endDate, Map<Integer, Station> stationsIdMap) {
		Map<Station, Double> result = new HashMap<>();
		String sql = "SELECT start_station_id, COUNT(*)/(SUM(COUNT(*)) OVER())*100 AS perc " + 
				"FROM rentals " + 
				"WHERE DATE(start_date) >= ? AND DATE(end_date) <= ? " + 
				"GROUP BY start_station_id " + 
				"ORDER BY perc DESC";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(startDate));
			st.setDate(2, Date.valueOf(endDate));
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
	 * Permette di ottenere la percentuale di noleggi per ogni fascia oraria, considerando i soli noleggi relativi all'intervallo di tempo passato.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @return La {@link Map mappa} con la {@link LocalDateTime istante di tempo} come chiave e la percentuale come valore.
	 */
	public static Map<LocalDateTime, Double> percentageTimePeriod(LocalDate startDate, LocalDate endDate) {
		Map<LocalDateTime, Double> result = new HashMap<>();
		String sql = "SELECT start_date, COUNT(*)/(SUM(COUNT(*)) OVER())*100 AS perc " + 
				"FROM rentals " + 
				"WHERE DATE(start_date) >= ? AND DATE(end_date) <= ? " + 
				"GROUP BY start_date " + 
				"ORDER BY perc DESC";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(startDate));
			st.setDate(2, Date.valueOf(endDate));
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
	 * Permette di ottenere la durata massima e minima del tragitto tra ogni coppia di {@link Station stazioni}, considerando i soli noleggi relativi all'intervallo di tempo passato.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @param stationsIdMap idMap relativa alle stazioni di interesse
	 * @return La {@link List lista} di {@link Route itinerari}
	 */
	public static List<Route> getAllRoutePeriod(LocalDate startDate, LocalDate endDate, Map<Integer, Station> stationsIdMap) {
		String sql = "SELECT start_station_id, end_station_id, MIN(duration) AS min, MAX(duration) AS max " + 
				"FROM rentals " + 
				"WHERE DATE(start_date) >= ? AND DATE(end_date) <= ? " + 
				"GROUP BY start_station_id, end_station_id " + 
				"ORDER BY start_station_id";
		List<Route> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(startDate));
			st.setDate(2, Date.valueOf(endDate));
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer startId = res.getInt("start_station_id");
				Integer endId = res.getInt("end_station_id");
				
				if(stationsIdMap.containsKey(startId) && stationsIdMap.containsKey(endId)) {
					Route route = new Route(stationsIdMap.get(startId), stationsIdMap.get(endId), Duration.of(res.getLong("min"), ChronoUnit.SECONDS), Duration.of(res.getLong("max"), ChronoUnit.SECONDS));
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
	 * Permette di ottenere la percentuale dei noleggi relativi alle diversi {@link Station stazioni} di arrivo rispetto a tutte le stazioni di partenza, considerando i soli noleggi relativi all'intervallo di tempo passato.
	 * @param startDate Data iniziale
	 * @param endDate Data finale
	 * @param stationsIdMap idMap relativa alle stazioni di interesse
	 * @return La {@link Map mappa} con l'ID della {@link Station stazione} di partenza come chiave e come valore una mappa con chiave l'ID della stazioni di arrivo e valore la relativa percentuale.
	 */
	public static Map<Integer, Map<Integer, Double>> percentageEndStationsPeriod(LocalDate startDate, LocalDate endDate, Map<Integer, Station> stationsIdMap) {
		String sql = "SELECT start_station_id, end_station_id, COUNT(*)/(SUM(COUNT(*)) OVER (PARTITION BY start_station_id))*100 AS perc " + 
				"FROM rentals " +
				"WHERE DATE(start_date) >= ? AND DATE(end_date) <= ? " +
				"GROUP BY start_station_id, end_station_id " + 
				"ORDER BY start_station_id";
		Map<Integer, Map<Integer, Double>> result = new HashMap<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(startDate));
			st.setDate(2, Date.valueOf(endDate));
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
