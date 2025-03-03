package controllers;

import entities.Mailing;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.UtilisateurCrud;

import java.util.List;

public class ProfilMembreController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private VBox rhContainer; // For displaying RHs

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {
        // Add sorting options to the ComboBox
        sortComboBox.getItems().addAll("id", "nom", "prenom", "email", "role");
        sortComboBox.setValue("id"); // Default sorting by ID

        // Load current user
        currentUser = utilisateurCrud.getUtilisateurById(1); // Replace with actual user ID
        if (currentUser != null) {
            nomField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            emailField.setText(currentUser.getEmail());
            passwordField.setText(currentUser.getpassword());
        }
    }

    @FXML
    private void handleModifyProfile() {
        currentUser.setNom(nomField.getText());
        currentUser.setPrenom(prenomField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setpassword(passwordField.getText());

        utilisateurCrud.modifierEntite(currentUser);
        showAlert("Profil mis à jour avec succès !");
    }

    @FXML
    private void handleSearchRH() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadRHs(utilisateurCrud.getUtilisateursByRole(Role.RH)); // Reload all RHs if search term is empty
        } else {
            List<Utilisateur> searchResults = utilisateurCrud.searchByNameOrEmail(searchTerm, Role.RH);
            loadRHs(searchResults);
        }
    }

    @FXML
    private void handleSort() {
        String sortBy = sortComboBox.getValue();
        List<Utilisateur> sortedUsers = utilisateurCrud.sortUtilisateurs(sortBy);
        loadRHs(sortedUsers);
    }

    private void loadRHs(List<Utilisateur> rhs) {
        // Clear the VBox before loading new results
        rhContainer.getChildren().clear();

        // Display the results
        for (Utilisateur rh : rhs) {
            HBox rhBox = createRHBox(rh);
            rhContainer.getChildren().add(rhBox);
        }
    }

    private HBox createRHBox(Utilisateur rh) {
        HBox rhBox = new HBox(10); // Spacing between elements
        rhBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #004080; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add RH details (Nom, Prenom, Email, Role)
        Label nomLabel = new Label("Nom: " + rh.getNom());
        Label prenomLabel = new Label("Prénom: " + rh.getPrenom());
        Label emailLabel = new Label("Email: " + rh.getEmail());
        Label roleLabel = new Label("Role: " + rh.getroles());

        nomLabel.setStyle("-fx-text-fill: white;");
        prenomLabel.setStyle("-fx-text-fill: white;");
        emailLabel.setStyle("-fx-text-fill: white;");
        roleLabel.setStyle("-fx-text-fill: white;");

        // Add a "Contact" button
        Button contactButton = new Button("Contact");
        contactButton.setOnAction(event -> handleContactRH(rh));
        contactButton.getStyleClass().add("contact-button");

        // Add all components to the HBox
        rhBox.getChildren().addAll(nomLabel, prenomLabel, emailLabel, roleLabel, contactButton);

        return rhBox;
    }

    private void handleContactRH(Utilisateur rh) {
        // Open a mailing dialog to contact the RH
        openMailingDialog(rh.getEmail());
    }

    private void openMailingDialog(String recipientEmail) {
        // Create a dialog for sending an email
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Envoyer un message");
        dialog.setHeaderText("Envoyer un message à " + recipientEmail);

        // Set the buttons
        ButtonType sendButtonType = new ButtonType("Envoyer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        // Create the form
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Entrez votre message ici...");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Message:"), 0, 0);
        grid.add(messageArea, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a message
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                return messageArea.getText();
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(message -> {
            // Send the email using the Mailing class
            Mailing.sendEmail(recipientEmail, "Message from ProfilMembreController", message);
            showAlert("Message envoyé avec succès !");
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}