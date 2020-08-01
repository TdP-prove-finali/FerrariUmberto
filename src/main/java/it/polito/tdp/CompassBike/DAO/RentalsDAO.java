package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import it.polito.tdp.CompassBike.model.Rental;

public class RentalsDAO {

	public static void addRental(Rental rental) {
		System.out.println("QUI");
		String sql = "INSERT INTO rentals VALUES(?, ?, ?, ?, ?, ?, ?)";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, rental.getId());
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

}
