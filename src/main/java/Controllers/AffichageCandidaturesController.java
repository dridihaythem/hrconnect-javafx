package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Candidat;
import models.Candidature;
import models.OffreEmploi;
import services.CandidatService;
import services.CandidatureService;
import services.OffreEmploiService;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AffichageCandidaturesController {

    @FXML
    private VBox tableContent;

    private CandidatureService candidatureService = new CandidatureService();
    private CandidatService candidatService = new CandidatService();
    private OffreEmploiService offreEmploiService = new OffreEmploiService();
    private int offreEmploiId;

    public void setOffreEmploiId(int offreEmploiId) {
        this.offreEmploiId = offreEmploiId;
        refreshCandidatures();
    }

    private void refreshCandidatures() {
        try {
            List<Candidature> candidatures = candidatureService.getCandidaturesByOffreEmploiId(offreEmploiId);
            tableContent.getChildren().clear();

            // Ajouter l'en-tête de la table
            GridPane header = new GridPane();
            header.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10;");
            header.getColumnConstraints().addAll(
                    createColumnConstraints(16.66),
                    createColumnConstraints(16.66),
                    createColumnConstraints(16.66),
                    createColumnConstraints(16.66),
                    createColumnConstraints(16.66),
                    createColumnConstraints(16.66)
            );
            header.add(createHeaderLabel("Nom"), 0, 0);
            header.add(createHeaderLabel("Prénom"), 1, 0);
            header.add(createHeaderLabel("Email"), 2, 0);
            header.add(createHeaderLabel("Téléphone"), 3, 0);
            header.add(createHeaderLabel("CV"), 4, 0);
            header.add(createHeaderLabel("Action"), 5, 0);
            tableContent.getChildren().add(header);

            // Ajouter les lignes de la table
            for (Candidature candidature : candidatures) {
                Candidat candidat = candidatService.getById(candidature.getCandidatId());
                if (candidat != null) {
                    GridPane row = new GridPane();
                    row.setStyle("-fx-padding: 10; -fx-background-color: #ffffff;");
                    row.getColumnConstraints().addAll(
                            createColumnConstraints(16.66),
                            createColumnConstraints(16.66),
                            createColumnConstraints(16.66),
                            createColumnConstraints(16.66),
                            createColumnConstraints(16.66),
                            createColumnConstraints(16.66)
                    );

                    Label nomLabel = createDataLabel(candidat.getLastName());
                    Label prenomLabel = createDataLabel(candidat.getFirstName());
                    Label emailLabel = createDataLabel(candidat.getEmail());
                    Label telephoneLabel = createDataLabel(candidat.getPhone());

                    Hyperlink cvLink = new Hyperlink("CV");
                    cvLink.setOnAction(event -> {
                        try {
                            File cvFile = new File(candidature.getCv());
                            if (cvFile.exists()) {
                                Desktop.getDesktop().open(cvFile);
                            } else {
                                System.out.println("Le fichier CV n'existe pas.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    HBox actionBox = new HBox(10);
                    Button greenButton = new Button("✔");
                    greenButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    greenButton.setOnAction(event -> handleAcceptCandidature(candidature));

                    Button redButton = new Button("✘");
                    redButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    redButton.setOnAction(event -> handleRejectCandidature(candidature));

                    actionBox.getChildren().addAll(greenButton, redButton);

                    row.add(nomLabel, 0, 0);
                    row.add(prenomLabel, 1, 0);
                    row.add(emailLabel, 2, 0);
                    row.add(telephoneLabel, 3, 0);
                    row.add(cvLink, 4, 0);
                    row.add(actionBox, 5, 0);
                    tableContent.getChildren().add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ColumnConstraints createColumnConstraints(double percentWidth) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(percentWidth);
        return columnConstraints;
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold;");
        GridPane.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return label;
    }

    private Label createDataLabel(String text) {
        Label label = new Label(text);
        GridPane.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return label;
    }

    private void handleRejectCandidature(Candidature candidature) {
        try {
            candidatureService.delete(candidature);
            refreshCandidatures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAcceptCandidature(Candidature candidature) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("En acceptant cette candidature, l'offre d'emploi sera clôturée et toutes les autres candidatures seront supprimées. Voulez-vous continuer ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer l'offre d'emploi et toutes les candidatures associées
                OffreEmploi offreEmploi = new OffreEmploi();
                offreEmploi.setId(offreEmploiId);
                offreEmploiService.delete(offreEmploi);
                candidatureService.deleteByOffreEmploiId(offreEmploiId);

                // Rediriger vers la page d'affichage des offres d'emploi
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
                Stage stage = (Stage) tableContent.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des offres d'emploi");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void afficherOffresRecrutement() {
        try {
            // Charger la page d'affichage des offres d'emploi
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
            Stage stage = (Stage) tableContent.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des offres d'emploi");
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