package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class MenuController {

    @FXML
    private AnchorPane menu;

    @FXML
    void redirectToGestionDesFormation() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandeConge/ListeDemandeConge.fxml"));
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        menu.getScene().setRoot(root);
    }

    @FXML
    public void redirectToValiderConge(ActionEvent actionEvent) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ValiderConge/ListeValiderConge.fxml"));
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        menu.getScene().setRoot(root);
    }

    @FXML
    public void redirectToListeEmploye(ActionEvent actionEvent) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Employe/ListeEmploye.fxml"));
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        menu.getScene().setRoot (root);
    }
}