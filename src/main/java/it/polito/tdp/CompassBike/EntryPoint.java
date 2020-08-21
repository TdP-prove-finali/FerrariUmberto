package it.polito.tdp.CompassBike;

import javafx.application.Application;
import it.polito.tdp.CompassBike.controller.StationsDataController;
import it.polito.tdp.CompassBike.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EntryPoint extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StationsDataImport.fxml"));
        BorderPane root = loader.load();
		
        StationsDataController controller = loader.getController();
		
        Model model = new Model();
		controller.setModel(model);
		controller.setStage(stage);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setResizable(false);
        stage.setTitle("Compass Bike - Dati stazioni");
        stage.getIcons().add(new Image(EntryPoint.class.getResourceAsStream("/images/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
