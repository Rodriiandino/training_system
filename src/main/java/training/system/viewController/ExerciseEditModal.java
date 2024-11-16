package training.system.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import training.system.core.domain.category.Category;
import training.system.core.domain.category.CategoryController;
import training.system.core.domain.exercise.Exercise;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ExerciseEditModal implements Initializable, IViewModal, Validator {
    public TextField input_name;
    public TextField input_description;
    public TextField input_explication;
    public TextField input_url;
    public ListView<Category> list_view;
    public Button btn_edit;
    public Label text_error;
    private ExerciseViewController exerciseViewController;
    private CategoryController categoryController;
    private Exercise exerciseToEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryController = new CategoryController();
        setupListeners();
    }

    private void setupListeners() {
        validateFields();
        addValidators();
        btn_edit.setOnAction(e -> edit());
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
    }

    private void edit() {
        String name = input_name.getText().toLowerCase();
        String description = input_description.getText().toLowerCase();
        String explication = input_explication.getText().toLowerCase();
        String url = input_url.getText().isEmpty() ? null : input_url.getText().toLowerCase();
        ObservableList<Category> selectedItems = list_view.getSelectionModel().getSelectedItems();

        Set<Category> categories = new HashSet<>(selectedItems);

        Exercise exercise = new Exercise(exerciseToEdit.getId(), name, description, explication, url, false, categories, exerciseToEdit.getUser());
        exerciseViewController.edit(exercise);
        ((Stage) btn_edit.getScene().getWindow()).close();
    }


    @Override
    public <T> void setParentController(T controller) {
        this.exerciseViewController = (ExerciseViewController) controller;
    }

    @Override
    public void validateFields() {
        Validators.exerciseValidator(input_name, input_description, input_explication, text_error, btn_edit);
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_name, input_description, input_explication);

        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }
}
