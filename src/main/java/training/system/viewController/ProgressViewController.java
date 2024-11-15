package training.system.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.progress.Progress;
import training.system.core.domain.progress.ProgressController;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

public class ProgressViewController implements Initializable, IView, IViewControllerManipulation {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView<Progress> table;
    public Button btn_edit;
    public Button btn_create;
    public Button btn_out;
    private SessionManager sessionManager;
    ProgressController progressController;
    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());

        setupListeners();
    }

    public void setupListeners() {
        progressController = new ProgressController();
        createColumn();
        list();

        user_section.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/profile-view.fxml", user_section);
        });
        exercise_section.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/exercise-view.fxml", exercise_section);
        });

        progress_section.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/progress-view.fxml", progress_section);
        });

        note_section.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/notes-view.fxml", note_section);
        });

        routine_section.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/routine-view.fxml", routine_section);
        });

        btn_out.setOnAction(e -> {
            sessionManager.closeSession();
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/login-view.fxml", btn_out);
        });
    }

    @Override
    public void createColumn() {
        TableColumn<Progress, Date> dateColumn = new TableColumn<>("Fecha de Progreso");
        TableColumn<Progress, Integer> repetitionsColumn = new TableColumn<>("Repeticiones");
        TableColumn<Progress, Integer> weightColumn = new TableColumn<>("Peso");
        TableColumn<Progress, Integer> timeColumn = new TableColumn<>("Tiempo");
        TableColumn<Progress, User> trainerColumn = new TableColumn<>("Entrenador");
        TableColumn<Progress, Exercise> exerciseColumn = new TableColumn<>("Ejercicio");

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("progressDate"));
        repetitionsColumn.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        exerciseColumn.setCellValueFactory(new PropertyValueFactory<>("exercise"));

        exerciseColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Exercise exercise, boolean empty) {
                super.updateItem(exercise, empty);
                if (empty || exercise == null) {
                    setText(null);
                } else {
                    setText(exercise.getName());
                }
            }
        });

        trainerColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(User trainer, boolean empty) {
                super.updateItem(trainer, empty);
                if (empty) {
                    setText(null);
                } else if (trainer == null) {
                    setText("Sin entrenador");
                } else {
                    setText(trainer.getName());
                }
            }
        });

        table.getColumns().addAll(dateColumn, repetitionsColumn, weightColumn, timeColumn, trainerColumn, exerciseColumn);
    }


    @Override
    public void edit() {

    }

    @Override
    public void create() {

    }

    @Override
    public void list() {
        ObservableList<Progress> progresses = FXCollections.observableArrayList();

        try {
            Set<Progress> progressSet = progressController.listUserProgress(currentUser);
            progresses.addAll(progressSet);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        table.setItems(progresses);
    }
}
