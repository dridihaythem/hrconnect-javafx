package controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Candidat;
import services.CandidatService;
import utils.SessionManager;

public class FormulaireCandidatController {

    @FXML
    private TextField tnom;

    @FXML
    private TextField tprenom;

    @FXML
    private TextField temail;

    @FXML
    private TextField ttelephone;

    @FXML
    private VBox formContainer;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label phoneErrorLabel;

    @FXML
    private Button btnsave;

    @FXML
    private Label formTitle;

    @FXML
    private Label hrConnectTitle;

    @FXML
    private Hyperlink loginLink;

    public void initialize() {
        // Ajouter des écouteurs d'événements pour les champs de texte
        temail.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        ttelephone.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        tnom.textProperty().addListener((observable, oldValue, newValue) -> validateName(tnom));
        tprenom.textProperty().addListener((observable, oldValue, newValue) -> validateName(tprenom));

        // Ajouter des animations pour le bouton
        addHoverAnimation(btnsave);

        // Ajouter une animation pour le titre HRCONNECT
        addTitleAnimation();

        // Ajouter un événement pour le lien de connexion
        loginLink.setOnAction(event -> showLoginForm());
    }

    private void validateFields() {
        validateEmail();
        validatePhone();
        btnsave.setDisable(!isFormValid());
    }

    private void validateEmail() {
        String email = temail.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailErrorLabel.setText("Email invalide");
            emailErrorLabel.setTextFill(Color.RED);
        } else {
            emailErrorLabel.setText("");
        }
    }

    private void validatePhone() {
        String phone = ttelephone.getText();
        if (!phone.matches("\\d{8}")) {
            phoneErrorLabel.setText("Le numéro de téléphone doit contenir 8 chiffres");
            phoneErrorLabel.setTextFill(Color.RED);
        } else {
            phoneErrorLabel.setText("");
        }
    }

    private void validateName(TextField textField) {
        String name = textField.getText();
        if (!name.matches("[a-zA-Z]*")) {
            textField.setText(name.replaceAll("[^a-zA-Z]", ""));
        }
    }

    private boolean isFormValid() {
        return !tnom.getText().isEmpty() && !tprenom.getText().isEmpty() &&
                emailErrorLabel.getText().isEmpty() && phoneErrorLabel.getText().isEmpty();
    }

    @FXML
    void suivant() {
        if (isFormValid()) {
            try {
                CandidatService candidatService = new CandidatService();

                // Vérifier si l'email ou le numéro de téléphone est déjà utilisé
                if (candidatService.isEmailUsed(temail.getText())) {
                    showAlert("Erreur de validation", "Cet email est déjà utilisé.");
                    return;
                }

                if (candidatService.isPhoneUsed(ttelephone.getText())) {
                    showAlert("Erreur de validation", "Ce numéro de téléphone est déjà utilisé.");
                    return;
                }

                // Créer un candidat avec les informations du formulaire
                Candidat candidat = new Candidat(
                        tnom.getText(),
                        tprenom.getText(),
                        temail.getText(),
                        ttelephone.getText()
                );

                // Enregistrer le candidat dans la base de données
                int candidatId = candidatService.createAndGetId(candidat);

                // Stocker le candidat dans SessionManager
                SessionManager.setCandidat(candidat);

                // Charger la page d'affichage des offres d'emploi pour les candidats
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) tnom.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des offres d'emploi");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur de validation", "Veuillez corriger les erreurs avant de continuer.");
        }
    }

    @FXML
    void afficherOffres() {
        if (isFormValid()) {
            try {
                // Charger la page d'affichage des offres pour les candidats
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
                Stage stage = (Stage) formContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des offres d'emploi");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur de validation", "Veuillez remplir le formulaire pour voir les offres d'emploi.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), button);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        button.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    private void addTitleAnimation() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), hrConnectTitle);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.1);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.1);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    private void showLoginForm() {
        try {
            // Charger la page de connexion
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
            Stage stage = (Stage) formContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}