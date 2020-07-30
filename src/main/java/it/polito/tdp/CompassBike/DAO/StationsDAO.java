package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.polito.tdp.CompassBike.model.Station;

public class StationsDAO {
	
	public static void addStation(Station station) {
		String sql = "INSERT INTO stations VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE station_id = ?";
		Connection conn = DBConnect.getConnection();
		
	    try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, station.getId());
			st.setInt(15, station.getId());
			
			st.setString(2, station.getCommonName());
			st.setString(3, station.getTerminalName());
			st.setBoolean(4, station.isInstalled());
			st.setBoolean(5, station.isLocked());
			st.setDate(6, Date.valueOf(station.getInstallDate()));
			st.setDate(7, Date.valueOf(station.getRemovalDate()));
			st.setBoolean(8, station.isTemporary());
			st.setInt(9, station.getNumBikes());
			st.setInt(10, station.getNumEmpityDocks());
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

}
