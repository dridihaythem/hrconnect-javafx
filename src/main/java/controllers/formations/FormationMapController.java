package controllers.formations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Formateur;
import models.Formation;
import utils.ConfigReader;
import utils.ShowMenu;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class FormationMapController implements ShowMenu, Initializable {

    @FXML
    private AnchorPane menu;


    @FXML
    private ImageView mapImage;

    Formation formation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);
        if(formation != null) {
            String url = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=" + formation.getLat() + "," + formation.getLng() +
                    "&zoom=15&size=600x400" +
                    "&markers=color:red%7Clabel:A%7C" + formation.getLat() + "," + formation.getLng() +
                    "&key=" + ConfigReader.get("HAYTHEM_GOOGLE_MAPS");

            System.out.println(url);
            mapImage.setImage(new Image(url));
        }
    }

    public void setFormation(Formation formation) throws SQLException {
        this.formation = formation;
        initialize(null, null);
    }
}
