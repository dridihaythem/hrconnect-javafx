package controllers.DemandeConge;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Demande_Conge;
import services.DemandeCongeService;
import utils.ShowMenu;
import utils.TableCell;
import utils.TableRow;
import utils.enums.TableRowType;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeDemandeCongeController implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    DemandeCongeService demandeCongeService = new DemandeCongeService();

    @FXML
    private VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);

        TableRow header = new TableRow(TableRowType.HEADER);

        header.addCell(new TableCell("Employé", 250));
        header.addCell(new TableCell("Type de congé", 140));
        header.addCell(new TableCell("Date de début", 140));
        header.addCell(new TableCell("Date de fin", 140));
        header.addCell(new TableCell("Statut", 140));
        header.addCell(new TableCell("Commentaire", 250));
        header.addCell(new TableCell("Action", 140));

        vbox.getChildren().add(header.build());

        try {
            List<Demande_Conge> demandes = demandeCongeService.getAll();

            if (demandes.isEmpty()) {
                TableRow row = new TableRow(TableRowType.BODY);
                row.addCell(new TableCell("Aucune demande trouvée", 140));
                vbox.getChildren().add(row.build());
            } else {
                for (int i = 0; i < demandes.size(); i++) {
                    int finalI = i;
                    TableRow row = new TableRow(TableRowType.BODY);
                    row.addCell(new TableCell(String.valueOf(demandes.get(i).getEmploye().getNom() + " " + demandes.get(i).getEmploye().getPrenom()), 250));
                    row.addCell(new TableCell(demandes.get(i).getTypeConge().name(), 140));
                    row.addCell(new TableCell(String.valueOf(demandes.get(i).getDateDebut()), 140));
                    row.addCell(new TableCell(String.valueOf(demandes.get(i).getDateFin()), 140));
                    row.addCell(new TableCell(demandes.get(i).getStatut().name(), 140));
                    row.addCell(new TableCell(demandes.get(i).getValidationCommentaire(), 250));

                    row.addAction("EDIT", "table-edit-btn", () -> {
                        Parent root = null;
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandeConge/ModifierDemandeConge.fxml"));
                            root = loader.load();
                            ModifierDemandeCongeController controller = loader.getController();
                            controller.setDemande(demandes.get(finalI));
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
                        if (result.get() == ButtonType.OK) {
                            try {
                                demandeCongeService.delete(demandes.get(finalI));
                                vbox.getChildren().clear();
                                initialize(location, resources);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    });

                    vbox.getChildren().add(row.build());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
