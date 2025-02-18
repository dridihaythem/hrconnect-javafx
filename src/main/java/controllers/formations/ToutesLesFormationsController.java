package controllers.formations;

import controllers.formations.quiz.ListeQuizController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Formation;
import services.FormationService;
import utils.ShowMenu;
import utils.TableCell;
import utils.TableRow;
import utils.enums.TableRowType;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ToutesLesFormationsController implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private VBox vbox;

    FormationService fs = new FormationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);

        vbox.setSpacing(10);

        List<Formation> formations = null;
        try {
            formations = fs.getAllFormationsForUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(formations.size() == 0){
            Label label = new Label("Aucune formation disponible pour vous");
            label.setFont(new Font(20));
            vbox.getChildren().add(label);
        }else{

            for(int i=0;i<formations.size();i++){

                final Formation formation = formations.get(i);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/components/FormationListItem.fxml"));
                    Parent xmlContent = loader.load();

                    ImageView imageView = (ImageView) loader.getNamespace().get("imageView");
                    imageView.setImage(new Image(formation.getImage()));


                    Label title = (Label) loader.getNamespace().get("title");
                    title.setText(formation.getTitle());

                    Label place = (Label) loader.getNamespace().get("place");
                    if(formation.isIs_online()){
                        place.setText("En ligne");
                    }else {
                        place.setText(formation.getPlace());
                    }

                    Label description = (Label) loader.getNamespace().get("description");
                    title.setText(formation.getDescription());


                    Label formateur = (Label) loader.getNamespace().get("formateur");
                    formateur.setText(formation.getFormateur().getFirstName() + " " + formation.getFormateur().getLastName());

                    Label StarDate = (Label) loader.getNamespace().get("startDate");
                    StarDate.setText(formation.getStart_date().toString());

                    MFXButton inscrireButton = (MFXButton) loader.getNamespace().get("inscrireBtn");
                    inscrireButton.setOnAction(event -> {
                        try {
                            fs.participerFormation(formation.getId(),18);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Inscription");
                            alert.setHeaderText("Inscription réussie");
                            alert.setContentText("Vous êtes inscrit à la formation " + formation.getTitle());
                            alert.showAndWait();

                            vbox.getChildren().clear();
                            initialize(location, resources);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });


                    vbox.getChildren().add(xmlContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
