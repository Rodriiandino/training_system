package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class RoutineModal implements Initializable, IViewModal, Validator, IView {

    public TextField input_name;
    public TextField input_description;
    public ListView<Exercise> list_view;
    public Button btn_register;
    public Label text_error;
    public Label text_title;
    public HBox trainer_container;
    public ListView<User> list_clients;
    private RoutineViewController routineViewController;
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    private UserController userController;
    private User currentUser;
    private Routine routineToEdit;
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
            if (routineToEdit == null) {
                if (isCreateForClient) {
                    createForClient();
                } else {
                    create();
                }
            } else {
                edit();
            }
        });

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

    @Override
    public void create() {
        String name = input_name.getText();
        String description = input_description.getText().isEmpty() ? null : input_description.getText();
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Exercise> exercises = new HashSet<>(selectedItems);

        Routine routine = new Routine(name, description, currentUser, exercises);
        routineViewController.create(routine);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void edit() {
        String name = input_name.getText();
        String description = input_description.getText().isEmpty() ? null : input_description.getText();
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Exercise> exercises = new HashSet<>(selectedItems);

        Routine routine = new Routine(routineToEdit.getId(), name, description, currentUser, exercises);

        routineViewController.edit(routine);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void createForClient() {
        String name = input_name.getText();
        String description = input_description.getText().isEmpty() ? null : input_description.getText();
        ObservableList<Exercise> selectedItems = list_view.getSelectionModel().getSelectedItems();
        User client = list_clients.getSelectionModel().getSelectedItem();
        Set<Exercise> exercises = new HashSet<>(selectedItems);

        Routine routine = new Routine(name, description, client, currentUser, exercises);
        routineViewController.createForClient(routine);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    public void setRoutineToEdit(Routine routine) {
        this.routineToEdit = routine;
        input_name.setText(routine.getName());
        input_description.setText(routine.getDescription());

        updateModalMode();
    }

    public void setCreateForClient(boolean createForClient) {
        isCreateForClient = createForClient;

        updateModalMode();
    }

    @Override
    public void updateModalMode() {
        if (routineToEdit == null) {
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
        Validators.routineValidator(input_name, list_view, text_error, btn_register);
        if (isCreateForClient) {
            Validators.listValidator(list_clients, text_error, btn_register);
        }
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private void configureContainer(HBox container, boolean visible) {
        container.setVisible(visible);
        container.setManaged(visible);
    }
}