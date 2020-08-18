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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
    private Label lblResult;
    
    @FXML
    private GridPane gridResult;
    
    @FXML
    private Label lblDateSimulation;

    @FXML
    private Label lblVariation;

    @FXML
    private Label lblBikes;

    @FXML
    private Label lblDateData;

    @FXML
    private Label lblNewStation;

    @FXML
    private Label lblRedistribution;

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
    	ChangePage.goToRentalsData(this.stage, this.model);
    }

    @FXML
    void goToSimulation(ActionEvent event) throws Exception {
    	ChangePage.goToSimulation(this.stage, this.model);
    }

    @FXML
    void goToStationsData(ActionEvent event) throws Exception {
    	ChangePage.goToStationsData(this.stage, this.model);
    }

    @FXML
    void initialize() {
    	assert btnStationsData != null : "fx:id=\"btnStationsData\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnRentalsData != null : "fx:id=\"btnRentalsData\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblResult != null : "fx:id=\"lblResult\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert gridResult != null : "fx:id=\"gridResult\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert btnShowMap != null : "fx:id=\"btnShowMap\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblDateSimulation != null : "fx:id=\"lblDateSimulation\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblVariation != null : "fx:id=\"lblVariation\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblBikes != null : "fx:id=\"lblBikes\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblDateData != null : "fx:id=\"lblDateData\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblNewStation != null : "fx:id=\"lblNewStation\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblRedistribution != null : "fx:id=\"lblRedistribution\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblCompleted != null : "fx:id=\"lblCompleted\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblEmpty != null : "fx:id=\"lblEmpty\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblCanceled != null : "fx:id=\"lblCanceled\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert lblFull != null : "fx:id=\"lblFull\" was not injected: check your FXML file 'ResultScene.fxml'.";
        assert tableStationsResult != null : "fx:id=\"tableStationsResult\" was not injected: check your FXML file 'ResultScene.fxml'.";

        
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.noSimulationResult();
    	
    	this.lblCompleted.setText("Noleggi completati:");
    	this.lblCanceled.setText("Noleggi cancellati:");
    	this.lblEmpty.setText("Tentativi di noleggio falliti (stazione vuota):");
    	this.lblFull.setText("Tentativi di riconsegna falliti (stazione piena):");
    	
    	
    	if(!this.noSimulationResult()) {
    		this.lblResult.setText("Simulazione effettuata con successo!");
    		
    		this.lblDateSimulation.setText("Intervallo di tempo simulato: "+this.model.getStartDatePrint()+" - "+this.model.getEndDatePrint());
    		this.lblDateData.setText("Intervallo di tempo dei dati: "+this.model.getStartDate()+" - "+this.model.getEndDate());
    		
    		this.lblVariation.setText(String.format("Variazione del traffico nel sistema: %.1f%%", this.model.getVariation()));
    		this.lblNewStation.setText(String.format("Tendenza degli utenti a cercare altre stazioni: %.1f%%", this.model.getProbabilityNewStation()));
    		
    		this.lblBikes.setText("Numero di bici inserite nel sistema: "+this.model.getNumBikes());
    		String txtRedistribution = "Algoritmo di redistribuzione notturna: ";
    		switch(this.model.getRedistributionType()) {
				case NESSUNO:
					txtRedistribution += "NESSUNO";
					break;
				case UNIFORME:
					txtRedistribution += "DISTRIBUZIONE UNIFORME";
					break;
				case VERSO_CENTRO:
					txtRedistribution += "DALLA PERIFERIA VERSO IL CENTRO";
					break;
				case VERSO_PERIFERIA:
					txtRedistribution += "DAL CENTRO VERSO LA PERIFERIA";
					break;
    		}
    		this.lblRedistribution.setText(txtRedistribution);
    		
    		this.lblCompleted.setText(this.lblCompleted.getText()+" "+this.model.getNumCompletedRent());
        	this.lblCanceled.setText(this.lblCanceled.getText()+" "+this.model.getNumCanceledRent());
        	this.lblEmpty.setText(this.lblEmpty.getText()+" "+this.model.getNumEmptyRent());
        	this.lblFull.setText(this.lblFull.getText()+" "+this.model.getNumFullRent());
        	
        	
        	
        	Map<Integer, Station> stations = this.model.getStationsResult();
        	List<Station> list = new ArrayList<>(stations.values());
        	list.sort(null);

        	
        	TableColumn<Station, Integer> idColumn = new TableColumn<>("ID");
        	idColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("id"));
        	idColumn.setPrefWidth(40);
        	
        	TableColumn<Station, String> nameColumn = new TableColumn<>("Nome");
        	nameColumn.setCellValueFactory(new PropertyValueFactory<Station, String>("commonName"));
        	nameColumn.setPrefWidth(160);
        	
        	TableColumn<Station, String> problemColumn = new TableColumn<>("Problema riscontrato");
        	problemColumn.setCellValueFactory(new PropertyValueFactory<Station, String>("problemString"));
        	problemColumn.setPrefWidth(150);
        	
        	TableColumn<Station, Integer> completedColumn = new TableColumn<>("Noleggi completati");
        	completedColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numCompletedRent"));
        	completedColumn.setPrefWidth(130);
        	
        	TableColumn<Station, Integer> canceledColumn = new TableColumn<>("Noleggi cancellati");
        	canceledColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numCanceledRent"));
        	canceledColumn.setPrefWidth(130);
        	
        	TableColumn<Station, Integer> emptyColumn = new TableColumn<>("Noleggi falliti");
        	emptyColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numEmptyStationRent"));
        	emptyColumn.setPrefWidth(100);
        	
        	TableColumn<Station, Integer> fullColumn = new TableColumn<>("Riconsegne fallite");
        	fullColumn.setCellValueFactory(new PropertyValueFactory<Station, Integer>("numFullStationRent"));
        	fullColumn.setPrefWidth(120);
        	
        	
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
    		this.gridResult.setVisible(false);
    		this.gridResult.setManaged(false);
    		this.lblResult.setText("Non sono disponibili risultati, si prega di effettuare una simulazione.");
    		return true;
    	} else {
    		this.btnShowMap.setDisable(false);
    		return false;
    	}
    }
}
