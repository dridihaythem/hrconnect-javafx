package controllers.formations.quiz;

import controllers.formations.ModifierFormationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Formateur;
import models.Formation;
import models.Quiz;
import services.QuizService;
import utils.ShowMenu;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import utils.TableCell;
import utils.TableRow;
import utils.enums.QuizType;
import utils.enums.TableRowType;


public class ListeQuizController implements Initializable,ShowMenu {

    @FXML
    private AnchorPane menu;

    @FXML
    private Text foamtionTitle;

    @FXML
    private Button ajouterQuiz;

    @FXML
    private VBox vbox;


    private Formation formation;

    QuizService qz = new QuizService();


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

        TableRow header = new TableRow(TableRowType.HEADER);
        header.addCell(new TableCell("ID",50));
        header.addCell(new TableCell("Question",250));
        header.addCell(new TableCell("Type", 140));
        header.addCell(new TableCell("Response 1", 140));
        header.addCell(new TableCell("Response 2", 140));
        header.addCell(new TableCell("Response 3", 140));
        header.addCell(new TableCell("?", 140));


        vbox.getChildren().add(header.build());

        try {
            List<Quiz> quizs = qz.getAll();

            if(quizs.isEmpty()){
                TableRow row = new TableRow(TableRowType.BODY);
                row.addCell(new TableCell("Aucun question trouvée", 140));
                vbox.getChildren().add(row.build());
            }else{
                for(int i = 0;i<quizs.size();i++) {
                    int finalI = i;
                    TableRow row = new TableRow(TableRowType.BODY);

                    row.addCell(new TableCell(String.valueOf(quizs.get(i).getId()),50));
                    row.addCell(new TableCell(String.valueOf(quizs.get(i).getQuestion()),250));
                    String type;
                    if(quizs.get(i).getType() == QuizType.SINGLE) {
                        type = "Choix unique";
                    }else{
                        type = "Choix multiple";
                    }
                    row.addCell(new TableCell(String.valueOf(type),140));
                    row.addCell(new TableCell(String.valueOf(quizs.get(i).getReponse1()),140));
                    row.addCell(new TableCell(String.valueOf(quizs.get(i).getReponse2()),140));
                    row.addCell(new TableCell(String.valueOf(quizs.get(i).getReponse3()),140));


                    row.addAction("EDIT","table-edit-btn",()->{

                    });

                    row.addAction("TRASH","table-delete-btn",()->{

                    });

                    vbox.getChildren().add(row.build());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setFormation(Formation formation) throws SQLException {
        this.formation = formation;

        foamtionTitle.setText(this.formation.getTitle());
    }



}
