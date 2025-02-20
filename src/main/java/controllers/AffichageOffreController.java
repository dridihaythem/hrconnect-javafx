package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.OffreEmploi;
import services.OffreEmploiService;
import java.util.List;
import java.util.Optional;

public class AffichageOffreController {

    @FXML
    private VBox offresContainer;

    private OffreEmploiService offreService = new OffreEmploiService();

    @FXML
    public void initialize() {
        refreshOffres();
    }

    private void refreshOffres() {
        try {
            List<OffreEmploi> offres = offreService.getAll();
            offresContainer.getChildren().clear();

            // Ajouter le titre "Offres de recrutement" centré
            HBox header = new HBox();
            header.setSpacing(20);
            header.setStyle("-fx-alignment: center;");

            Label titre = new Label("Offres de recrutement");
            titre.getStyleClass().add("centered-title");

            header.getChildren().addAll(titre);
            offresContainer.getChildren().add(header);

            // Ajouter le bouton "Ajouter offre d'emploi"
            HBox addButtonContainer = new HBox();
            addButtonContainer.setSpacing(20);
            addButtonContainer.setStyle("-fx-alignment: center-right;");

            Button addButton = new Button("Ajouter offre d'emploi");
            addButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #272b3f, #256b51); -fx-text-fill: white;");
            addButton.setOnAction(event -> ajouterOffre());

            addButtonContainer.getChildren().add(addButton);
            offresContainer.getChildren().add(addButtonContainer);

            for (OffreEmploi offre : offres) {
                // Créer une carte pour chaque offre
                HBox card = new HBox(10);
                card.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 5, 5, 5);");

                // Ajouter l'image par défaut
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/default-offre.png")));
                imageView.setFitWidth(100); // Ajuster la largeur de l'image
                imageView.setFitHeight(100); // Ajuster la hauteur de l'image

                // Détails de l'offre
                VBox details = new VBox(5);
                Label titreOffre = new Label(offre.getTitle());
                titreOffre.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

                Label lieu = new Label("Lieu : " + offre.getLocation());

                // Bouton Modifier
                Button btnModifier = new Button("Modifier");
                btnModifier.getStyleClass().add("custom-button");
                btnModifier.setOnAction(event -> {
                    try {
                        // Charger la page du formulaire avec les données de l'offre
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulaireOffre.fxml"));
                        Parent root = loader.load();
                        FormulaireOffreController controller = loader.getController();
                        controller.setOffreToUpdate(offre);

                        Stage stage = (Stage) offresContainer.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Modifier une offre d'emploi");
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Bouton Supprimer
                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.getStyleClass().add("custom-button");
                btnSupprimer.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText(null);
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer cette offre d'emploi ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            offreService.delete(offre); // Supprimer l'offre
                            refreshOffres(); // Rafraîchir la liste des offres
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Bouton Afficher la description
                Button btnDescription = new Button("Description");
                btnDescription.getStyleClass().add("custom-button");
                btnDescription.setOnAction(event -> {
                    try {
                        // Rediriger vers la page DescriptionOffreRH
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/descriptionOffreRH.fxml"));
                        Parent root = loader.load();
                        DescriptionOffreRHController controller = loader.getController();
                        controller.setOffre(offre); // Passer l'offre à la nouvelle page

                        Stage stage = (Stage) offresContainer.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Description de l'offre");
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Bouton Afficher les candidatures
                Button btnAfficherCandidatures = new Button("Afficher les candidatures");
                btnAfficherCandidatures.getStyleClass().add("custom-button");
                btnAfficherCandidatures.setOnAction(event -> {
                    try {
                        // Charger la page d'affichage des candidatures
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/affichageCandidature.fxml"));
                        Parent root = loader.load();

                        // Passer l'ID de l'offre d'emploi au contrôleur de la page d'affichage des candidatures
                        AffichageCandidaturesController controller = loader.getController();
                        controller.setOffreEmploiId(offre.getId());

                        // Afficher la nouvelle scène
                        Stage stage = (Stage) offresContainer.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Liste des candidatures");
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Ajouter les boutons à la carte
                HBox boutonsContainer = new HBox(10);
                boutonsContainer.getChildren().addAll(btnModifier, btnSupprimer, btnDescription, btnAfficherCandidatures);

                // Ajouter les détails et les boutons à la carte
                details.getChildren().addAll(titreOffre, lieu, boutonsContainer);
                card.getChildren().addAll(imageView, details);

                // Ajouter la carte à la liste
                offresContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterOffre() {
        try {
            // Charger la page du formulaire d'offre
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/formulaireOffre.fxml"));
            Stage stage = (Stage) offresContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une offre d'emploi");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherOffres(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Vous êtes déjà sur la page des offres d'emploi.");
        alert.showAndWait();
    }

    @FXML
    void afficherOffresRecrutement() {
        try {
            // Vérifier si nous sommes déjà sur la page des offres d'emploi
            Stage stage = (Stage) offresContainer.getScene().getWindow();
            if (stage.getTitle().equals("Liste des offres d'emploi")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Vous êtes déjà sur la page des offres d'emploi.");
                alert.showAndWait();
            } else {
                // Charger la page d'affichage des offres d'emploi
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des offres d'emploi");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exitToRole() {
        try {
            // Charger la page de sélection du rôle
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/role.fxml"));
            Stage stage = (Stage) offresContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sélection du rôle");
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