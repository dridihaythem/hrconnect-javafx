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

        for(int i=0;i<formations.size();i++){

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/components/FormationListItem.fxml"));
                Parent xmlContent = loader.load();

                ImageView imageView = (ImageView) loader.getNamespace().get("imageView");
                imageView.setImage(new Image(formations.get(i).getImage()));


                Label title = (Label) loader.getNamespace().get("title");
                title.setText(formations.get(i).getTitle());

                Label place = (Label) loader.getNamespace().get("place");
                if(formations.get(i).isIs_online()){
                    place.setText("En ligne");
                }else {
                    place.setText(formations.get(i).getPlace());
                }

                Label description = (Label) loader.getNamespace().get("description");
                title.setText(formations.get(i).getDescription());


                Label formateur = (Label) loader.getNamespace().get("formateur");
                formateur.setText(formations.get(i).getFormateur().getFirstName() + " " + formations.get(i).getFormateur().getLastName());

                Label StarDate = (Label) loader.getNamespace().get("startDate");
                StarDate.setText(formations.get(i).getStart_date().toString());


                vbox.getChildren().add(xmlContent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
