package controllers.formations.quiz;

import exceptions.InvalidInputException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import models.Formation;
import models.Quiz;
import services.QuizService;
import utils.ShowMenu;
import utils.enums.QuizType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierQuizController implements Initializable, ShowMenu  {

    QuizService qs = new QuizService();

    @FXML
    private MFXButton cancelBtn;

    @FXML
    private AnchorPane menu;

    @FXML
    private MFXButton saveBtn;

    @FXML
    private MFXTextField reponse1;

    @FXML
    private MFXTextField reponse2;

    @FXML
    private MFXTextField reponse3;

    @FXML
    private MFXTextField title;

    @FXML
    private MFXRadioButton ChoixMultiple;

    @FXML
    private ToggleGroup QuestionType;

    @FXML
    private MFXRadioButton SimpleChoix;


    @FXML
    void OnClickCancelBtn() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/quiz/ListeQuiz.fxml"));
            root = loader.load();
            ListeQuizController controller = loader.getController();
            controller.setFormation(formation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cancelBtn.getScene().setRoot(root);
    }

    @FXML
    void onSave() {
        try {
            if (title.getText().isEmpty()) {
                throw new InvalidInputException("Le titre est requis");
            } else if (reponse1.getText().isEmpty()) {
                throw new InvalidInputException("Ajouter au mois une reponse possible");
            }


            quiz.setQuestion(title.getText());
            quiz.setReponse1(reponse1.getText());
            quiz.setReponse2(reponse2.getText());
            quiz.setReponse3(reponse3.getText());

            if (ChoixMultiple.isSelected()) {
                quiz.setType(QuizType.MULTIPLE);
            } else {
                quiz.setType(QuizType.SINGLE);
            }

            qs.update(quiz);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Quiz modifié avec succès");
            alert.showAndWait();

            //redirect to list
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/formations/quiz/ListeQuiz.fxml"));
                root = loader.load();
                ListeQuizController controller = loader.getController();
                controller.setFormation(formation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cancelBtn.getScene().setRoot(root);

        } catch (InvalidInputException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("An error has occured");
            alert.showAndWait();
        }

    }



    private Formation formation;
    private Quiz quiz;

    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);
    }

    public void setData(Formation formation,Quiz quiz) throws SQLException {
        this.formation = formation;
        this.quiz = quiz;

        title.setText(quiz.getQuestion());
        reponse1.setText(quiz.getReponse1());
        reponse2.setText(quiz.getReponse2());
        reponse3.setText(quiz.getReponse3());

        if(quiz.getType().equals(QuizType.MULTIPLE)) {
            ChoixMultiple.setSelected(true);
            SimpleChoix.setSelected(false);
        }else{
            SimpleChoix.setSelected(true);
            ChoixMultiple.setSelected(false);
        }

    }

}
