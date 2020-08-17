package it.polito.tdp.CompassBike;

import it.polito.tdp.CompassBike.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe di utilità che permette il funzionamento del menù dell'interfaccia grafica.
 * @author Umberto Ferrari
 *
 */
public class ChangePage {
	
	protected static void goToStationsData(Stage stage, Model model) throws Exception {
		FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource("/fxml/StationsDataImport.fxml"));
        BorderPane root = loader.load();
		
        StationsDataController controller = loader.getController();
		
		controller.setModel(model);
		controller.setStage(stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Dati stazioni");
        stage.setScene(scene);
        stage.show();
	}
	
	
	protected static void goToRentalsData(Stage stage, Model model) throws Exception {
		FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource("/fxml/RentalsDataImport.fxml"));
        BorderPane root = loader.load();
		
        RentalsDataController controller = loader.getController();
		
		controller.setModel(model);
		controller.setStage(stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Dati noleggi");
        stage.setScene(scene);
        stage.show();
	}
	
	
	protected static void goToSimulation(Stage stage, Model model) throws Exception {
		FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource("/fxml/SimulationScene.fxml"));
        BorderPane root = loader.load();
		
        SimulationController controller = loader.getController();
		
		controller.setModel(model);
		controller.setStage(stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Simulazione");
        stage.setScene(scene);
        stage.show();
	}
	
	
	protected static void goToResult(Stage stage, Model model) throws Exception {
		FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource("/fxml/ResultScene.fxml"));
        BorderPane root = loader.load();
		
        ResultController controller = loader.getController();
		
		controller.setModel(model);
		controller.setStage(stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Compass Bike - Risultati");
        stage.setScene(scene);
        stage.show();
	}
	
	
	protected static Stage loadingScreen() throws Exception {
		FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource("/fxml/LoadingScene.fxml"));
        BorderPane root = loader.load();
        
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		
		Stage stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
        
        stage.setScene(scene);
        stage.show();
        stage.toFront();
        
        return stage;
	}
	
	
	protected static void closeLoadingScreen(Stage stage) {
		if(stage != null)
			stage.close();
	}
	
}
