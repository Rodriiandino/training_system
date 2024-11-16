package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.user.User;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class RoutineEditModal implements Initializable, IViewModal, Validator {
    public TextField input_name;
    public TextField input_description;
    public ListView<Exercise> list_view;
    public Button btn_edit;
    public Label text_error;
    private RoutineViewController routineViewController;
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    private User currentUser;
    private Routine routineToEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        setupListeners();
    }

    private void setupListeners() {
        validateFields();
        addValidators();
        exerciseController = new ExerciseController();
        btn_edit.setOnAction(e -> edit());
        list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Set<Exercise> exercises = null;

        try {
            exercises = exerciseController.listUserExercises(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exercises != null) {
            exercises.forEach(exercise -> list_view.getItems().add(exercise));
            list_view.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Exercise item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getName() == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
        }
    }

    public void setRoutineToEdit(Routine routine) {
        this.routineToEdit = routine;
        input_name.setText(routine.getName());
        input_description.setText(routine.getDescription());
    }

    private void edit() {
        String name = input_name.getText();
        String description = input_description.getText().isEmpty() ? null : input_description.getText();
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Exercise> exercises = new HashSet<>(selectedItems);

        Routine routine = new Routine(routineToEdit.getId(), name, description, routineToEdit.getTrainer(), exercises);

        routineViewController.edit(routine);
        ((Stage) btn_edit.getScene().getWindow()).close();
    }

    @Override
    public <T> void setParentController(T controller) {
        this.routineViewController = (RoutineViewController) controller;
    }

    @Override
    public void validateFields() {
        Validators.routineValidator(input_name, list_view, text_error, btn_edit);
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}
