package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.polito.tdp.CompassBike.model.Rental;

public class BikeDAO {
	
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

}
