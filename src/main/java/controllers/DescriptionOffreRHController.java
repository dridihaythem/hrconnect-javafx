package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.OffreEmploi;

public class DescriptionOffreRHController {

    @FXML
    private Label titreLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label lieuLabel;

    @FXML
    private Button btnRetour;

    private OffreEmploi offre;

    public void setOffre(OffreEmploi offre) {
        this.offre = offre;
        titreLabel.setText(offre.getTitle());
        descriptionLabel.setText(offre.getDescription());
        lieuLabel.setText("Lieu : " + offre.getLocation());
    }

    @FXML
    void retourListe() {
        try {
            // Charger la page d'affichage des offres
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offres de recrutement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherOffresRecrutement() {
        try {
            // Charger la page d'affichage des offres
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offres de recrutement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deconnexion() {
        // Ne rien faire pour désactiver le bouton
    }
}