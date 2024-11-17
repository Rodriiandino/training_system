module training.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens training.system to javafx.fxml;
    opens training.system.viewController to javafx.fxml;
    exports training.system;
    exports training.system.core.domain.user;
    exports training.system.core.domain.routine;
    exports training.system.core.domain.exercise;
    exports training.system.core.domain.note;
    exports training.system.core.domain.gym;
    exports training.system.core.domain.category;
    exports training.system.core.domain.progress;
    exports training.system.viewController;
    exports training.system.utils;
    exports training.system.viewController.interfaces;
    opens training.system.viewController.interfaces to javafx.fxml;
    exports training.system.viewController.modals;
    opens training.system.viewController.modals to javafx.fxml;
}