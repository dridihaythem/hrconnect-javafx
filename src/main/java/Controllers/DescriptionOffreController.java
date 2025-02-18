package Controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.OffreEmploi;
import utils.SessionManager;
import models.Candidat;

public class DescriptionOffreController {

    @FXML
    private Label titreLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label lieuLabel;

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnPostuler;

    @FXML
    private Label hrConnectTitle;

    private OffreEmploi offre;

    public void initialize() {
        // Add hover animation to buttons
        addHoverAnimation(btnRetour);
        addHoverAnimation(btnPostuler);

        // Add title animation
        addTitleAnimation();
    }

    public void setOffre(OffreEmploi offre) {
        this.offre = offre;
        titreLabel.setText(offre.getTitle());
        descriptionLabel.setText(offre.getDescription());
        lieuLabel.setText("Lieu : " + offre.getLocation());
    }

    @FXML
    void postuler() {
        try {
            // Load the application form page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulaireCandidature.fxml"));
            Parent root = loader.load();

            // Pass the offer and candidate to the FormulaireCandidatureController
            FormulaireCandidatureController controller = loader.getController();
            controller.setOffreEmploiId(offre.getId());

            // Check if the candidate is null before passing it
            Candidat candidat = SessionManager.getCandidat();
            if (candidat != null) {
                controller.setCandidat(candidat);
            } else {
                System.out.println("Candidat is null");
                // You can also display an alert here to inform the user
            }

            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Postuler à l'offre");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retourListe() {
        try {
            // Load the job offers page for candidates
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offres d'emploi");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherOffres() {
        try {
            // Load the job offers page for candidates
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/affichageOffreCandidat.fxml"));
            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offres d'emploi");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deconnexion() {
        // Handle logout logic here
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
}