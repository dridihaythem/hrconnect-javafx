package utils;

import javafx.scene.control.Label;

public class TableCell {
    String text;
    int minWidth;

    public TableCell(String text, int minWidth) {
        this.text = text;
        this.minWidth = minWidth;
    }

    public  Label build() {
        Label label = new Label(text);
        label.setMinWidth(minWidth);
        return label;
    }
}
