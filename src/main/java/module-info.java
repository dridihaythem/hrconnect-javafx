module com.melocode.hrreclam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.melocode.hrreclam to javafx.fxml;
    exports com.melocode.hrreclam;
}