package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;

import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.dataImport.DataImport;
import it.polito.tdp.CompassBike.model.Model;
import it.polito.tdp.CompassBike.model.Station;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StationsDataController {
	
	private Model model;
	private Stage stage;
	
	private boolean gridAddStationIsVisible = false;

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
    private Label lblNumStations;

    @FXML
    private JFXButton btnFileStations;

    @FXML
    private Label lblResultFileStations;

    @FXML
    private JFXButton btnShowGridAddStation;

    @FXML
    private Label lblMessageAddStation;

    @FXML
    private GridPane gridAddStation;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDocks;

    @FXML
    private TextField txtBikes;

    @FXML
    private TextField txtLat;

    @FXML
    private TextField txtLon;

    @FXML
    private Label lblErrorAddStation;

    @FXML
    private JFXButton btnAddStation;

    @FXML
    private JFXButton btnClearGridStation;
    

    
    @FXML
    void doLoadStations(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Importa stazioni");
    	FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extentionFilter);
		
    	Stage newStage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(newStage);
		newStage.close();
		
		if(selectedFile != null) {
			Integer[] res = DataImport.parseJSONStations(selectedFile);
			String textLbl = "";
			if(res[0] != 0) {
				switch(res[0]) {
				case 1:
					textLbl = "File non trovato!";
					break;
				case 2:
					textLbl = "Impossibile leggere il file!";
					break;
				}
			} else {
				Integer correctLine = res[1];
				Integer errorLine = res[2];
				Double percentage = ((double) correctLine) / (correctLine + errorLine) * 100.0;
				textLbl = String.format("E' stato salvato il %.1f%% del file, in quanto %d righe contengono errori di formato.", percentage, errorLine);
			}
			this.lblResultFileStations.setText(textLbl);
		}
			
    }
    
    
    @FXML
    void doAddStation(ActionEvent event) {
    	String sId = txtId.getText();
    	if(sId == null) {
    		this.lblErrorAddStation.setText("Si prega di inserire l'ID della stazione.");
    		return;
    	}
    	Integer id;
    	try {
    		id = Integer.parseInt(sId);
    	} catch(NumberFormatException e) {
    		this.lblErrorAddStation.setText("L'ID deve essere un numero intero!");
    		return;
    	}
    	
    	if(id <= 9000) {
    		this.lblErrorAddStation.setText("Per motivi applicativi le stazioni inserite manualmente dall'utente devono avere un ID maggiore di 9000.\nNon è possibile modificare i parametri di stazioni non inserite manualmente dall'utente.");
    		return;
    	}
    	
    	String name = txtName.getText();
    	if(name.length() <= 0 || name == null) {
    		this.lblErrorAddStation.setText("Inserire un nome!");
    		return;
    	}
    	
    	String docks = txtDocks.getText();
    	if(docks == null) {
    		this.lblErrorAddStation.setText("Si prega di inserire il numero di docks (colonnine) della stazione.");
    		return;
    	}
    	Integer numDocks = null;
    	try {
    		numDocks = Integer.parseInt(docks);
    	} catch(NumberFormatException e) {
    		this.lblErrorAddStation.setText("Il numero di docks deve essere un numero intero!");
    		return;
    	}
    	
    	String bikes = txtBikes.getText();
    	if(bikes == null) {
    		this.lblErrorAddStation.setText("Si prega di inserire il numero di bici della stazione.");
    		return;
    	}
    	Integer numBikes = null;
    	try {
    		numBikes = Integer.parseInt(bikes);
    	} catch(NumberFormatException e) {
    		this.lblErrorAddStation.setText("Il numero di bici deve essere un numero intero!");
    		return;
    	}
    	
    	if(numBikes > numDocks) {
    		this.lblErrorAddStation.setText("Il numero di bici deve essere minore o uguale del numero di docks!");
    		return;
    	}
    	
    	String latitude = txtLat.getText();
    	if(latitude == null) {
    		this.lblErrorAddStation.setText("Si prega di inserire la latitudine della posizione della stazione.");
    		return;
    	}
    	Double lat;
    	try {
    		lat = Double.parseDouble(latitude);
    	} catch(NumberFormatException e) {
    		this.lblErrorAddStation.setText("La latitudine deve essere un numero decimale!");
    		return;
    	}
    	
    	String longitude = txtLon.getText();
    	if(longitude == null) {
    		this.lblErrorAddStation.setText("Si prega di inserire la longitudine della posizione della stazione.");
    		return;
    	}
    	Double lon;
    	try {
    		lon = Double.parseDouble(longitude);
    	} catch(NumberFormatException e) {
    		this.lblErrorAddStation.setText("La longitudine deve essere un numero decimale!");
    		return;
    	}
    	
    	if(!StationsDAO.isInsideArea(lat, lon)) {
    		this.lblErrorAddStation.setText("La nuova stazione deve essere all'interno dell'attuale area operativa del servizio!");
    		return;
    	}
    	
    	Integer numEmpty = numDocks - numBikes;
    	
    	Station station = new Station(id, name, numBikes, numEmpty, numDocks, lat, lon);
    	StationsDAO.addStationUser(station);
    	this.lblErrorAddStation.setText("Stazione inserita correttamente.");
    	this.clearGrid();
    }
    

    @FXML
    void doClearGridAddStation(ActionEvent event) {
    	this.clearGrid();
    	this.lblErrorAddStation.setText("");
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
    void goToResult(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResultScene.fxml"));
        BorderPane root = loader.load();
		
        ResultController controller = loader.getController();
		
		controller.setModel(this.model);
		controller.setStage(this.stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Risultati");
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
    void showGridAddStation(ActionEvent event) {
    	this.gridAddStationIsVisible = !this.gridAddStationIsVisible;
    	
    	this.gridAddStation.setManaged(this.gridAddStationIsVisible);
    	this.gridAddStation.setVisible(this.gridAddStationIsVisible);
    	
    	this.lblMessageAddStation.setVisible(this.gridAddStationIsVisible);
    }

    @FXML
    void initialize() {
    	assert btnStationsData != null : "fx:id=\"btnStationsData\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnRentalsData != null : "fx:id=\"btnRentalsData\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert lblNumStations != null : "fx:id=\"lblNumStations\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnFileStations != null : "fx:id=\"btnFileStations\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert lblResultFileStations != null : "fx:id=\"lblResultFileStations\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnShowGridAddStation != null : "fx:id=\"btnShowGridAddStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert lblMessageAddStation != null : "fx:id=\"lblMessageAddStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert gridAddStation != null : "fx:id=\"gridAddStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtId != null : "fx:id=\"txtId\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtDocks != null : "fx:id=\"txtDocks\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtBikes != null : "fx:id=\"txtBikes\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtLat != null : "fx:id=\"txtLat\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert txtLon != null : "fx:id=\"txtLon\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert lblErrorAddStation != null : "fx:id=\"lblErrorAddStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnAddStation != null : "fx:id=\"btnAddStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        assert btnClearGridStation != null : "fx:id=\"btnClearGridStation\" was not injected: check your FXML file 'StationsDataImport.fxml'.";
        
        this.gridAddStation.setManaged(false);
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	Integer numStations = StationsDAO.getNumStations();
    	this.lblNumStations.setText("Il database contiene "+numStations+" stazioni.");
    	
    	Integer id = StationsDAO.getLastIdUserStation() + 1;
    	this.txtId.setText(id.toString());
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    
    private void clearGrid() {
    	this.txtName.clear();
    	this.txtDocks.clear();
    	this.txtBikes.clear();
    	this.txtLat.clear();
    	this.txtLon.clear();
    }
}
