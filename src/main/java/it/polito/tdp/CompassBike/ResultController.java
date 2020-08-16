package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;

import it.polito.tdp.CompassBike.model.Model;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ResultController {
	
	private Model model;
	private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnStationsData;

    @FXML
    private JFXButton btnRentalsData;

    @FXML
    private JFXButton btnSimulation;

    @FXML
    private JFXButton btnResult;

    @FXML
    void goToRentalsData(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RentalsDataImport.fxml"));
        BorderPane root = loader.load();
		
        RentalsDataController controller = loader.getController();
		
		controller.setModel(this.model);
		controller.setStage(this.stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Dati noleggi");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToSimulation(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SimulationScene.fxml"));
        BorderPane root = loader.load();
		
        SimulationController controller = loader.getController();
		
		controller.setModel(this.model);
		controller.setStage(this.stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Simulazione");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToStationsData(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StationsDataImport.fxml"));
        BorderPane root = loader.load();
		
        StationsDataController controller = loader.getController();
		
		controller.setModel(this.model);
		controller.setStage(this.stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Dati stazioni");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        assert btnStationsData != null : "fx:id=\"btnStationsData\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnRentalsData != null : "fx:id=\"btnRentalsData\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'ResultScene.fxml'.";

    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
}
