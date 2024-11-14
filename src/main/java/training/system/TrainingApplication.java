package training.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TrainingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/training/system/view/login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setResizable(false);
        stage.setTitle("Sistema de Gestión de Entrenamientos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


