package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;

import it.polito.tdp.CompassBike.model.Model;
import it.polito.tdp.CompassBike.model.Station;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Label lblNumRentals;

    @FXML
    private JFXButton btnShowMap;

    @FXML
    private Label lblCompleted;

    @FXML
    private Label lblEmpty;

    @FXML
    private Label lblCanceled;

    @FXML
    private Label lblFull;

    @FXML
    private TableView<Station> tableStationsResult;
    
    
    @FXML
    void doShowMap(ActionEvent event) {
    	File map = this.model.getMapsResult();
    	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
				Desktop.getDesktop().browse(map.toURI());
			} catch (IOException e) {
				System.out.println("Impossibile aprire il Browser per mostrare la mappa!");
			}
        }
    }

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
    	
    	this.noSimulationResult();
    	
    	this.lblCompleted.setText("Noleggi completati:");
    	this.lblCanceled.setText("Noleggi cancellati:");
    	this.lblEmpty.setText("Tentativi di noleggio falliti (stazione vuota):");
    	this.lblFull.setText("Tentativi di riconsegna falliti (stazione piena):");
    	
    	
    	if(!this.noSimulationResult()) {
    		this.lblCompleted.setText(this.lblCompleted.getText()+" "+this.model.getNumCompletedRent());
        	this.lblCanceled.setText(this.lblCanceled.getText()+" "+this.model.getNumCanceledRent());
        	this.lblEmpty.setText(this.lblEmpty.getText()+" "+this.model.getNumEmptyRent());
        	this.lblFull.setText(this.lblFull.getText()+" "+this.model.getNumFullRent());
        	
        	
        	
        	Map<Integer, Station> stations = this.model.getStationsResult();
        	List<Station> list = new ArrayList<>(stations.values());
        	list.sort(null);
        	
        	// TODO Da settare meglio le dimensioni delle colonne
        	
        	TableColumn<Station, Integer> idColumn = new TableColumn<>("ID");
        	idColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("id"));
        	idColumn.setPrefWidth(50);
        	
        	TableColumn<Station, String> nameColumn = new TableColumn<>("Nome");
        	nameColumn.setCellValueFactory(new PropertyValueFactory<Station, String>("commonName"));
        	nameColumn.setPrefWidth(180);
        	
        	TableColumn<Station, String> problemColumn = new TableColumn<>("Problema riscontrato");
        	problemColumn.setCellValueFactory(new PropertyValueFactory<Station, String>("problemString"));
        	problemColumn.setPrefWidth(150);
        	
        	TableColumn<Station, Integer> completedColumn = new TableColumn<>("Numero noleggi completati");
        	completedColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numCompletedRent"));
        	completedColumn.setPrefWidth(100);
        	
        	TableColumn<Station, Integer> canceledColumn = new TableColumn<>("Numero noleggi cancellati");
        	canceledColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numCanceledRent"));
        	canceledColumn.setPrefWidth(100);
        	
        	TableColumn<Station, Integer> emptyColumn = new TableColumn<>("Tentativi di noleggio falliti");
        	emptyColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numEmptyStationRent"));
        	emptyColumn.setPrefWidth(100);
        	
        	TableColumn<Station, Integer> fullColumn = new TableColumn<>("Tentativi di riconsegna falliti");
        	fullColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numFullStationRent"));
        	fullColumn.setPrefWidth(100);
        	
        	
        	this.tableStationsResult.getColumns().add(idColumn);
        	this.tableStationsResult.getColumns().add(nameColumn);
        	this.tableStationsResult.getColumns().add(problemColumn);
        	this.tableStationsResult.getColumns().add(completedColumn);
        	this.tableStationsResult.getColumns().add(canceledColumn);
        	this.tableStationsResult.getColumns().add(emptyColumn);
        	this.tableStationsResult.getColumns().add(fullColumn);
        	
        	
        	this.tableStationsResult.getItems().addAll(list);
    	}
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    
    private boolean noSimulationResult() {
    	if(this.model.getCompletedRent() == null || this.model.getCanceledRent() == null) {
    		this.btnShowMap.setDisable(true);
    		this.lblNumRentals.setText("Non sono disponibili risultati, si prega di effettuare una simulazione.");
    		return true;
    	} else {
    		this.btnShowMap.setDisable(false);
    		return false;
    	}
    }
}
