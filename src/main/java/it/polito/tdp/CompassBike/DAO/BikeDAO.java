package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.polito.tdp.CompassBike.model.Rental;

public class BikeDAO {
	
	public static void addBike(Rental rental) {
		String sql = "INSERT INTO bike VALUES(?)";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, rental.getBikeId());
			
			st.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
