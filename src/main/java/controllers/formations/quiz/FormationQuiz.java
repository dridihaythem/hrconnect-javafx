package controllers.formations.quiz;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Formation;
import models.Quiz;
import services.FormationService;
import services.QuizService;
import utils.ShowMenu;
import utils.enums.QuizType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormationQuiz implements Initializable, ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private VBox vbox;

    QuizService qs = new QuizService();

    Formation formation;

    int currentQuizIndex = 0;

    List<Boolean> reponsesResultats = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMenu(menu);

        vbox.setSpacing(20);

        if(formation != null){
            try {
                List<Quiz> quizzes = qs.getAll(formation.getId());
                if(currentQuizIndex == quizzes.size()){
                    Label label = new Label("Vous avez terminé le quiz");
                    label.setFont(new Font(20));
                    vbox.getChildren().add(label);

                    int score = 0;
                    for (Boolean reponseResultat : reponsesResultats) {
                        if(reponseResultat){
                            score++;
                        }
                    }

                    boolean passed = score >= (quizzes.size() +1) / 2;


                    FontAwesomeIconView icon = new FontAwesomeIconView();
                    icon.setGlyphName(passed ? FontAwesomeIcon.CHECK_CIRCLE.name() : FontAwesomeIcon.TIMES_CIRCLE.name());
                    icon.setSize("200");
                    icon.setFill(passed ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);

                    vbox.getChildren().add(icon);

                    Label scoreLabel = new Label("Votre score est : " + score + "/" + quizzes.size());
                    scoreLabel.setFont(new Font(40));


                    if(passed) {
                        scoreLabel.setStyle("-fx-text-fill: green");
                    }else{
                        scoreLabel.setStyle("-fx-text-fill: red");
                    }

                    vbox.getChildren().add(scoreLabel);

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


                    MFXButton next = new MFXButton("Suivant");
                    next.setMinWidth(200);
                    next.getStyleClass().add("save-btn");
                    vbox.getChildren().add(next);

                    next.setOnAction(event -> {

                        Toggle selectedToggle = group.getSelectedToggle();

                        RadioButton selectedRadioButton = (RadioButton) selectedToggle;

                        String selected = selectedRadioButton.getText();

                        int numReponseChoisie = 0;
                        if(selected.equals(quiz.getReponse1())) {
                            numReponseChoisie = 1;
                        }else if(selected.equals(quiz.getReponse2())) {
                            numReponseChoisie = 2;
                        }else{
                            numReponseChoisie = 3;
                        }

                        if(numReponseChoisie == quiz.getNumRepCorrect()) {
                            reponsesResultats.add(true);
                        }else{
                            reponsesResultats.add(false);
                        }

                        try {
                            qs.enregisterReponse(18, quiz.getId(), numReponseChoisie);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

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
