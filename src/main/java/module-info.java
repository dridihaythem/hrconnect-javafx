module com.melocode.rh_semi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.melocode.rh_semi to javafx.fxml;
    exports com.melocode.rh_semi;
}