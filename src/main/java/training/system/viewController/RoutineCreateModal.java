package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.user.User;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class RoutineCreateModal implements Initializable, IViewModal, Validator {

    public TextField input_name;
    public TextField input_description;
    public ListView<Exercise> list_view;
    public Button btn_register;
    public Label text_error;
    RoutineViewController routineViewController;
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    User currentUser;

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
        btn_register.setOnAction(e -> create());
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

    @Override
    public <T> void setParentController(T controller) {
        this.routineViewController = (RoutineViewController) controller;
    }

    private void create() {
        String name = input_name.getText();
        String description = input_description.getText().isEmpty() ? null : input_description.getText();
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Exercise> exercises = new HashSet<>(selectedItems);

        routineViewController.create(name, description, exercises);
        ((Stage) btn_register.getScene().getWindow()).close();
    }


    @Override
    public void validateFields() {
        Validators.createRoutineValidator(input_name, list_view, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}