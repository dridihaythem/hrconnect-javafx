package controllers.formations;

import exceptions.InvalidInputException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import jfxtras.scene.control.CalendarTextField;
import models.Formateur;
import models.Formation;
import services.FormateurService;
import services.FormationService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierFormationController implements Initializable {

    private Formation formation;


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
    FormateurService formateurService = new FormateurService();

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
            }else if (!datefin.getText().isEmpty() && datefin.getCalendar().getTime().before(dateDebut.getCalendar().getTime())) {
                throw new InvalidInputException("La date de fin doit être supérieure à la date de début");
            }

            formation.setTitle(title.getText());
            formation.setDescription(description.getText());
            formation.setFormateur_id(formateur.getValue().getId());
            formation.setIs_online(typeFormation.isSelected());
            formation.setPlace(emplacement.getText());
            formation.setAvailable_for_employee(pourEmployes.isSelected());
            formation.setAvailable_for_intern(pourStagaires.isSelected());
            formation.setStart_date(dateDebut.getCalendar().getTime());

            if (!datefin.getText().isEmpty()) {
                formation.setEnd_date(datefin.getCalendar().getTime());
            }

            fs.update(formation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Formation modifiée avec succès");
            alert.showAndWait();

        }catch (InvalidInputException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }catch (Exception e){
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
        FormateurService fs = new FormateurService();
        List<Formateur> formateurs = new ArrayList<>();

        try {
            formateurs.addAll(fs.getAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // Custom cell factory to show only the name in the dropdown list
        formateur.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Formateur formateur, boolean empty) {
                super.updateItem(formateur, empty);
                setText(empty || formateur == null ? "" : formateur.getFirstName() + " " + formateur.getLastName());
            }
        });

        // show name when selected
        formateur.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Formateur formateur, boolean empty) {
                super.updateItem(formateur, empty);
                setText(empty || formateur == null ? "" : formateur.getFirstName() + " " + formateur.getLastName());
            }
        });


        // Add formateurs to the ComboBox
        formateur.getItems().addAll(formateurs);

        typeFormation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                emplacement.setDisable(true);
            }else{
                emplacement.setDisable(false);
            }
        });
    }

    public void setFormation(Formation formation) throws SQLException {
        this.formation = formation;
        System.out.println(formation);
        // Initialize the form with the formation data
        title.setText(formation.getTitle());
        description.setText(formation.getDescription());
        Formateur oldFormateur = formateurService.getById(formation.getFormateur_id());
        formateur.setValue(oldFormateur);
        typeFormation.setSelected(formation.isIs_online());
        emplacement.setText(formation.getPlace());
        dateDebut.setText(formation.getStart_date().toString());
        if(formation.getEnd_date() != null){
            datefin.setText(formation.getEnd_date().toString());
        }
        pourEmployes.setSelected(formation.isAvailable_for_employee());
        pourStagaires.setSelected(formation.isAvailable_for_intern());

    }
}
