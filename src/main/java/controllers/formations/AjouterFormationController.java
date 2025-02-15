package controllers.formations;

import exceptions.InvalidInputException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.control.CalendarTextField;
import models.Formateur;
import models.Formation;
import services.FormateurService;
import services.FormationService;
import utils.ShowMenu;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AjouterFormationController implements ShowMenu, Initializable {

    @FXML
    private AnchorPane menu;

    @FXML
    private MFXButton cancelBtn;

    @FXML
    private CalendarTextField dateDebut;

    @FXML
    private CalendarTextField datefin;

    @FXML
    private MFXTextField description;

    @FXML
    private MFXTextField emplacement;

    @FXML
    private MFXLegacyComboBox<Formateur> formateur;

    @FXML
    private MFXToggleButton pourEmployes;

    @FXML
    private MFXToggleButton pourStagaires;

    @FXML
    private MFXButton saveBtn;

    @FXML
    private MFXTextField title;

    @FXML
    private MFXToggleButton typeFormation;

    FormationService fs = new FormationService();

    @FXML
    void onSave(ActionEvent event) {

        try {
            if (title.getText().isEmpty()) {
                throw new InvalidInputException("Le titre est requis");
            } else if (description.getText().isEmpty()) {
                throw new InvalidInputException("La description est requise");
            } else if (formateur.getValue() == null) {
                throw new InvalidInputException("Choisir un formateur");
            } else if (!typeFormation.isSelected() && emplacement.getText().isEmpty()) {
                throw new InvalidInputException("L'emplacement est requis pour une formation en présentiel");
            } else if (dateDebut.getCalendar().getTime().before(new Date())) {
                throw new InvalidInputException("La date de début doit être supérieure à la date actuelle");
            } else if (!datefin.getText().isEmpty() && datefin.getCalendar().getTime().before(dateDebut.getCalendar().getTime())) {
                throw new InvalidInputException("La date de fin doit être supérieure à la date de début");
            }

            Formation formation = new Formation(
                    formateur.getValue().getId(),
                    "image",
                    title.getText(),
                    description.getText(),
                    emplacement.getText(),
                    typeFormation.isSelected(),
                    pourEmployes.isSelected(),
                    pourStagaires.isSelected(),
                    dateDebut.getCalendar().getTime()
            );

            if (!datefin.getText().isEmpty()) {
                formation.setEnd_date(datefin.getCalendar().getTime());
            }

            fs.create(formation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Formation ajoutée avec succès");
            alert.showAndWait();

            //redirect to list

            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/ListeFormation.fxml"));
                root = loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            title.getScene().setRoot(root);

        } catch (InvalidInputException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("An error has occured");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeMenu(menu);

        FormateurService fs = new FormateurService();
        List<Formateur> formateurs = new ArrayList<>();

        try {
            formateurs.addAll(fs.getAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // Custom cell factory to show only the name in the dropdown list
        formateur.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Formateur formateur, boolean empty) {
                super.updateItem(formateur, empty);
                setText(empty || formateur == null ? "" : formateur.getFirstName() + " " + formateur.getLastName());
            }
        });

        // show name when selected
        formateur.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Formateur formateur, boolean empty) {
                super.updateItem(formateur, empty);
                setText(empty || formateur == null ? "" : formateur.getFirstName() + " " + formateur.getLastName());
            }
        });


        // Add formateurs to the ComboBox
        formateur.getItems().addAll(formateurs);

        typeFormation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                emplacement.setDisable(true);
            } else {
                emplacement.setDisable(false);
            }
        });

    }

    @FXML
    void OnClickCancelBtn() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/ListeFormation.fxml"));
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        title.getScene().setRoot(root);
    }

}
