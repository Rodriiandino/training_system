package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import training.system.core.domain.category.Category;
import training.system.core.domain.category.CategoryController;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ExerciseCreateModal implements Initializable, IViewModal, Validator {
    public TextField input_name;
    public TextField input_description;
    public TextField input_explication;
    public TextField input_url;
    public ListView<Category> list_view;
    public Button btn_register;
    public Label text_error;
    private ExerciseViewController exerciseViewController;
    private CategoryController categoryController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListeners();
    }

    private void setupListeners() {
        validateFields();
        addValidators();
        categoryController = new CategoryController();
        btn_register.setOnAction(e -> create());
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

    private void create() {
        String name = input_name.getText().toLowerCase();
        String description = input_description.getText().toLowerCase();
        String explication = input_explication.getText().toLowerCase();
        String url = input_url.getText().isEmpty() ? null : input_url.getText().toLowerCase();
        ObservableList<Category> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Category> categories = new HashSet<>(selectedItems);

        exerciseViewController.create(name, description, explication, url, categories);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void validateFields() {
        Validators.createExerciseValidator(input_name, input_description, input_explication, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_name, input_description, input_explication);

        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }
}
