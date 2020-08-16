package it.polito.tdp.CompassBike.DAO;

public class TestDAO {

	public static void main(String[] args) {
		StationsDAO.isInsideArea(10.0, 10.0);
		Double[] center = StationsDAO.getCenterArea();
    	System.out.println(center[0] + " " + center[1]);
	}

}
