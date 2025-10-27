module com.tp.accessguard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.tp.accessguard.controller to javafx.fxml;
    opens com.tp.accessguard.model to javafx.base;

    exports com.tp.accessguard;
}