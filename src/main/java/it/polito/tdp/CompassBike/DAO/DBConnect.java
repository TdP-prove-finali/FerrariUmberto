package it.polito.tdp.CompassBike.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility class for connecting to the database
 * Uses the HikaryCP Library
 * 
 * @author Umberto
 *
 */
public class DBConnect {
	
	private static final String jdbcURL = "jdbc:mysql://localhost/bike_sharing";
	private static HikariDataSource ds;
	
	/**
	 * Metodo di utilità per la connessione al DB
	 * @return oggetto {Connection} 
	 */
	public static Connection getConnection() {
		
		if (ds == null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			config.setPassword("passwordDB");
			
			// Configurazione MySQL
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			ds = new HikariDataSource(config);
		}
		
		try {
			
			return ds.getConnection();

		} catch (SQLException e) {
			System.err.println("Errore di connessione al DB");
			throw new RuntimeException(e);
		}
	}

}
