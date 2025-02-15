package utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import utils.enums.TableControlBtn;
import utils.enums.TableRowType;

import java.util.ArrayList;
import java.util.List;


public class TableRow {
    TableRowType type;
    String classe;
    List<TableCell> cells;
    Runnable onDelete;
    Runnable onEdit;

    public TableRow(TableRowType type) {
        this.type = type;
        this.cells = new ArrayList();
    }

    public TableRow(TableRowType type,Runnable onEdit,Runnable onDelete) {
        this(type);
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    public void addCell(TableCell row) {
        cells.add(row);
    }

    public  HBox build(){
        HBox hBox = new HBox();
        hBox.getStyleClass().add(type == TableRowType.HEADER ? "table-header" : "table-body");
        for (TableCell cell : cells) {
            Label lb = cell.build();
            if(type == TableRowType.HEADER) {
                lb.getStyleClass().add("table-header-label");
            }else{
                lb.getStyleClass().add("table-body-label");
            }
            hBox.getChildren().add(lb);
        }
        if(type == TableRowType.BODY) {
            hBox.getChildren().add(buildControlBtn(TableControlBtn.EDIT));
            hBox.getChildren().add(buildControlBtn(TableControlBtn.DELETE));
        }
        return hBox;
    }

    private HBox buildControlBtn(TableControlBtn type){
        HBox container = new HBox();
        if(type == TableControlBtn.DELETE) {
            container.getStyleClass().add("table-delete-btn");
        }else {
            container.getStyleClass().add("table-edit-btn");
        }

        FontAwesomeIconView icon = new FontAwesomeIconView();
        if(type == TableControlBtn.DELETE) {
            icon.setGlyphName("TRASH");
        }else {
            icon.setGlyphName("EDIT");
        }
        icon.setSize("20");
        icon.setFill(javafx.scene.paint.Color.WHITE);

        icon.setOnMouseClicked(event -> {
            if(type == TableControlBtn.EDIT){
                onEdit.run();
            }else if(type == TableControlBtn.DELETE){
                onDelete.run();
            }
        });
        container.getChildren().add(icon);
        return container;
    }
}
