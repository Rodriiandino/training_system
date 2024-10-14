module training.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens training.system to javafx.fxml;
    exports training.system;
}