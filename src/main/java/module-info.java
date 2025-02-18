module pidev3a21 {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;

    opens Controllers to javafx.fxml; // Ouvrir le package Controllers
    exports Controllers; // Exporter le package Controllers

    opens org.example to javafx.fxml, javafx.graphics; // Ouvrir le package org.example
    exports org.example; // Exporter le package org.example
}