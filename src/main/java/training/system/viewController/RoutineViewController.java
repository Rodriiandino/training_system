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
import training.system.core.domain.routine.Routine;
import training.system.core.domain.routine.RoutineController;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class RoutineViewController implements Initializable, IView, IViewControllerManipulation {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView<Routine> table;
    public Button btn_create;
    public Button btn_edit;
    public Button btn_out;
    private SessionManager sessionManager;
    private RoutineController routineController;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());

        setupListeners();
    }

    public void setupListeners() {
        routineController = new RoutineController();
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
        TableColumn<Routine, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Routine, String> descriptionColumn = new TableColumn<>("Descripción");
        TableColumn<Routine, User> trainerColumn = new TableColumn<>("Entrenador");
        TableColumn<Routine, Set<Exercise>> exercisesColumn = new TableColumn<>("Ejercicios");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        exercisesColumn.setCellValueFactory(new PropertyValueFactory<>("exercises"));

        exercisesColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Set<Exercise> exercises, boolean empty) {
                super.updateItem(exercises, empty);
                if (empty) {
                    setText(null);
                } else if (exercises == null) {
                    setText("Sin ejercicios");
                } else {
                    StringBuilder exercisesString = new StringBuilder();
                    for (Exercise exercise : exercises) {
                        exercisesString.append(exercise.getName()).append(", ");
                    }
                    setText(exercisesString.toString());
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

        table.getColumns().addAll(nameColumn, descriptionColumn, trainerColumn, exercisesColumn);
    }


    @Override
    public void edit() {

    }

    @Override
    public void create() {

    }

    @Override
    public void list() {
        ObservableList<Routine> routines = FXCollections.observableArrayList();

        try {
            Set<Routine> routineSet = routineController.listUserRoutines(currentUser);
            routines.addAll(routineSet);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        table.setItems(routines);
    }
}