package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import javafx.geometry.Insets;

public class AffichageCandidaturesController {

    @FXML
    private VBox tableContent;

    @FXML
    private ComboBox<String> offreComboBox;

    @FXML
    private VBox candidaturesContainer;

    private CandidatureService candidatureService = new CandidatureService();
    private CandidatService candidatService = new CandidatService();
    private OffreEmploiService offreEmploiService = new OffreEmploiService();

    // Identifiants Mailjet
    private static final String MAILJET_API_KEY_PUBLIC = "2ebaf1f11393a3f2ba29c2f97d789507";
    private static final String MAILJET_API_KEY_PRIVATE = "3f8a77cc55415a7cc005d2533d14699b";
    private static final String MAILJET_TEMPLATE_ID_ACCEPTED = "6741933";
    private static final String MAILJET_TEMPLATE_ID_REJECTED = "6741947";

    @FXML
    public void initialize() {
        if (tableContent == null) {
            System.out.println("Error: tableContent not injected!");
            return;
        }
        loadOffres();
        refreshCandidatures();
    }

    private void loadOffres() {
        try {
            List<OffreEmploi> offres = offreEmploiService.getAll();
            for (OffreEmploi offre : offres) {
                offreComboBox.getItems().add(offre.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOffreSelection() {
        String selectedTitle = offreComboBox.getSelectionModel().getSelectedItem();
        if (selectedTitle != null) {
            try {
                OffreEmploi selectedOffre = offreEmploiService.getByTitle(selectedTitle);
                if (selectedOffre != null) {
                    refreshCandidatures(selectedOffre.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshCandidatures() {
        try {
            tableContent.getChildren().clear();
            List<Candidature> candidatures = candidatureService.getAllCandidatures();

            // En-tête
            GridPane header = new GridPane();
            header.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            header.getColumnConstraints().addAll(
                    createColumnConstraints(25),  // Titre
                    createColumnConstraints(25),  // Email
                    createColumnConstraints(20),  // Téléphone
                    createColumnConstraints(15),  // CV
                    createColumnConstraints(15)   // Actions
            );

            // Ajouter du padding et des marges
            header.setHgap(20); // Espacement horizontal entre les colonnes
            header.setVgap(15); // Espacement vertical
            header.setPadding(new Insets(10, 20, 10, 20)); // Padding autour du header

            header.add(createHeaderLabel("Titre"), 0, 0);
            header.add(createHeaderLabel("Email"), 1, 0);
            header.add(createHeaderLabel("Téléphone"), 2, 0);
            header.add(createHeaderLabel("CV"), 3, 0);
            header.add(createHeaderLabel("Actions"), 4, 0);

            tableContent.getChildren().add(header);

            for (Candidature candidature : candidatures) {
                Candidat candidat = candidatService.getById(candidature.getCandidatId());
                if (candidat != null) {
                    GridPane row = new GridPane();
                    row.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
                    row.getColumnConstraints().addAll(
                            createColumnConstraints(25),
                            createColumnConstraints(25),
                            createColumnConstraints(20),
                            createColumnConstraints(15),
                            createColumnConstraints(15)
                    );

                    // Ajouter du padding et des marges
                    row.setHgap(20); // Espacement horizontal entre les colonnes
                    row.setVgap(15); // Espacement vertical
                    row.setPadding(new Insets(10, 20, 10, 20)); // Padding autour de chaque ligne

                    // Ajouter les informations dans la ligne
                    row.add(createDataLabel(offreEmploiService.getById(candidature.getOffreEmploiId()).getTitle()), 0, 0);
                    row.add(createDataLabel(candidat.getEmail()), 1, 0);
                    row.add(createDataLabel(candidat.getPhone()), 2, 0);

                    // Lien vers le CV
                    Hyperlink cvLink = new Hyperlink("Voir CV");
                    cvLink.setOnAction(e -> openCV(candidature.getCv()));
                    row.add(cvLink, 3, 0);

                    // Boutons d'action
                    HBox actionBox = new HBox(10);
                    Button acceptButton = new Button("✔");
                    acceptButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    acceptButton.setOnAction(e -> handleAcceptCandidature(candidature));

                    Button rejectButton = new Button("✘");
                    rejectButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    rejectButton.setOnAction(e -> handleRejectCandidature(candidature));

                    actionBox.getChildren().addAll(acceptButton, rejectButton);
                    row.add(actionBox, 4, 0);

                    tableContent.getChildren().add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du chargement des candidatures.");
        }
    }

    private void openCV(String cvPath) {
        try {
            File cvFile = new File(cvPath);
            if (cvFile.exists()) {
                Desktop.getDesktop().open(cvFile);
            } else {
                showAlert("Erreur", "Le fichier CV n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le fichier CV.");
        }
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    private Label createDataLabel(String text) {
        return new Label(text != null ? text : "");
    }

    private ColumnConstraints createColumnConstraints(double percentage) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(percentage);
        return column;
    }

    private void handleRejectCandidature(Candidature candidature) {
        try {
            // Mettre à jour le statut avant la suppression
            candidatureService.updateCandidatureStatus(candidature.getReference(), "refusée");

            // Envoyer l'email et supprimer
            sendEmail(candidature, "rejected");
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
                // Mettre à jour le statut de la candidature acceptée
                candidatureService.updateCandidatureStatus(candidature.getReference(), "acceptée");

                // Envoyer l'email d'acceptation au candidat accepté
                sendEmail(candidature, "accepted");

                // Récupérer et traiter les autres candidatures
                List<Candidature> otherCandidatures = candidatureService.getCandidaturesByOffreEmploiId(candidature.getOffreEmploiId());
                for (Candidature otherCandidature : otherCandidatures) {
                    if (otherCandidature.getId() != candidature.getId()) {
                        // Envoyer l'email de refus avant la suppression
                        sendEmail(otherCandidature, "rejected");
                        candidatureService.delete(otherCandidature);
                    }
                }

                // Supprimer l'offre d'emploi
                OffreEmploi offreEmploi = new OffreEmploi();
                offreEmploi.setId(candidature.getOffreEmploiId());
                offreEmploiService.delete(offreEmploi);

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("La candidature a été acceptée et l'offre d'emploi a été clôturée avec succès !");
                successAlert.showAndWait();

                // Rediriger vers la page d'affichage des offres d'emploi
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffre.fxml"));
                Stage stage = (Stage) tableContent.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des offres d'emploi");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur est survenue lors du traitement de la candidature : " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void sendEmail(Candidature candidature, String status) {
        try {
            Candidat candidat = candidatService.getById(candidature.getCandidatId());
            OffreEmploi offreEmploi = offreEmploiService.getById(candidature.getOffreEmploiId());

            if (candidat == null || offreEmploi == null) {
                throw new Exception("Impossible de récupérer les informations du candidat ou de l'offre");
            }

            String templateId = status.equals("accepted") ? MAILJET_TEMPLATE_ID_ACCEPTED : MAILJET_TEMPLATE_ID_REJECTED;
            String jsonPayload = "{"
                    + "\"Messages\":[{"
                    + "\"From\": {\"Email\": \"aminraissi54@gmail.com\", \"Name\": \"Amine\"},"
                    + "\"To\": [{\"Email\": \"" + candidat.getEmail() + "\", \"Name\": \"" + candidat.getFirstName() + " " + candidat.getLastName() + "\"}],"
                    + "\"TemplateID\": " + templateId + ","
                    + "\"TemplateLanguage\": true,"
                    + "\"Subject\": \"" + (status.equals("accepted") ? "Félicitations ! Votre candidature a été acceptée" : "Mise à jour sur votre candidature") + "\","
                    + "\"Variables\": {"
                    + "\"firstName\": \"" + candidat.getFirstName() + "\","
                    + "\"lastName\": \"" + candidat.getLastName() + "\","
                    + "\"jobTitle\": \"" + offreEmploi.getTitle() + "\""
                    + "}"
                    + "}]"
                    + "}";

            URI uri = new URI("https://api.mailjet.com/v3.1/send");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((MAILJET_API_KEY_PUBLIC + ":" + MAILJET_API_KEY_PRIVATE).getBytes(StandardCharsets.UTF_8)));
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Email sent successfully.");
            } else {
                System.out.println("Failed to send email. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void refreshCandidatures(int offreId) {
        try {
            tableContent.getChildren().clear();
            List<Candidature> candidatures = candidatureService.getCandidaturesByOffreEmploiId(offreId);

            // En-tête
            GridPane header = new GridPane();
            header.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            header.getColumnConstraints().addAll(
                    createColumnConstraints(25),  // Titre
                    createColumnConstraints(25),  // Email
                    createColumnConstraints(20),  // Téléphone
                    createColumnConstraints(15),  // CV
                    createColumnConstraints(15)   // Actions
            );

            // Ajouter du padding et des marges
            header.setHgap(20); // Espacement horizontal entre les colonnes
            header.setVgap(15); // Espacement vertical
            header.setPadding(new Insets(10, 20, 10, 20)); // Padding autour du header

            header.add(createHeaderLabel("Titre"), 0, 0);
            header.add(createHeaderLabel("Email"), 1, 0);
            header.add(createHeaderLabel("Téléphone"), 2, 0);
            header.add(createHeaderLabel("CV"), 3, 0);
            header.add(createHeaderLabel("Actions"), 4, 0);

            tableContent.getChildren().add(header);

            for (Candidature candidature : candidatures) {
                Candidat candidat = candidatService.getById(candidature.getCandidatId());
                if (candidat != null) {
                    GridPane row = new GridPane();
                    row.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
                    row.getColumnConstraints().addAll(
                            createColumnConstraints(25),
                            createColumnConstraints(25),
                            createColumnConstraints(20),
                            createColumnConstraints(15),
                            createColumnConstraints(15)
                    );

                    // Ajouter du padding et des marges
                    row.setHgap(20); // Espacement horizontal entre les colonnes
                    row.setVgap(15); // Espacement vertical
                    row.setPadding(new Insets(10, 20, 10, 20)); // Padding autour de chaque ligne

                    // Ajouter les informations dans la ligne
                    row.add(createDataLabel(offreEmploiService.getById(candidature.getOffreEmploiId()).getTitle()), 0, 0);
                    row.add(createDataLabel(candidat.getEmail()), 1, 0);
                    row.add(createDataLabel(candidat.getPhone()), 2, 0);

                    // Lien vers le CV
                    Hyperlink cvLink = new Hyperlink("Voir CV");
                    cvLink.setOnAction(e -> openCV(candidature.getCv()));
                    row.add(cvLink, 3, 0);

                    // Boutons d'action
                    HBox actionBox = new HBox(10);
                    Button acceptButton = new Button("✔");
                    acceptButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    acceptButton.setOnAction(e -> handleAcceptCandidature(candidature));

                    Button rejectButton = new Button("✘");
                    rejectButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    rejectButton.setOnAction(e -> handleRejectCandidature(candidature));

                    actionBox.getChildren().addAll(acceptButton, rejectButton);
                    row.add(actionBox, 4, 0);

                    tableContent.getChildren().add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du chargement des candidatures.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void afficherCandidatures() {
        try {
            // Charger la page d'affichage des candidatures
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/affichageCandidatures.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableContent.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des candidatures");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du chargement des candidatures.");
        }
    }
}