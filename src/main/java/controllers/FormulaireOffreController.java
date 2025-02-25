package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.OffreEmploi;
import services.OffreEmploiService;

public class FormulaireOffreController {

    @FXML
    private Button btnsave;

    @FXML
    private Button btncancel;

    @FXML
    private TextField ttitre;

    @FXML
    private TextField tdescription;

    @FXML
    private TextField tlieu;

    private OffreEmploiService offreService = new OffreEmploiService();
    private OffreEmploi offreToUpdate; // Offre à mettre à jour

    @FXML
    void initialize() {
        // Ajouter des écouteurs d'événements pour valider les champs de texte
        ttitre.textProperty().addListener((observable, oldValue, newValue) -> validateTextField(ttitre, newValue));
        tdescription.textProperty().addListener((observable, oldValue, newValue) -> validateTextField(tdescription, newValue));
        tlieu.textProperty().addListener((observable, oldValue, newValue) -> validateTextField(tlieu, newValue));
    }

    private void validateTextField(TextField textField, String newValue) {
        if (!newValue.matches("[a-zA-Z\\s]*")) {
            textField.setStyle("-fx-border-color: red;");
            showAlert("Erreur de saisie", "Seuls les caractères alphabétiques sont autorisés.");
        } else {
            textField.setStyle(null);
        }
    }

    @FXML
    void saveoffre() {
        try {
            // Vérifier si on est en mode mise à jour ou création
            if (offreToUpdate == null) {
                // Mode création : créer une nouvelle offre
                OffreEmploi offre = new OffreEmploi(
                        ttitre.getText(),
                        tdescription.getText(),
                        tlieu.getText()
                );
                offreService.create(offre);

                // Afficher un message de succès
                showAlert("Succès", "L'offre a été créée avec succès !");
            } else {
                // Mode mise à jour : mettre à jour l'offre existante
                offreToUpdate.setTitle(ttitre.getText());
                offreToUpdate.setDescription(tdescription.getText());
                offreToUpdate.setLocation(tlieu.getText());
                offreService.update(offreToUpdate);

                // Afficher un message de succès
                showAlert("Succès", "L'offre a été mise à jour avec succès !");
            }

            // Retourner à la page d'affichage des offres d'emploi
            retournerAffichageOffres();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite lors de l'enregistrement de l'offre.");
        }
    }

    @FXML
    void cancel() {
        // Retourner à la page d'affichage des offres d'emploi
        retournerAffichageOffres();
    }

    private void retournerAffichageOffres() {
        try {
            // Charger la page d'affichage des offres
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
            Stage stage = (Stage) btnsave.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des offres d'emploi");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setOffreToUpdate(OffreEmploi offre) {
        this.offreToUpdate = offre;

        // Remplir les champs du formulaire avec les données de l'offre à mettre à jour
        ttitre.setText(offre.getTitle());
        tdescription.setText(offre.getDescription());
        tlieu.setText(offre.getLocation());
    }

    @FXML
    void afficherOffresRecrutement() {
        try {
            // Charger la page d'affichage des offres
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
            Stage stage = (Stage) ttitre.getScene().getWindow();
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