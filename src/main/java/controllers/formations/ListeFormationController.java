package controllers.formations;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import models.Formation;
import services.FormationService;
import utils.TableCell;
import utils.TableRow;
import utils.enums.TableRowType;

public class ListeFormationController implements Initializable {

    FormationService fs = new FormationService();

    @FXML
    private VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableRow header = new TableRow(TableRowType.HEADER);
        header.addCell(new TableCell("ID",50));
        header.addCell(new TableCell("Titre",250));
        header.addCell(new TableCell("Lieu", 150));
        header.addCell(new TableCell("Disponible pour", 150));
        header.addCell(new TableCell("Date de début", 150));
        header.addCell(new TableCell("Date de fin", 150));
        header.addCell(new TableCell("?", 150));


        vbox.getChildren().add(header.build());

        try {
            List<Formation> formations = fs.getAll();


           for(int i = 0;i<formations.size();i++) {
               int finalI = i;
               TableRow row = new TableRow(TableRowType.BODY,()->{
                   System.out.println("Edit");
               },()->{
                 try{
                     fs.delete(formations.get(finalI).getId());
                     vbox.getChildren().clear();
                     initialize(location,resources);
                 }catch (Exception e){
                     System.out.println(e);
                 }
               });

               row.addCell(new TableCell(String.valueOf(formations.get(i).getId()),50));
               row.addCell(new TableCell(String.valueOf(formations.get(i).getTitle()),250));
               row.addCell(new TableCell(formations.get(i).getPlace() != null  ?  formations.get(i).getPlace() : "En ligne",150));

               String dispoPour = "";
               if(formations.get(i).isAvailable_for_employee() && formations.get(i).isAvailable_for_intern()){
                   dispoPour = "Employés/Stagaires";
               }else if(formations.get(i).isAvailable_for_employee()){
                   dispoPour = "Employes";
               }else if(formations.get(i).isAvailable_for_intern()){
                   dispoPour = "Stagaires";
               }
               row.addCell(new TableCell(dispoPour,150));
               row.addCell(new TableCell(String.valueOf(formations.get(i).getStart_date()),150));
               row.addCell(new TableCell(String.valueOf(formations.get(i).getEnd_date()),150));

               vbox.getChildren().add(row.build());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
