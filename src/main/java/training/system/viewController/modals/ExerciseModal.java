package training.system.viewController.modals;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import training.system.core.domain.category.Category;
import training.system.core.domain.category.CategoryController;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;
import training.system.viewController.ExerciseViewController;
import training.system.viewController.interfaces.IView;
import training.system.viewController.interfaces.IViewModal;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ExerciseModal implements Initializable, IViewModal, Validator, IView {
    public TextField input_name;
    public TextField input_description;
    public TextField input_explication;
    public TextField input_url;
    public ListView<Category> list_view;
    public Button btn_register;
    public Label text_error;
    public Label text_title;
    public HBox trainer_container;
    public ListView<User> list_clients;
    private ExerciseViewController exerciseViewController;
    private UserController userController;
    private CategoryController categoryController;
    private Exercise exerciseToEdit = null;
    private SessionManager sessionManager;
    User currentUser;
    private boolean isCreateForClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        categoryController = new CategoryController();
        userController = new UserController();
        configureContainer(trainer_container, false);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        validateFields();
        addValidators();

        btn_register.setOnAction(event -> {
            if (exerciseToEdit == null) {
                if (isCreateForClient) {
                    createForClient();
                } else {
                    create();
                }
            } else {
                edit();
            }
        });

        Set<Category> categories = null;

        try {
            categories = categoryController.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        categories.forEach(category -> list_view.getItems().add(category));
        list_view.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @Override
    public <T> void setParentController(T controller) {
        this.exerciseViewController = (ExerciseViewController) controller;
    }

    @Override
    public void create() {
        String name = input_name.getText().toLowerCase();
        String description = input_description.getText().toLowerCase();
        String explication = input_explication.getText().toLowerCase();
        String url = input_url.getText().isEmpty() ? null : input_url.getText().toLowerCase();
        ObservableList<Category> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Category> categories = new HashSet<>(selectedItems);

        Exercise exercise = new Exercise(name, description, explication, url, false, categories, currentUser);

        exerciseViewController.create(exercise);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void edit() {
        String name = input_name.getText().toLowerCase();
        String description = input_description.getText().toLowerCase();
        String explication = input_explication.getText().toLowerCase();
        String url = input_url.getText().isEmpty() ? null : input_url.getText().toLowerCase();
        ObservableList<Category> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Category> categories = new HashSet<>(selectedItems);

        Exercise exercise = new Exercise(exerciseToEdit.getId(), name, description, explication, url, false, categories, exerciseToEdit.getUser());
        exerciseViewController.edit(exercise);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void createForClient() {
        String name = input_name.getText().toLowerCase();
        String description = input_description.getText().toLowerCase();
        String explication = input_explication.getText().toLowerCase();
        String url = input_url.getText().isEmpty() ? null : input_url.getText().toLowerCase();
        ObservableList<Category> selectedItems = list_view.getSelectionModel().getSelectedItems();
        User client = list_clients.getSelectionModel().getSelectedItem();

        Set<Category> categories = new HashSet<>(selectedItems);

        Exercise exercise = new Exercise(name, description, explication, url, false, categories, client, currentUser);

        exerciseViewController.createForClient(exercise);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    public void setExerciseToEdit(Exercise exerciseToEdit) {
        this.exerciseToEdit = exerciseToEdit;
        input_name.setText(exerciseToEdit.getName());
        input_description.setText(exerciseToEdit.getDescription());
        input_explication.setText(exerciseToEdit.getExplanation());
        if (exerciseToEdit.getVideoUrl() != null) {
            input_url.setText(exerciseToEdit.getVideoUrl());
        }
        if (exerciseToEdit.getCategories() == null) {
            return;
        }
        list_view.getItems().forEach(category -> {
            if (exerciseToEdit.getCategories().contains(category)) {
                list_view.getSelectionModel().select(category);
            }
        });

        updateModalMode();
    }

    public void setCreateForClient(boolean createForClient) {
        isCreateForClient = createForClient;

        updateModalMode();
    }

    @Override
    public void updateModalMode() {
        if (exerciseToEdit == null) {
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
        Validators.exerciseValidator(input_name, input_description, input_explication, text_error, btn_register);
        if (isCreateForClient) {
            Validators.listValidator(list_clients, text_error, btn_register);
        }
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_name, input_description, input_explication);

        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
        list_clients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private void configureContainer(HBox container, boolean visible) {
        container.setVisible(visible);
        container.setManaged(visible);
    }
}
