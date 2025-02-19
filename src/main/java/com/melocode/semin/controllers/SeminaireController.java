package com.melocode.semin.controllers;

import com.melocode.semin.dao.SeminaireDAO;
import com.melocode.semin.models.Seminaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class SeminaireController {

    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField formateurField;
    @FXML
    private TextField coutField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TableView<Seminaire> tableViewSeminaire;

    @FXML
    public void initialize() {
        // Populate the ComboBox with seminar types
        typeComboBox.setItems(FXCollections.observableArrayList(
                "Workshop",
                "Conference",
                "Webinar",
                "Training",
                "Seminar"
        ));

        // Optionally, you can load seminars when the view is initialized
        afficherSeminaire();
    }

    @FXML
    public void supprimerSeminaire() {
        Seminaire selectedSeminaire = tableViewSeminaire.getSelectionModel().getSelectedItem();
        if (selectedSeminaire != null) {
            SeminaireDAO.supprimerSeminaire(selectedSeminaire.getId());
            tableViewSeminaire.getItems().remove(selectedSeminaire);
            showAlert(Alert.AlertType.INFORMATION, "Séminaire Supprimé", "Le séminaire a été supprimé avec succès !");
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un séminaire à supprimer.");
        }
    }

    @FXML
    public void ajouterSeminaire() {
        try {
            // Validate input
            if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() || dateDebutField.getValue() == null ||
                    dateFinField.getValue() == null || lieuField.getText().isEmpty() || formateurField.getText().isEmpty() ||
                    coutField.getText().isEmpty() || typeComboBox.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            double cout = Double.parseDouble(coutField.getText());

            String nom = nomField.getText();
            String description = descriptionField.getText();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String lieu = lieuField.getText();
            String formateur = formateurField.getText();
            String type = typeComboBox.getValue();

            Seminaire seminaire = new Seminaire(0, nom, description, dateDebut, dateFin, lieu, formateur, cout, type);
            SeminaireDAO.ajouterSeminaire(seminaire);
            tableViewSeminaire.getItems().add(seminaire);
            showAlert(Alert.AlertType.INFORMATION, "Séminaire Ajouté", "Le séminaire a été ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le champ 'Coût' doit être un nombre.");
        }
    }

    @FXML
    public void modifierSeminaire() {
        Seminaire selectedSeminaire = tableViewSeminaire.getSelectionModel().getSelectedItem();
        if (selectedSeminaire == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un séminaire à modifier.");
            return;
        }

        try {
            // Update the seminar details
            selectedSeminaire.setNom(nomField.getText());
            selectedSeminaire.setDescription(descriptionField.getText());
            selectedSeminaire.setDateDebut(dateDebutField.getValue());
            selectedSeminaire.setDateFin(dateFinField.getValue());
            selectedSeminaire.setLieu(lieuField.getText());
            selectedSeminaire.setFormateur(formateurField.getText());
            selectedSeminaire.setCout(Double.parseDouble(coutField.getText()));
            selectedSeminaire.setType(typeComboBox.getValue());

            SeminaireDAO.modifierSeminaire(selectedSeminaire);

            // Refresh the table view
            tableViewSeminaire.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Séminaire Modifié", "Le séminaire a été modifié avec succès !");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le champ 'Coût' doit être un nombre.");
        }
    }

    @FXML
    public void afficherSeminaire() {
        ObservableList<Seminaire> seminaireList = SeminaireDAO.recupererTousLesSeminaires();
        tableViewSeminaire.setItems(seminaireList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
