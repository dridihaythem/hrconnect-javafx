package com.melocode.semin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Correct FXML path based on your directory structure
            URL fxmlLocation = getClass().getResource("/com/melocode/semin/views/SeminaireView.fxml");
            System.out.println("FXML Location: " + fxmlLocation);

            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found at /com/melocode/semin/views/SeminaireView.fxml");
            }

            // Load the FXML
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion des Séminaires");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface utilisateur.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
