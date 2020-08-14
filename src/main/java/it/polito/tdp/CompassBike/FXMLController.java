package it.polito.tdp.CompassBike;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnDati;

    @FXML
    private JFXButton btnSimulazione;

    @FXML
    private JFXButton btnRisultati;

    @FXML
    private Label lblNumStazioni;

    @FXML
    private JFXButton btnFileStazioni;

    @FXML
    private Label lblResultFileStazioni;
    
    @FXML
    private HBox boxBtnAddStazione;

    @FXML
    private JFXButton btnShowGridAddStazione;

    @FXML
    private Label lblResultAddStazione;

    @FXML
    private JFXButton btnAggiungiStazione;

    @FXML
    private JFXButton btnPulisciStazione;

    @FXML
    private GridPane gridAddStazione;

    @FXML
    private Label lblErroreAddStazione;

    @FXML
    private JFXButton btnFileNoleggi;

    @FXML
    private Label lblResultFileNoleggi;

    @FXML
    private TableView<?> tableNoleggi;

    @FXML
    void doAggiungiStazione(ActionEvent event) {

    }

    @FXML
    void doCaricaNoleggi(ActionEvent event) {

    }

    @FXML
    void doCaricaStazioni(ActionEvent event) {

    }

    @FXML
    void doPulisciGridStazione(ActionEvent event) {

    }

    @FXML
    void goToRisultati(ActionEvent event) {

    }

    @FXML
    void goToSimulazione(ActionEvent event) {

    }

    @FXML
    void showGridAddStazione(ActionEvent event) {
    	this.gridAddStazione.setManaged(true);
    	this.gridAddStazione.setVisible(true);
    	
    	this.boxBtnAddStazione.setVisible(true);
    }

    @FXML
    void initialize() {
        assert btnDati != null : "fx:id=\"btnDati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRisultati != null : "fx:id=\"btnRisultati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblNumStazioni != null : "fx:id=\"lblNumStazioni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnFileStazioni != null : "fx:id=\"btnFileStazioni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblResultFileStazioni != null : "fx:id=\"lblResultFileStazioni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnShowGridAddStazione != null : "fx:id=\"btnShowGridAddStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblResultAddStazione != null : "fx:id=\"lblResultAddStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxBtnAddStazione != null : "fx:id=\"boxBtnAddStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAggiungiStazione != null : "fx:id=\"btnAggiungiStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPulisciStazione != null : "fx:id=\"btnPulisciStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert gridAddStazione != null : "fx:id=\"gridAddStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblErroreAddStazione != null : "fx:id=\"lblErroreAddStazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnFileNoleggi != null : "fx:id=\"btnFileNoleggi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblResultFileNoleggi != null : "fx:id=\"lblResultFileNoleggi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tableNoleggi != null : "fx:id=\"tableNoleggi\" was not injected: check your FXML file 'Scene.fxml'.";
        gridAddStazione.setManaged(false);

    }
}
