package it.polito.tdp.CompassBike.model;

import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;

public class Simulator {
	
	// Coda degli eventi
	private Queue<Event> queue;
	
	// Parametri di simulazione
	private RentalsGenerator generator;
	
	// Modello del mondo
	private Graph<Station, RouteEdge> graph;
	
		
	// Valori da calcolare
	
	
	
	/**
	 * Inizializza i parametri di simulazione.
	 */
	public void init() { // TODO Passare periodo di tempo
		this.generator = new RentalsGenerator();
		this.generator.loadParameters(); // TODO Forse è meglio spostare questi due comandi nel model
		
		this.queue = new PriorityQueue<>();
		this.queue.addAll(this.generator.generateEvents());
		
		this.graph = this.generator.getGraph();
		System.out.println("Vertici: "+this.graph.vertexSet().size()+"\nArchi: "+this.graph.edgeSet().size());
	}
	

}
