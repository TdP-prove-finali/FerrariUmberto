package it.polito.tdp.CompassBike.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.CompassBike.model.Model;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class ChartController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<String, Integer> chartCompleted;

    @FXML
    private LineChart<String, Integer> chartEmpty;

    @FXML
    private LineChart<String, Integer> chartCanceled;

    @FXML
    private LineChart<String, Integer> chartFull;

    @FXML
    void initialize() {
        assert chartCompleted != null : "fx:id=\"chartCompleted\" was not injected: check your FXML file 'ChartScene.fxml'.";
        assert chartEmpty != null : "fx:id=\"chartEmpty\" was not injected: check your FXML file 'ChartScene.fxml'.";
        assert chartCanceled != null : "fx:id=\"chartCompleted\" was not injected: check your FXML file 'ChartScene.fxml'.";
        assert chartFull != null : "fx:id=\"chartFull\" was not injected: check your FXML file 'ChartScene.fxml'.";

    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	
    	this.chartCompleted.getXAxis().setLabel("Giorno simulato");
    	this.chartCompleted.getYAxis().setLabel("Numero");
    	this.chartCompleted.setLegendVisible(false);
    	
    	this.chartCanceled.getXAxis().setLabel("Giorno simulato");
    	this.chartCanceled.getYAxis().setLabel("Numero");
    	this.chartCanceled.setLegendVisible(false);
    	
    	this.chartEmpty.getXAxis().setLabel("Giorno simulato");
    	this.chartEmpty.getYAxis().setLabel("Numero");
    	this.chartEmpty.setLegendVisible(false);
    	
    	this.chartFull.getXAxis().setLabel("Giorno simulato");
    	this.chartFull.getYAxis().setLabel("Numero");
    	this.chartFull.setLegendVisible(false);
    	
    	
    	Map<LocalDate, Integer> mapCompleted = this.model.getNumCompletedRentDay();
    	Map<LocalDate, Integer> mapCanceled = this.model.getNumCanceledRentDay();
    	Map<LocalDate, Integer> mapEmpty = this.model.getNumEmptyRentDay();
    	Map<LocalDate, Integer> mapFull = this.model.getNumFullRentDay();
    	

        XYChart.Series<String, Integer> seriesCompleted = new XYChart.Series<>();
        for(LocalDate date : mapCompleted.keySet()) {
        	String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"));
            XYChart.Data<String, Integer> d = new XYChart.Data<>(formattedDate, mapCompleted.get(date));
            seriesCompleted.getData().add(d);
        }
        this.chartCompleted.getData().add(seriesCompleted);
        
        
        XYChart.Series<String, Integer> seriesCanceled = new XYChart.Series<>();
        for(LocalDate date : mapCanceled.keySet()) {
        	String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"));
        	XYChart.Data<String, Integer> d = new XYChart.Data<>(formattedDate, mapCanceled.get(date));
            seriesCanceled.getData().add(d);
        }
        this.chartCanceled.getData().add(seriesCanceled);
        
        
        XYChart.Series<String, Integer> seriesEmpty = new XYChart.Series<>();
        for(LocalDate date : mapEmpty.keySet()) {
        	String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"));
        	XYChart.Data<String, Integer> d = new XYChart.Data<>(formattedDate, mapEmpty.get(date));
            seriesEmpty.getData().add(d);
        }
        this.chartEmpty.getData().add(seriesEmpty);
        
        
        XYChart.Series<String, Integer> seriesFull = new XYChart.Series<>();
        for(LocalDate date : mapFull.keySet()) {
        	String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"));
        	XYChart.Data<String, Integer> d = new XYChart.Data<>(formattedDate, mapFull.get(date));
            seriesFull.getData().add(d);
        }
        this.chartFull.getData().add(seriesFull);
    }
}
