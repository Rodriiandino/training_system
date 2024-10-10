module training.system {
    requires javafx.controls;
    requires javafx.fxml;


    opens training.system to javafx.fxml;
    exports training.system;
}