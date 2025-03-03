package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.RoleController;

public class FxmlMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Charger la page du contrôleur de rôle
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/role.fxml"));
        Parent parent = loader.load();

        RoleController roleController = loader.getController();
        roleController.setStage(stage); // Passer le stage au contrôleur

        Scene scene = new Scene(parent);
        stage.setTitle("Détermination du rôle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void afficherOffresCandidat() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Offres d'emploi pour les candidats");
        stage.show();
    }
}