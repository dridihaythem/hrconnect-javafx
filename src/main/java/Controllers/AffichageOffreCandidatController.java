package Controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Candidature;
import models.OffreEmploi;
import services.CandidatureService;
import services.OffreEmploiService;
import utils.SessionManager;

import java.util.List;

public class AffichageOffreCandidatController {

    @FXML
    private VBox offresContainer;

    @FXML
    private Label hrConnectTitle;

    private OffreEmploiService offreService = new OffreEmploiService();
    private CandidatureService candidatureService = new CandidatureService();

    @FXML
    public void initialize() {
        // Ajouter l'animation pour le titre HRCONNECT
        addTitleAnimation();
        refreshOffres();
    }

    private void refreshOffres() {
        try {
            List<OffreEmploi> offres = offreService.getAll();
            offresContainer.getChildren().clear();

            // Ajouter le titre "Offres d'emploi" centré
            HBox header = new HBox();
            header.setSpacing(20);
            header.setStyle("-fx-alignment: center;");

            Label titre = new Label("Offres d'emploi");
            titre.getStyleClass().add("centered-title");

            header.getChildren().addAll(titre);
            offresContainer.getChildren().add(header);

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

                // Bouton Afficher la description
                Button btnDescription = new Button("Description");
                btnDescription.getStyleClass().add("custom-button");
                btnDescription.setOnAction(event -> {
                    try {
                        // Charger la page de description
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/descriptionOffre.fxml"));
                        Parent root = loader.load();
                        DescriptionOffreController controller = loader.getController();
                        controller.setOffre(offre); // Passer l'offre à la nouvelle page

                        Stage stage = (Stage) offresContainer.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Description de l'offre");
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Ajouter les détails et les boutons à la carte
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                details.getChildren().addAll(titreOffre, lieu);
                card.getChildren().addAll(imageView, details, spacer, btnDescription);

                // Ajouter la carte à la liste
                offresContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherOffres() {
        Stage stage = (Stage) offresContainer.getScene().getWindow();
        if (stage.getTitle().equals("Offres d'emploi")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Vous êtes déjà sur la page des offres d'emploi.");
            alert.showAndWait();
        } else {
            try {
                // Charger la page d'affichage des offres d'emploi
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Offres d'emploi");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void afficherMesCandidatures() {
        try {
            int candidatId = SessionManager.getCandidat().getId();
            List<Candidature> candidatures = candidatureService.getCandidaturesByCandidatId(candidatId);

            if (candidatures.isEmpty()) {
                showAlert("Aucune candidature", "Vous n'avez pas de candidatures.");
            } else {
                // Charger la page d'affichage des candidatures de l'utilisateur
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageMesCandidatures.fxml"));
                Stage stage = (Stage) offresContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mes Candidatures");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}