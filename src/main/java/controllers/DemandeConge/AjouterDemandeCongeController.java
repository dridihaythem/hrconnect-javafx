package controllers.DemandeConge;

import exceptions.InvalidInputException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.control.CalendarTextField;
import models.Demande_Conge;
import models.Employe;
import models.TypeConge;
import services.DemandeCongeService;
import services.EmployeService;
import utils.ShowMenu;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjouterDemandeCongeController implements ShowMenu, Initializable {

    @FXML
    private AnchorPane menu;

    @FXML
    private MFXButton cancelBtn;

    @FXML
    private CalendarTextField dateDebut;

    @FXML
    private CalendarTextField dateFin;

    @FXML
    private MFXComboBox<TypeConge> typeConge;

    @FXML
    private MFXButton saveBtn;

    @FXML
    private MFXTextField employeId;

    DemandeCongeService demandeCongeService = new DemandeCongeService();

    @FXML
    void onSave(ActionEvent event) {

        try {
            if (employeId.getText().isEmpty()) {
                throw new InvalidInputException("L'identifiant de l'employé est requis");
            } else if (dateDebut.getCalendar().getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now())) {
                throw new InvalidInputException("La date de début doit être supérieure ou égale à la date actuelle");
            } else if (!dateFin.getText().isEmpty() && dateFin.getCalendar().getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isBefore(dateDebut.getCalendar().getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())) {
                throw new InvalidInputException("La date de fin doit être supérieure ou égale à la date de début");
            }

            EmployeService employeService = new EmployeService();
            Employe employe = employeService.getEmployeById(Integer.parseInt(employeId.getText()));

            Demande_Conge demande = new Demande_Conge(
                    employe,
                    typeConge.getValue(),
                    dateDebut.getCalendar().getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    dateFin.getCalendar().getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            );

            demandeCongeService.create(demande);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Demande de congé ajoutée avec succès");
            alert.showAndWait();

            //redirect to list
            Parent root = FXMLLoader.load(getClass().getResource("/DemandeConge/ListeDemandeConge.fxml"));
            employeId.getScene().setRoot(root);

        } catch (InvalidInputException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
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

        typeConge.getItems().addAll(TypeConge.values());
    }

    @FXML
    void OnClickCancelBtn() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/DemandeConge/ListeDemandeConge.fxml"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        employeId.getScene().setRoot(root);
    }
}