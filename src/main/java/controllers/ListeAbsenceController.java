package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Absence;
import services.AbsenceService;
import utils.ShowMenu;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ListeAbsenceController implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private TableView<Absence> absencesTable;

    @FXML
    private TableColumn<Absence, Integer> idColumn;

    @FXML
    private TableColumn<Absence, Integer> employeIdColumn;

    @FXML
    private TableColumn<Absence, Absence.Motif> motifColumn;

    @FXML
    private TableColumn<Absence, String> justificatifColumn;

    @FXML
    private TableColumn<Absence, String> remarqueColumn;

    @FXML
    private TableColumn<Absence, String> dateEnregistrementColumn;

    @FXML
    private MFXButton addBtn;

    @FXML
    private MFXButton editBtn;

    @FXML
    private MFXButton deleteBtn;

    AbsenceService absenceService = new AbsenceService();
    ObservableList<Absence> absencesList = FXCollections.observableArrayList();

    @FXML
    void onAdd(ActionEvent event) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbsence.fxml"));
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        absencesTable.getScene().setRoot(root);
    }

    @FXML
    void onEdit(ActionEvent event) {
        Absence selectedAbsence = absencesTable.getSelectionModel().getSelectedItem();
        if (selectedAbsence != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAbsence.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                ModifierAbsenceController controller = loader.getController();
                controller.setAbsence(selectedAbsence);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            absencesTable.getScene().setRoot(root);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une absence à modifier.");
            alert.showAndWait();
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        Absence selectedAbsence = absencesTable.getSelectionModel().getSelectedItem();
        if (selectedAbsence != null) {
            try {
                absenceService.deleteAbsence(selectedAbsence.getId());
                absencesList.remove(selectedAbsence);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de l'absence.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une absence à supprimer.");
            alert.showAndWait();
        }
    }

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));
        justificatifColumn.setCellValueFactory(new PropertyValueFactory<>("justificatif"));
        remarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        dateEnregistrementColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnregistrement"));

        try {
            List<Absence> absences = absenceService.getAllAbsences();
            absencesList.addAll(absences);
            absencesTable.setItems(absencesList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors du chargement des absences.");
            alert.showAndWait();
        }
    }
}
