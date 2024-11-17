package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.progress.Progress;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
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

public class ProgressModal implements Initializable, IViewModal, Validator, IView {

    public TextField input_repe;
    public TextField input_weight;
    public TextField input_time;
    public DatePicker input_date;
    public Button btn_register;
    public Label text_error;
    public ListView<Exercise> list_view;
    public ProgressViewController progressViewController;
    public Label text_title;
    public HBox trainer_container;
    public ListView<User> list_clients;
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    private UserController userController;
    private User currentUser;
    private Progress progressToEdit;
    private boolean isCreateForClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        userController = new UserController();
        configureContainer(trainer_container, false);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        validateFields();
        addValidators();
        exerciseController = new ExerciseController();
        btn_register.setOnAction(event -> {
            if (progressToEdit == null) {
                if (isCreateForClient) {
                    createForClient();
                } else {
                    create();
                }
            } else {
                edit();
            }
        });

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

    @Override
    public void create() {
        int repe = input_repe.getText().isEmpty() ? 0 : Integer.parseInt(input_repe.getText());
        int weight = input_weight.getText().isEmpty() ? 0 : Integer.parseInt(input_weight.getText());
        int time = input_time.getText().isEmpty() ? 0 : Integer.parseInt(input_time.getText());
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Exercise exercise = list_view.getSelectionModel().getSelectedItem();

        Progress progress = new Progress(date, repe, weight, time, currentUser, exercise);

        progressViewController.create(progress);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void edit() {
        int inputRepe = input_repe.getText().isEmpty() ? 0 : Integer.parseInt(input_repe.getText());
        int inputWeight = input_weight.getText().isEmpty() ? 0 : Integer.parseInt(input_weight.getText());
        int inputTime = input_time.getText().isEmpty() ? 0 : Integer.parseInt(input_time.getText());
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Exercise exercise = list_view.getSelectionModel().getSelectedItem();

        Progress progress = new Progress(progressToEdit.getId(), date, inputRepe, inputWeight, inputTime, progressToEdit.getUser(), exercise);

        progressViewController.edit(progress);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void createForClient() {
        int inputRepe = input_repe.getText().isEmpty() ? 0 : Integer.parseInt(input_repe.getText());
        int inputWeight = input_weight.getText().isEmpty() ? 0 : Integer.parseInt(input_weight.getText());
        int inputTime = input_time.getText().isEmpty() ? 0 : Integer.parseInt(input_time.getText());
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        User client = list_clients.getSelectionModel().getSelectedItem();
        Exercise exercise = list_view.getSelectionModel().getSelectedItem();

        Progress progress = new Progress(date, inputRepe, inputWeight, inputTime, client, currentUser, exercise);

        progressViewController.createForClient(progress);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    public void setProgressToEdit(Progress progress) {
        this.progressToEdit = progress;
        input_repe.setText(String.valueOf(progress.getRepetitions()));
        input_weight.setText(String.valueOf(progress.getWeight()));
        input_time.setText(String.valueOf(progress.getTime()));

        updateModalMode();
    }

    public void setCreateForClient(boolean createForClient) {
        isCreateForClient = createForClient;

        updateModalMode();
    }

    @Override
    public void updateModalMode() {
        if (progressToEdit == null) {
            if (isCreateForClient) {
                text_title.setText("Crear Rutina para Cliente");
                configureContainer(trainer_container, true);
                Set<User> clients = null;

                try {
                    clients = userController.listClients(currentUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (clients != null) {
                    clients.forEach(client -> list_clients.getItems().add(client));
                    list_clients.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(User item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null || item.getEmail() == null) {
                                setText(null);
                            } else {
                                setText(item.getEmail());
                            }
                        }
                    });
                }
            } else {
                text_title.setText("Crear Rutina");
            }
        } else {
            text_title.setText("Editar Rutina");
        }
    }

    @Override
    public void validateFields() {
        Validators.progressValidator(list_view, input_date, text_error, btn_register);
        if (isCreateForClient) {
            Validators.listValidator(list_clients, text_error, btn_register);
        }
    }

    @Override
    public void addValidators() {
        input_date.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private void configureContainer(HBox container, boolean visible) {
        container.setVisible(visible);
        container.setManaged(visible);
    }
}

