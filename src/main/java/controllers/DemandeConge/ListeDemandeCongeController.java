package controllers.DemandeConge;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Demande_Conge;
import models.Employe;
import services.DemandeCongeService;
import services.EmployeService;
import utils.ShowMenu;
import utils.TableCell;
import utils.TableRow;
import utils.enums.TableRowType;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ListeDemandeCongeController implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    DemandeCongeService demandeCongeService = new DemandeCongeService();
    EmployeService employeService = new EmployeService();

    @FXML
    private VBox vbox;

    @FXML
    private DatePicker filterStartDate;

    @FXML
    private DatePicker filterEndDate;

    @FXML
    private Label soldeCongesLabel;

    private List<Demande_Conge> allDemandes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);

        TableRow header = new TableRow(TableRowType.HEADER);

        header.addCell(new TableCell("Employé", 200));
        header.addCell(new TableCell("Type de congé", 120));
        header.addCell(new TableCell("Date de début", 120));
        header.addCell(new TableCell("Date de fin", 120));
        header.addCell(new TableCell("Statut", 120));
        header.addCell(new TableCell("Commentaire", 200));
        header.addCell(new TableCell("Solde Congés", 120));
        header.addCell(new TableCell("Restant", 120));
        header.addCell(new TableCell("Action", 120));

        vbox.getChildren().add(header.build());

        try {
            allDemandes = demandeCongeService.getAll();
            displayDemandes(allDemandes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void displayDemandes(List<Demande_Conge> demandes) {
        vbox.getChildren().clear();
        TableRow header = new TableRow(TableRowType.HEADER);

        header.addCell(new TableCell("Employé", 200));
        header.addCell(new TableCell("Type de congé", 120));
        header.addCell(new TableCell("Date de début", 120));
        header.addCell(new TableCell("Date de fin", 120));
        header.addCell(new TableCell("Statut", 120));
        header.addCell(new TableCell("Commentaire", 200));
        header.addCell(new TableCell("Solde Congés", 120));
        header.addCell(new TableCell("Restant", 120));
        header.addCell(new TableCell("Action", 120));

        vbox.getChildren().add(header.build());

        if (demandes.isEmpty()) {
            TableRow row = new TableRow(TableRowType.BODY);
            row.addCell(new TableCell("Aucune demande trouvée", 140));
            vbox.getChildren().add(row.build());
        } else {
            for (int i = 0; i < demandes.size(); i++) {
                int finalI = i;
                Demande_Conge demande = demandes.get(i);
                Employe employe = demande.getEmploye();
                long duration = ChronoUnit.DAYS.between(demande.getDateDebut(), demande.getDateFin()) + 1;
                long remainingLeaves = employe.getSoldeConges() - duration;

                TableRow row = new TableRow(TableRowType.BODY);
                row.addCell(new TableCell(employe.getNom() + " " + employe.getPrenom(), 200));
                row.addCell(new TableCell(demande.getTypeConge().name(), 120));
                row.addCell(new TableCell(demande.getDateDebut().toString(), 120));
                row.addCell(new TableCell(demande.getDateFin().toString(), 120));
                row.addCell(new TableCell(demande.getStatut().name(), 120));
                row.addCell(new TableCell(demande.getValidationCommentaire(), 200));
                row.addCell(new TableCell(String.valueOf(employe.getSoldeConges()), 120));
                row.addCell(new TableCell(String.valueOf(remainingLeaves), 120));

                if (remainingLeaves < 0) {
                    //row.setStyle("-fx-background-color: red;");
                }

                row.addAction("EDIT", "table-edit-btn", () -> {
                    Parent root = null;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandeConge/ModifierDemandeConge.fxml"));
                        root = loader.load();
                        ModifierDemandeCongeController controller = loader.getController();
                        controller.setDemande(demande);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    vbox.getScene().setRoot(root);
                });

                row.addAction("TRASH", "table-delete-btn", () -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette demande de congé ?");
                    alert.setContentText("Cette action est irréversible.");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            demandeCongeService.delete(demande);
                            reloadPage();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

                vbox.getChildren().add(row.build());
            }
        }
    }

    private void reloadPage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/DemandeConge/ListeDemandeConge.fxml"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        vbox.getScene().setRoot(root);
    }

    @FXML
    void sortByStartDate() {
        List<Demande_Conge> sortedDemandes = allDemandes.stream()
                .sorted(Comparator.comparing(Demande_Conge::getDateDebut))
                .collect(Collectors.toList());
        displayDemandes(sortedDemandes);
    }

    @FXML
    void sortByEndDate() {
        List<Demande_Conge> sortedDemandes = allDemandes.stream()
                .sorted(Comparator.comparing(Demande_Conge::getDateFin))
                .collect(Collectors.toList());
        displayDemandes(sortedDemandes);
    }

    @FXML
    void filterByDate() {
        LocalDate startDate = filterStartDate.getValue();
        LocalDate endDate = filterEndDate.getValue();
        List<Demande_Conge> filteredDemandes = allDemandes.stream()
                .filter(demande -> {
                    boolean matches = true;
                    if (startDate != null) {
                        matches = matches && !demande.getDateDebut().isBefore(startDate);
                    }
                    if (endDate != null) {
                        matches = matches && !demande.getDateFin().isAfter(endDate);
                    }
                    return matches;
                })
                .collect(Collectors.toList());
        displayDemandes(filteredDemandes);
    }

    @FXML
    void AjouterDemande() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/DemandeConge/AjouterDemandeConge.fxml"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        vbox.getScene().setRoot(root);
    }
}