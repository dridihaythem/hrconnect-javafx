package controllers;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UtilisateurCrud;

import java.io.IOException;
import java.util.List;

public class ProfilAdminController {
    @FXML
    private VBox userContainer;

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

    @FXML
    public void initialize() {
        loadUsers();
    }

    public void loadUsers() {
        // Clear the VBox before loading new users
        userContainer.getChildren().clear();

        // Fetch all users from the database
        List<Utilisateur> utilisateurs = utilisateurCrud.afficherEntite();

        // Create a UI component for each user
        for (Utilisateur user : utilisateurs) {
            // Create an HBox to hold user details and buttons
            HBox userBox = new HBox(10); // Spacing between elements
            userBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");

            // Add user details (ID, Nom, Prenom, Email, Role)
            Label idLabel = new Label("ID: " + user.getId());
            Label nomLabel = new Label("Nom: " + user.getNom());
            Label prenomLabel = new Label("Prénom: " + user.getPrenom());
            Label emailLabel = new Label("Email: " + user.getEmail());
            Label roleLabel = new Label("Role: " + user.getroles());

            // Add buttons for modify and delete
            Button modifyButton = new Button("Modifier");
            modifyButton.setOnAction(event -> handleModifyUser(user));

            Button deleteButton = new Button("Supprimer");
            deleteButton.setOnAction(event -> handleDeleteUser(user));

            // Add all components to the HBox
            userBox.getChildren().addAll(idLabel, nomLabel, prenomLabel, emailLabel, roleLabel, modifyButton, deleteButton);

            // Add the HBox to the VBox
            userContainer.getChildren().add(userBox);
        }
    }

    private void handleModifyUser(Utilisateur user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfileDialogController.fxml"));
            Parent root = loader.load();

            EditProfileDialogController controller = loader.getController();
            controller.setUser(user);
            controller.setParentController(this);

            Stage stage = (Stage) userContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteUser(Utilisateur user) {
        utilisateurCrud.supprimerEntite(user.getId());
        loadUsers();
    }
}