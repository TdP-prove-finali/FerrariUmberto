package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;

import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.DAO.StationsDAO;
import it.polito.tdp.CompassBike.dataImport.DataImport;
import it.polito.tdp.CompassBike.model.GroupRentals;
import it.polito.tdp.CompassBike.model.Model;
import it.polito.tdp.CompassBike.model.Station;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DataController {
	
	private Model model;
	private boolean gridAddStationIsVisible = false;

	 @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private JFXButton btnData;

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
	    private JFXButton btnFileRentals;

	    @FXML
	    private Label lblResultFileRentals;

	    @FXML
	    private TableView<GroupRentals> tableRentals;


    @FXML
    void doLoadRentals(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Importa noleggi");
    	FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extentionFilter);
		
    	Stage newStage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(newStage);
		newStage.close();
		
		if(selectedFile != null) {
			Integer[] res = DataImport.parseCSVRentals(selectedFile);
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
			this.lblResultFileRentals.setText(textLbl);
		}
			
    }

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
    		this.lblErrorAddStation.setText("Per motivi applicativi le stazioni inserite manualmente dall'utente devono avere un ID maggiore di 9000");
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
    void goToResult(ActionEvent event) {

    }
    

    @FXML
    void goToSimulation(ActionEvent event) {

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
    	assert btnData != null : "fx:id=\"btnData\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert lblNumStations != null : "fx:id=\"lblNumStations\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnFileStations != null : "fx:id=\"btnFileStations\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert lblResultFileStations != null : "fx:id=\"lblResultFileStations\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnShowGridAddStation != null : "fx:id=\"btnShowGridAddStation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert lblMessageAddStation != null : "fx:id=\"lblResultAddStation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert gridAddStation != null : "fx:id=\"gridAddStation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtId != null : "fx:id=\"txtId\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtDocks != null : "fx:id=\"txtDocks\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtBikes != null : "fx:id=\"txtBikes\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtLat != null : "fx:id=\"txtLat\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert txtLon != null : "fx:id=\"txtLon\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert lblErrorAddStation != null : "fx:id=\"lblErroreAddStazione\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnAddStation != null : "fx:id=\"btnAddStation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnClearGridStation != null : "fx:id=\"btnClearGridStation\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert btnFileRentals != null : "fx:id=\"btnFileRentals\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert lblResultFileRentals != null : "fx:id=\"lblResultFileRentals\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        assert tableRentals != null : "fx:id=\"tableRentals\" was not injected: check your FXML file 'DataImportScene.fxml'.";
        
        this.gridAddStation.setManaged(false);
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	Integer numStations = StationsDAO.getNumStations();
    	this.lblNumStations.setText("Il database contiene "+numStations+" stazioni.");
    	
    	List<GroupRentals> groupRentals = RentalsDAO.getGroupRentals();
    	
    	TableColumn<GroupRentals, LocalDate> fromDateColumn = new TableColumn<>("Da");
    	fromDateColumn.setCellValueFactory(new PropertyValueFactory<GroupRentals, LocalDate>("fromDate"));
    	fromDateColumn.setPrefWidth(160);
    	
    	TableColumn<GroupRentals, LocalDate> toDateColumn = new TableColumn<>("A");
    	toDateColumn.setCellValueFactory(new PropertyValueFactory<GroupRentals, LocalDate>("toDate"));
    	toDateColumn.setPrefWidth(160);
    	
    	TableColumn<GroupRentals, Integer> numColumn = new TableColumn<>("Numero noleggi");
    	numColumn.setCellValueFactory(new PropertyValueFactory<GroupRentals, Integer>("numRentals"));
    	numColumn.setPrefWidth(210);
    	
    	this.tableRentals.getColumns().add(fromDateColumn);
    	this.tableRentals.getColumns().add(toDateColumn);
    	this.tableRentals.getColumns().add(numColumn);
    	
    	this.tableRentals.getItems().addAll(groupRentals);
    	
    	Integer id = StationsDAO.getLastIdUserStation() + 1;
    	this.txtId.setText(id.toString());
    }
    
    
    public void clearGrid() {
    	this.txtName.clear();
    	this.txtDocks.clear();
    	this.txtBikes.clear();
    	this.txtLat.clear();
    	this.txtLon.clear();
    }
}
