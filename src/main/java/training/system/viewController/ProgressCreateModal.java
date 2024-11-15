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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ProgressCreateModal implements Initializable, IViewModal, Validator {

    public TextField input_repe;
    public TextField input_weight;
    public TextField input_time;
    public DatePicker input_date;
    public Button btn_register;
    public Label text_error;
    public ListView<Exercise> list_view;
    ProgressViewController progressViewController;
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
        this.progressViewController = (ProgressViewController) controller;
    }

    private void create() {
        int inputRepe = input_repe.getText().isEmpty() ? 0 : Integer.parseInt(input_repe.getText());
        int inputWeight = input_weight.getText().isEmpty() ? 0 : Integer.parseInt(input_weight.getText());
        int inputTime = input_time.getText().isEmpty() ? 0 : Integer.parseInt(input_time.getText());
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Exercise> exercises = new HashSet<>(selectedItems);
        Exercise exercise = exercises.iterator().next();

        progressViewController.create(inputRepe, inputWeight, inputTime, date, exercise);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void validateFields() {
        Validators.createProgressValidator(list_view, input_date, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        input_date.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}

