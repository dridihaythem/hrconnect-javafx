package controllers.formations;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import jfxtras.scene.control.CalendarTextField;
import models.Formateur;
import models.Formation;
import services.FormateurService;
import services.FormationService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterFormationController implements Initializable {

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
        //TODO: check inputs
        try{
            //TODO : fixe time not working
            fs.create(new Formation( formateur.getValue().getId(),"image", title.getText(), description.getText(),emplacement.getText(), typeFormation.isSelected(), pourEmployes.isSelected(), pourStagaires.isSelected(), dateDebut.getCalendar().getTime(), datefin.getCalendar().getTime()));
            //TODO: show alert
        }catch (Exception e){
            //TODO : show alert
            System.out.println(e.getMessage());
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

        // Set how the ComboBox should display items (showing only names)
        formateur.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Formateur formateur) {
                return formateur != null ? formateur.getFirstName() : "";
            }

            @Override
            public Formateur fromString(String string) {
                return formateur.getItems().stream()
                        .filter(f -> f.getFirstName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Custom cell factory to show only the name in the dropdown list
        formateur.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Formateur formateur, boolean empty) {
                super.updateItem(formateur, empty);
                setText(empty || formateur == null ? "" : formateur.getFirstName());
            }
        });


        // Add formateurs to the ComboBox
        formateur.getItems().addAll(formateurs);
    }

}
