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
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;

public class ProfilRhController implements ParentController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox memberContainer; // For displaying Stagiaires and Employés
    @FXML
    private VBox sidePanel; // Side panel for navigation

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {
        setupSidePanel();

        // Load current RH user
        currentUser = utilisateurCrud.getUtilisateurById(MyConnection.getInstance().getId());
        if (currentUser != null) {
            nomField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            emailField.setText(currentUser.getEmail());
            passwordField.setText(currentUser.getpassword());
        }

        // Load Stagiaires and Employés
        loadMembers();
    }

    private void setupSidePanel() {
        Button profileButton = new Button("Gestion du Profil");
        profileButton.setOnAction(event -> loadProfile());

        Button memberButton = new Button("Gestion des Membres");
        memberButton.setOnAction(event -> loadMembers());

        sidePanel.getChildren().addAll(profileButton, memberButton);
    }

    private void loadProfile() {
        // Clear the member container and show profile fields
        memberContainer.getChildren().clear();
    }

    @Override
    public void loadMembers() {
        // Clear the VBox before loading new members
        memberContainer.getChildren().clear();

        // Fetch Stagiaires (USER) and Employés (EMPLOYE)
        List<Utilisateur> members = utilisateurCrud.getUtilisateursByRole(Role.USER); // Stagiaires
        members.addAll(utilisateurCrud.getUtilisateursByRole(Role.EMPLOYE)); // Employés

        // Create a UI component for each member
        for (Utilisateur member : members) {
            HBox memberBox = createMemberBox(member);
            memberContainer.getChildren().add(memberBox);
        }
    }

    private HBox createMemberBox(Utilisateur member) {
        HBox memberBox = new HBox(10); // Spacing between elements
        memberBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #004080; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add member details (ID, Nom, Prenom, Email, Role)
        Label idLabel = new Label("ID: " + member.getId());
        Label nomLabel = new Label("Nom: " + member.getNom());
        Label prenomLabel = new Label("Prénom: " + member.getPrenom());
        Label emailLabel = new Label("Email: " + member.getEmail());
        Label roleLabel = new Label("Role: " + member.getroles());

        idLabel.setStyle("-fx-text-fill: white;");
        nomLabel.setStyle("-fx-text-fill: white;");
        prenomLabel.setStyle("-fx-text-fill: white;");
        emailLabel.setStyle("-fx-text-fill: white;");
        roleLabel.setStyle("-fx-text-fill: white;");

        // Add buttons for modify and delete
        Button modifyButton = new Button("Modifier");
        modifyButton.setOnAction(event -> handleModifyMember(member));
        modifyButton.getStyleClass().add("modify-button");

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> handleDeleteMember(member));
        deleteButton.getStyleClass().add("delete-button");

        // Add all components to the HBox
        memberBox.getChildren().addAll(idLabel, nomLabel, prenomLabel, emailLabel, roleLabel, modifyButton, deleteButton);

        return memberBox;
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

    private void handleModifyMember(Utilisateur member) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfilePage.fxml"));
            Parent root = loader.load();

            EditProfilePageController controller = loader.getController();
            controller.setUser(member);
            controller.setParentController(this);
            controller.setRole(member.getroles().name());

            Stage stage = new Stage();
            stage.setTitle("Modifier Profil");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteMember(Utilisateur member) {
        // Delete the member from the database
        utilisateurCrud.supprimerEntite(member.getId());

        // Reload the members
        loadMembers();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}