package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;

import it.polito.tdp.CompassBike.dataImport.DataImport;
import it.polito.tdp.CompassBike.model.GroupRentals;
import it.polito.tdp.CompassBike.model.Model;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RentalsDataController {
	
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
    private JFXButton btnFileRentals;

    @FXML
    private Label lblResultFileRentals;

    @FXML
    private TableView<GroupRentals> tableRentals;
    

    @FXML
    void doLoadRentals(ActionEvent event) {
    	this.lblResultFileRentals.setText("");
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Importa noleggi");
    	FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extentionFilter);
		
		Stage loadingStage = null;
		try {
			loadingStage = ChangePage.loadingScreen();
		} catch (Exception e) {}
		
    	Stage newStage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(newStage);
		newStage.close();

		
		if(selectedFile != null) {
			Integer[] res = DataImport.parseCSVRentals(selectedFile);
			
			ChangePage.closeLoadingScreen(loadingStage);
			
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
				if(errorLine > 0) {
					Double percentage = ((double) correctLine) / (correctLine + errorLine) * 100.0;
					textLbl = String.format("E' stato salvato il %.1f%% del file, in quanto %d righe contenevano errori di formato.", percentage, errorLine);
				} else {
					textLbl = String.format("E' stato salvato il 100%% del file, il file non conteneva errori di formato.");
				}
			}
			this.lblResultFileRentals.setText(textLbl);
			
			this.model.setGroupsRentals();
			this.loadTableGroupRentals();
		} else {
			ChangePage.closeLoadingScreen(loadingStage);
		}
		
    }
    

    @FXML
    void goToResult(ActionEvent event) throws Exception {
    	ChangePage.goToResult(this.stage, this.model);
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
        assert btnStationsData != null : "fx:id=\"btnStationsData\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert btnRentalsData != null : "fx:id=\"btnRentalsData\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert btnFileRentals != null : "fx:id=\"btnFileRentals\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert lblResultFileRentals != null : "fx:id=\"lblResultFileRentals\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";
        assert tableRentals != null : "fx:id=\"tableRentals\" was not injected: check your FXML file 'RentalsDataImport.fxml'.";

    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.loadTableGroupRentals();
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    
    private void loadTableGroupRentals() {
    	this.tableRentals.getColumns().clear();
    	this.tableRentals.getItems().clear();
    	
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
    	
    	this.tableRentals.getItems().addAll(this.model.getGroupsRentals());
    }
}
