package org.example;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //template fixe
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();


        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/AjouterFormation.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandeConge/ListeDemandeConge.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);


        primaryStage.setScene(scene);
        primaryStage.setWidth(1300);
        primaryStage.setHeight(700);
        primaryStage.show();
    }
}
