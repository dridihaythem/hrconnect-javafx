module com.melocode.semin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.melocode.semin to javafx.fxml;
    opens com.melocode.semin.controllers to javafx.fxml;
    opens com.melocode.semin.models to javafx.base;

    exports com.melocode.semin;
    exports com.melocode.semin.controllers;
}
