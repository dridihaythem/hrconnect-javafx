package controllers.formations.quiz;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Formation;
import models.Quiz;
import models.QuizReponse;
import services.FormationService;
import services.QuizService;
import utils.ConfigReader;
import utils.ShowMenu;
import utils.enums.QuizType;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class FormationQuizResult implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private VBox vbox;

    QuizService qs = new QuizService();

    Formation formation;

    int currentQuizIndex = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);

        vbox.setSpacing(20);

        if(formation != null){
            try {
                List<Quiz> quizzes = qs.getAll(formation.getId());
                List<QuizReponse> reponses = qs.getUserReponses(formation.getId(),18);

                if(currentQuizIndex == quizzes.size()){
                    Label label = new Label("Vous avez terminé le quiz");
                    label.setFont(new Font(20));
                    vbox.getChildren().add(label);

                    vbox.setAlignment(javafx.geometry.Pos.CENTER);
                }else{

                    Quiz quiz = quizzes.get(currentQuizIndex);

                    Label question = new Label(quiz.getQuestion());
                    question.setFont(new Font("Arial", 20));
                    vbox.getChildren().add(question);

                    ToggleGroup group = new ToggleGroup();
                    RadioButton reponse1 = new RadioButton(quiz.getReponse1());
                    reponse1.setFont(new Font("Arial", 15));
                    reponse1.setToggleGroup(group);
                    vbox.getChildren().add(reponse1);
                    RadioButton reponse2 = new RadioButton(quiz.getReponse2());
                    reponse2.setFont(new Font("Arial", 15));
                    reponse2.setToggleGroup(group);
                    vbox.getChildren().add(reponse2);
                    RadioButton reponse3 = new RadioButton(quiz.getReponse3());
                    reponse3.setFont(new Font("Arial", 15));
                    reponse3.setToggleGroup(group);
                    vbox.getChildren().add(reponse3);


                  /*  if(quiz.getNumRepCorrect() == 1){
                        reponse1.setStyle("-fx-background-color: green;");
                    }else if(quiz.getNumRepCorrect() == 2) {
                        reponse1.setStyle("-fx-background-color: green;");
                    }else{
                        reponse1.setStyle("-fx-background-color: green;");
                    }*/

                    // cocher les reponses du l'utilisateur
                    if (reponses.stream().anyMatch(r -> r.getQuiz_id() == quiz.getId())) {
                        QuizReponse reponse = reponses.stream().filter(r -> r.getQuiz_id() == quiz.getId()).findFirst().get();
                        if (reponse.getNum_reponse() == 1) {
                            reponse1.setSelected(true);
                            if (quiz.getNumRepCorrect() != 1) {
                                reponse1.setStyle("-fx-background-color: red !important;");
                            }
                        } else if (reponse.getNum_reponse() == 2) {
                            reponse2.setSelected(true);
                            if (quiz.getNumRepCorrect() != 2) {
                                reponse2.setStyle("-fx-background-color: red !important;");
                            }
                        } else {
                            reponse3.setSelected(true);
                            if (quiz.getNumRepCorrect() != 3) {
                                reponse3.setStyle("-fx-background-color: red; !important;");
                            }
                        }
                    }




                    MFXButton next = new MFXButton("Suivant");
                    next.setMinWidth(200);
                    next.getStyleClass().add("save-btn");
                    vbox.getChildren().add(next);

                    next.setOnAction(event -> {
                        currentQuizIndex++;
                        vbox.getChildren().clear();
                        initialize(null,null);
                    });

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }



    public void setFormation(Formation formation) throws SQLException {
        this.formation = formation;
        initialize(null,null);
    }
}
