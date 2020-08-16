package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import it.polito.tdp.CompassBike.DAO.BikesDAO;
import it.polito.tdp.CompassBike.DAO.RentalsDAO;
import it.polito.tdp.CompassBike.model.GroupRentals;
import it.polito.tdp.CompassBike.model.Model;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SimulationController {
	
	// TODO Da fare molto meglio, ci sono molte parti di codice copiate più volte
	
	// TODO Spostare il codice di Group Rentals nel model, anche per Result
	
	// TODO Da mettere il settaggio dei parametri numerici
	
	private Model model;
	private Stage stage;
	
	private List<GroupRentals> groups;
	
	private Integer numMaxBikes;

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
    private Label lblErrorSimulator;

    @FXML
    private DatePicker dateStartSimulation;

    @FXML
    private DatePicker dateEndSimulation;
    
    @FXML
    private JFXRadioButton radioMonth;

    @FXML
    private JFXRadioButton radioYear;

    @FXML
    private JFXRadioButton radioManual;

    @FXML
    private ToggleGroup radioDateData;

    @FXML
    private HBox hBoxManual;

    @FXML
    private DatePicker dateStartData;

    @FXML
    private DatePicker dateEndData;

    @FXML
    private Label lblErrorData;

    @FXML
    private TextField txtNumBikes;

    @FXML
    private Label lblErrorBike;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnPlaySimulation;
    
    @FXML
    private Label lblMaxBikes;
    
    
    @FXML
    void doClear(ActionEvent event) {
    	this.dateStartSimulation.getEditor().clear();
    	this.dateStartSimulation.setValue(null);
    	this.dateEndSimulation.getEditor().clear();
    	this.dateEndSimulation.setValue(null);
    	
    	if(this.radioDateData.getSelectedToggle() != null)
    		this.radioDateData.getSelectedToggle().setSelected(false);
    	
    	this.hBoxManual.setVisible(false);
    	
    	this.dateStartData.getEditor().clear();
    	this.dateStartData.setValue(null);
    	this.dateEndData.getEditor().clear();
    	this.dateEndData.setValue(null);
    	
    	this.loadNumBikes();
    }

    @FXML
    void doSimulation(ActionEvent event) {
    	LocalDate startDate = this.dateStartSimulation.getValue();
    	LocalDate endDate = this.dateEndSimulation.getValue();
    	
    	LocalDate startDateData = null;
    	LocalDate endDateData = null;
    	
    	if(this.radioDateData.getSelectedToggle() == null) {
    		this.lblErrorData.setText("Selezionare una alternativa");
    		return;
    	}
    	
    	if(this.radioYear.isSelected()) {
    		startDateData = startDate.minus(1, ChronoUnit.YEARS);
    		endDateData = endDate.minus(1, ChronoUnit.YEARS);
    		
    		if(this.isDataAvaiable(startDateData, endDateData)) {
    			this.model.setParametersSimulation(startDateData, endDateData, 10.0);
    			this.model.runSimulation();
    			try {
					this.goToResult(new ActionEvent());
				} catch (Exception e) {
					e.printStackTrace();
				}
    		} else {
    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
    		}
    	} else if(this.radioMonth.isSelected()) {
    		startDateData = startDate.minus(1, ChronoUnit.MONTHS);
    		endDateData = endDate.minus(1, ChronoUnit.MONTHS);
    		
    		if(this.isDataAvaiable(startDateData, endDateData)) {
    			this.model.setParametersSimulation(startDateData, endDateData, 10.0);
    			this.model.runSimulation();
    			try {
					this.goToResult(new ActionEvent());
				} catch (Exception e) {
					e.printStackTrace();
				}
    		} else {
    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
    		}
    	} else if(this.radioManual.isSelected()) {
    		startDateData = this.dateStartData.getValue();
    		endDateData = this.dateEndData.getValue();
    		
    		if(startDateData != null && endDateData != null) {
	    		if(startDateData.isAfter(endDateData)) {
	    			this.lblErrorData.setText("La data di inizio non può essere successiva a quella di fine.");
	    			return;
	    		}
	    		
	    		if(this.isDataAvaiable(startDateData, endDateData)) {
	    			this.model.setParametersSimulation(startDateData, endDateData, 10.0);
	    			this.model.runSimulation();
	    			try {
						this.goToResult(new ActionEvent());
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		} else {
	    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
	    		}
    		}
    	}
    }
    
    
    @FXML
    void showHBoxData(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	
    	this.hBoxManual.setVisible(true);
    	
    	this.lblErrorData.setText("");
    }
    
    
    @FXML
    void checkDateData(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	
    	LocalDate startDate = this.dateStartSimulation.getValue();
    	LocalDate endDate = this.dateEndSimulation.getValue();
    	if(startDate != null && endDate != null) {
    		LocalDate startDateData = this.dateStartData.getValue();
    		LocalDate endDateData = this.dateEndData.getValue();
    		
    		if(startDateData != null && endDateData != null) {
	    		if(startDateData.isAfter(endDateData)) {
	    			this.lblErrorData.setText("La data di inizio non può essere successiva a quella di fine.");
	    			this.btnPlaySimulation.setDisable(true);
	    			return;
	    		}
	    		
	    		if(!this.isDataAvaiable(startDateData, endDateData)) {
	    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
	    			this.btnPlaySimulation.setDisable(true);
	    		}
    		}
    	}
    }
    
    
    @FXML
    void hideHBoxDataMonth(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	
    	this.hBoxManual.setVisible(false);
    	this.dateStartData.getEditor().clear();
    	this.dateStartData.setValue(null);
    	this.dateEndData.getEditor().clear();
    	this.dateEndData.setValue(null);
    	
    	this.lblErrorData.setText("");
    	
    	LocalDate startDate = this.dateStartSimulation.getValue();
    	LocalDate endDate = this.dateEndSimulation.getValue();
    	if(startDate != null && endDate != null) {
    		LocalDate startDateData = startDate.minus(1, ChronoUnit.MONTHS);
    		LocalDate endDateData = endDate.minus(1, ChronoUnit.MONTHS);
    		
    		if(!this.isDataAvaiable(startDateData, endDateData)) {
    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
    			this.btnPlaySimulation.setDisable(true);
    		}
    	}
    }
    
    
    @FXML
    void hideHBoxDataYear(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	
    	this.hBoxManual.setVisible(false);
    	
    	this.lblErrorData.setText("");
    	
    	LocalDate startDate = this.dateStartSimulation.getValue();
    	LocalDate endDate = this.dateEndSimulation.getValue();
    	if(startDate != null && endDate != null) {
    		LocalDate startDateData = startDate.minus(1, ChronoUnit.YEARS);
    		LocalDate endDateData = endDate.minus(1, ChronoUnit.YEARS);
    		
    		if(!this.isDataAvaiable(startDateData, endDateData)) {
    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
    			this.btnPlaySimulation.setDisable(true);
    		}
    	}
    }
    
    
    @FXML
    void checkDateSimulation(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	
    	LocalDate startDate = this.dateStartSimulation.getValue();
    	LocalDate endDate = this.dateEndSimulation.getValue();
    	if(startDate != null && endDate != null) {
    		if(startDate.isAfter(endDate)) {
    			this.lblErrorSimulator.setText("La data di inizio non può essere successiva a quella di fine.");
    			this.btnPlaySimulation.setDisable(true);
    			return;
    		}
    	}
    }
    

    @FXML
    void checkNumBikes(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(true);
    	
    	String bikes = this.txtNumBikes.getText();
    	Integer numBikes = null;
    	try {
    		numBikes = Integer.parseInt(bikes);
    	} catch(NumberFormatException e) {
    		this.lblErrorBike.setText("Il numero di biciclette da utilizzare deve essere un numero intero!");
    		this.btnPlaySimulation.setDisable(true);
    		return;
    	}
    	
    	if(numBikes <= 0) {
    		this.lblErrorBike.setText("Il numero di biciclette da utilizzare deve essere un numero intero positivo e maggiore di 0!");
    		this.btnPlaySimulation.setDisable(true);
    		return;
    	}
    	
    	if(numBikes > this.numMaxBikes) {
    		this.lblErrorBike.setText("Il numero inserito è maggiore del numero massimo di biciclette utilizzabili!");
    		this.btnPlaySimulation.setDisable(true);
    		return;
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
    	assert btnStationsData != null : "fx:id=\"btnStationsData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnRentalsData != null : "fx:id=\"btnRentalsData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnSimulation != null : "fx:id=\"btnSimulation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnResult != null : "fx:id=\"btnResult\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorSimulator != null : "fx:id=\"lblErrorSimulator\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateStartSimulation != null : "fx:id=\"dateStartSimulation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateEndSimulation != null : "fx:id=\"dateEndSimulation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioMonth != null : "fx:id=\"radioMonth\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioDateData != null : "fx:id=\"radioDateData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioYear != null : "fx:id=\"radioYear\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioManual != null : "fx:id=\"radioManual\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert hBoxManual != null : "fx:id=\"hBoxManual\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateStartData != null : "fx:id=\"dateStartData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateEndData != null : "fx:id=\"dateEndData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorData != null : "fx:id=\"lblErrorData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert txtNumBikes != null : "fx:id=\"txtNumBikes\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorBike != null : "fx:id=\"lblErrorBike\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblMaxBikes != null : "fx:id=\"lblMaxBikes\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnPlaySimulation != null : "fx:id=\"btnPlaySimulation\" was not injected: check your FXML file 'SimulationScene.fxml'.";

    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.loadNumBikes();
    	this.numMaxBikes = BikesDAO.getNumDocks();
    	this.lblMaxBikes.setText("Il numero massimo di biciclette utilizzabili è "+numMaxBikes+" (pari al numero di colonnine).");
    	
    	this.groups = RentalsDAO.getGroupRentals();
    	
    	this.hBoxManual.setVisible(false);
    	
    	
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    
    private void loadNumBikes() {
    	Integer numBikes = BikesDAO.getNumBike();
    	this.txtNumBikes.setText(numBikes.toString());
    }
    
    
    private boolean isDataAvaiable(LocalDate start, LocalDate end) {
    	for(GroupRentals group : this.groups) {
    		LocalDate from = group.getFromDate();
    		LocalDate to = group.getToDate();
    		if((from.isBefore(start) || from.equals(start)) && (from.isBefore(end) || from.equals(end)) && (to.isAfter(start) || to.equals(start)) && (to.isAfter(end) || to.equals(end)))
    			return true;
    	}
    	
    	return false;
    }
}
