package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleButton;

import it.polito.tdp.CompassBike.model.Model;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SimulationController {
	
	private Model model;
	private Stage stage;
	
	private Integer numBikesDB;
	private Integer numMaxBikes;
	
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	private LocalDate startDateData;
	private LocalDate endDateData;
	
	private Double variation;
	private Double probability;
	private Integer numBikes;
	
	private final Double DEFAULT_VARIATION = 0.0;
	private final Double DEFAULT_PROBABILITY = 60.0;
	
	
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
    private GridPane gridDateData;

	@FXML
	private DatePicker dateStartSimulation;

	@FXML
	private DatePicker dateEndSimulation;

	@FXML
	private JFXRadioButton radioMonth;

	@FXML
	private ToggleGroup radioDateData;

	@FXML
	private JFXRadioButton radioYear;

	@FXML
	private JFXRadioButton radioManual;

	@FXML
	private HBox hBoxManual;

	@FXML
	private DatePicker dateStartData;

	@FXML
	private DatePicker dateEndData;

	@FXML
	private Label lblErrorData;

	@FXML
	private TextField txtVariation;

	@FXML
	private TextField txtProbabilityNewStation;

	@FXML
	private Label lblErrorParameters;

	@FXML
	private JFXToggleButton toggleRedistribution;

	@FXML
	private TextField txtNumBikes;

	@FXML
	private Label lblErrorBike;

	@FXML
	private Label lblMaxBikes;

	@FXML
	private JFXButton btnClear;

	@FXML
	private JFXButton btnPlaySimulation;
	
	@FXML
	private Label lblIncompleteData;
	

    @FXML
    void doClear(ActionEvent event) {    	
    	this.dateStartSimulation.setValue(null);
    	this.dateEndSimulation.setValue(null);
    	
    	this.clearGridDateData();
    	
    	this.toggleRedistribution.setSelected(false);
    	
    	this.lblIncompleteData.setVisible(false);

    	
    	this.loadNumBikes();
    }

    @FXML
    void doSimulation(ActionEvent event) throws Exception {
    	this.lblIncompleteData.setVisible(false);
    	/*
    	if(!this.checkDateSimulation(event)) {
    		this.lblIncompleteData.setVisible(true);
    		return;
    	}
    	
    	if(!this.checkDateData(event)) {
    		this.lblIncompleteData.setVisible(true);
    		return;
    	}*/
    	
    	if(!this.checkParameters(event)) {
    		this.lblIncompleteData.setVisible(true);
    		return;
    	}
    	
    	if(!this.checkNumBikes(event)) {
    		this.lblIncompleteData.setVisible(true);
    		return;
    	}
    	
    	
    	if(this.startDate == null || this.endDate == null || this.startDateData == null || this.endDateData == null || this.variation == null || this.probability == null || this.numBikes == null) {
    		this.lblIncompleteData.setVisible(true);
    		this.btnPlaySimulation.setDisable(true);
    		return;
    	}
    	
    	
    	model.setParametersSimulation(startDateData, endDateData, variation, startDate, endDate);
	    model.setProbabilityNewStartStation(probability);
	    
	    if(this.toggleRedistribution.isSelected()) {
	    	this.model.setRedistribution(true);
	    } else {
	    	this.model.setRedistribution(false);
	    }
	    
	    this.model.setNumBikes(this.numBikes);
	    
		model.runSimulation();

		
		ChangePage.goToResult(this.stage, this.model);
    }
    
    
    @FXML
    void checkDateSimulation(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	this.gridDateData.setDisable(true);
    	this.lblErrorSimulator.setText("");
    	
    	
    	this.startDate = this.dateStartSimulation.getValue();
    	this.endDate = this.dateEndSimulation.getValue();
    	
    	if(this.startDate != null && this.endDate != null) {
    		if(this.startDateData != null || this.endDateData != null) {
    			this.clearGridDateData();
    		}
    		
    		if(this.startDate.isAfter(this.endDate)) {
    			this.lblErrorSimulator.setText("La data di inizio non può essere successiva a quella di fine.");
    			this.btnPlaySimulation.setDisable(true);
    			return;
    		} else {
    			this.gridDateData.setDisable(false);
    			return;
    		}
    	} else {
    		this.btnPlaySimulation.setDisable(true);
    		return;
    	}
    }
    
    
    @FXML
    void showHBoxData(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	this.lblErrorData.setText("");
    	
    	this.hBoxManual.setVisible(true);
    	this.btnPlaySimulation.setDisable(true);
    }
    
    
    @FXML
    void hideHBoxDataMonth(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	this.lblErrorData.setText("");
    	
    	this.hBoxManual.setVisible(false);
    	this.dateStartData.setValue(null);
    	this.dateEndData.setValue(null);
    	

    	this.startDateData = startDate.minus(1, ChronoUnit.MONTHS);
		this.endDateData = endDate.minus(1, ChronoUnit.MONTHS);
		
		if(!this.model.isDataAvaiable(startDateData, endDateData)) {
			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
			this.btnPlaySimulation.setDisable(true);
		}
    }
    
    
    @FXML
    void hideHBoxDataYear(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	this.lblErrorData.setText("");
    	
    	this.hBoxManual.setVisible(false);
    	this.dateStartData.setValue(null);
    	this.dateEndData.setValue(null);
    	
    	
    	this.startDateData = startDate.minus(1, ChronoUnit.YEARS);
		this.endDateData = endDate.minus(1, ChronoUnit.YEARS);
		
		if(!this.model.isDataAvaiable(startDateData, endDateData)) {
			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
			this.btnPlaySimulation.setDisable(true);
		}
    }
    
    
    @FXML
    void checkDateData(ActionEvent event) {
    	this.btnPlaySimulation.setDisable(false);
    	this.lblErrorData.setText("");

    	
    	this.startDateData = this.dateStartData.getValue();
    	this.endDateData = this.dateEndData.getValue();
    		
    	if(startDateData != null && endDateData != null) {
    		if(startDateData.isAfter(endDateData)) {
    			this.lblErrorData.setText("La data di inizio non può essere successiva a quella di fine.");
    			this.btnPlaySimulation.setDisable(true);
    			return;
    		}
    		
    		if(ChronoUnit.DAYS.between(startDate, endDate) != ChronoUnit.DAYS.between(startDateData, endDateData)) {
    			this.lblErrorData.setText("L'ampiezza dei due intervalli deve essere uguale.");
    			this.btnPlaySimulation.setDisable(true);
    			return;
    		}
    		
    		if(!this.model.isDataAvaiable(startDateData, endDateData)) {
    			this.lblErrorData.setText("I dati richiesti non sono disponibili, nella pagina 'Dati noleggi' sono riportati gli intervalli di tempo di cui sono disponibili i dati.");
    			this.btnPlaySimulation.setDisable(true);
    			return;
    		}
    		
		} else {
			this.btnPlaySimulation.setDisable(true);
			return;
		}
			
    }
    
    
    @FXML
    boolean checkParameters(ActionEvent event) {
    	this.lblErrorParameters.setText("");
    	
    	String var = this.txtVariation.getText();
    	this.variation = null;
    	try {
    		this.variation = Double.parseDouble(var);
    	} catch(NumberFormatException e) {
    		this.lblErrorParameters.setText("La variazione deve essere un numero maggiore di 0.0%.");
    		return false;
    	}
    	
    	if(variation < 0.0) {
    		this.lblErrorParameters.setText("La variazione deve essere un numero maggiore di 0.0%.");
    		return false;
    	}
    	
    	
    	String prob = this.txtProbabilityNewStation.getText();
    	this.probability = null;
    	try {
    		this.probability = Double.parseDouble(prob);
    	} catch(NumberFormatException e) {
    		this.lblErrorParameters.setText("La tendenza degli utenti a cercare un'altra stazione deve essere un numero compreso tra 0.0% e 100.0%.");
    		return false;
    	}
    	
    	if(probability < 0.0 || probability > 100.0) {
    		this.lblErrorParameters.setText("La tendenza degli utenti a cercare un'altra stazione deve essere un numero compreso tra 0.0% e 100.0%.");
    		return false;
    	}
    	
    	return true;
    }
    

    @FXML
    boolean checkNumBikes(ActionEvent event) {
    	this.lblErrorBike.setText("");
    	
    	String bikes = this.txtNumBikes.getText();
    	this.numBikes = null;
    	try {
    		this.numBikes = Integer.parseInt(bikes);
    	} catch(NumberFormatException e) {
    		this.lblErrorBike.setText("Il numero di biciclette da utilizzare deve essere un numero intero!");
    		return false;
    	}
    	
    	if(numBikes <= 0) {
    		this.lblErrorBike.setText("Il numero di biciclette da utilizzare deve essere un numero intero positivo e maggiore di 0!");
    		return false;
    	}
    	
    	if(numBikes > this.numMaxBikes) {
    		this.lblErrorBike.setText("Il numero inserito è maggiore del numero massimo di biciclette utilizzabili!");
    		return false;
    	}
    	
    	return true;
    }

    @FXML
    void goToRentalsData(ActionEvent event) throws Exception {
    	ChangePage.goToRentalsData(this.stage, this.model);
    }

    @FXML
    void goToResult(ActionEvent event) throws Exception {
    	ChangePage.goToResult(this.stage, this.model);
    }

    @FXML
    void goToStationsData(ActionEvent event) throws Exception {
    	ChangePage.goToStationsData(this.stage, this.model);
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
        assert gridDateData != null : "fx:id=\"gridDateData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioMonth != null : "fx:id=\"radioMonth\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioDateData != null : "fx:id=\"radioDateData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioYear != null : "fx:id=\"radioYear\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert radioManual != null : "fx:id=\"radioManual\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert hBoxManual != null : "fx:id=\"hBoxManual\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateStartData != null : "fx:id=\"dateStartData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert dateEndData != null : "fx:id=\"dateEndData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorData != null : "fx:id=\"lblErrorData\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert txtVariation != null : "fx:id=\"txtVariation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert txtProbabilityNewStation != null : "fx:id=\"txtProbabilityNewStation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorParameters != null : "fx:id=\"lblErrorParameters\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert toggleRedistribution != null : "fx:id=\"toggleRedistribution\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert txtNumBikes != null : "fx:id=\"txtNumBikes\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblErrorBike != null : "fx:id=\"lblErrorBike\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblMaxBikes != null : "fx:id=\"lblMaxBikes\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert btnPlaySimulation != null : "fx:id=\"btnPlaySimulation\" was not injected: check your FXML file 'SimulationScene.fxml'.";
        assert lblIncompleteData != null : "fx:id=\"lblIncompleteData\" was not injected: check your FXML file 'SimulationScene.fxml'.";

        this.hBoxManual.setVisible(false);
        
        this.gridDateData.setDisable(true);
        
        this.dateStartSimulation.getEditor().setDisable(true);
        this.dateEndSimulation.getEditor().setDisable(true);
        this.dateStartData.getEditor().setDisable(true);
        this.dateEndData.getEditor().setDisable(true);
        
        this.txtVariation.setText(this.DEFAULT_VARIATION.toString());
        this.txtProbabilityNewStation.setText(this.DEFAULT_PROBABILITY.toString());
        
        this.lblIncompleteData.setVisible(false);
        
        this.btnPlaySimulation.setDisable(true);
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.model.setNumBikesDB();
    	
    	this.numBikesDB = this.model.getNumBikesDB();
    	this.loadNumBikes();
    	
    	this.numMaxBikes = this.model.getNumDocks();
    	this.lblMaxBikes.setText("Il numero massimo di biciclette utilizzabili è "+this.numMaxBikes+" (pari al numero di colonnine).");
    	
    	this.numBikes = this.numBikesDB;
    	this.variation = this.DEFAULT_VARIATION;
    	this.probability = this.DEFAULT_PROBABILITY;
    }
    
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    
    private void loadNumBikes() {
    	this.txtNumBikes.setText(this.numBikesDB.toString());
    }
    
    
    private void clearGridDateData() {
    	if(this.radioDateData.getSelectedToggle() != null)
    		this.radioDateData.getSelectedToggle().setSelected(false);
    	
    	this.lblErrorData.setText("");
    	
    	this.hBoxManual.setVisible(false);
    	
    	this.dateStartData.setValue(null);
    	this.dateEndData.setValue(null);
    	
    	this.startDateData = null;
    	this.endDateData = null;
    }
    
}
