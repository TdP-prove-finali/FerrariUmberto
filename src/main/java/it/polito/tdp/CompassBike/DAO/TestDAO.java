package it.polito.tdp.CompassBike.DAO;

public class TestDAO {

	public static void main(String[] args) {
		Integer numBike = BikesDAO.getNumBike();
		Integer numDocks = BikesDAO.getNumDocks();
		
		System.out.println(numBike);
		System.out.println(numDocks);
	}

}
