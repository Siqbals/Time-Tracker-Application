module com.cmpt370.timetracker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.cmpt370.timetracker to javafx.fxml;
    exports com.cmpt370.timetracker;
}