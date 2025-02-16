package controllers.formations.quiz;

import controllers.formations.ModifierFormationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import models.Formateur;
import models.Formation;
import utils.ShowMenu;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.scene.control.Button;


public class ListeQuizController implements Initializable,ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private Button ajouterQuiz;

    private Formation formation;


    @FXML
    void ajouterQuestion() {
        System.out.println("hello");
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/quiz/AjouterQuiz.fxml"));
            root = loader.load();
            AjouterQuizController controller = loader.getController();
            controller.setFormation(formation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ajouterQuiz.getScene().setRoot(root);
    }

    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);
    }

    public void setFormation(Formation formation) throws SQLException {
        this.formation = formation;
    }



}
