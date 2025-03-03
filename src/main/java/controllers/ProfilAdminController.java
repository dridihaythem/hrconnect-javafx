package controllers;

import entities.Utilisateur;
import entities.enums.Role;
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
import java.util.ArrayList;
import java.util.List;

public class ProfilAdminController {
    @FXML
    private VBox userContainer;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private TextField searchField;

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

    @FXML
    public void initialize() {
        // Add sorting options to the ComboBox
        sortComboBox.getItems().addAll("id", "nom", "prenom", "email", "role");
        sortComboBox.setValue("id"); // Default sorting by ID

        // Load users
        loadUsers();
    }

    @FXML
    private void handleSort() {
        String sortBy = sortComboBox.getValue();
        List<Utilisateur> sortedUsers = utilisateurCrud.sortUtilisateurs(sortBy);
        loadUsers(sortedUsers);
    }


    private void loadUsers(List<Utilisateur> users) {
        // Clear the VBox before loading new users
        userContainer.getChildren().clear();

        // Create a UI component for each user
        for (Utilisateur user : users) {
            HBox userBox = createUserBox(user);
            userContainer.getChildren().add(userBox);
        }
    }

    public void loadUsers() {
        loadUsers(utilisateurCrud.afficherEntite()); // Default load all users
    }

    private HBox createUserBox(Utilisateur user) {
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

        return userBox;
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
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadUsers(utilisateurCrud.afficherEntite()); // Reload all users if search term is empty
        } else {
            // Search across all roles
            List<Utilisateur> searchResults = new ArrayList<>();
            for (Role role : Role.values()) { // Iterate through all roles
                searchResults.addAll(utilisateurCrud.searchByNameOrEmail(searchTerm, role));
            }
            loadUsers(searchResults);
        }
    }
    private void handleDeleteUser(Utilisateur user) {
        utilisateurCrud.supprimerEntite(user.getId());
        loadUsers();
    }
}