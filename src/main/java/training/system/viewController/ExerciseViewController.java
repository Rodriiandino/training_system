package training.system.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import training.system.core.domain.category.Category;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ConfigureColumn;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;
import training.system.viewController.interfaces.IView;
import training.system.viewController.interfaces.IViewControllerManipulation;
import training.system.viewController.modals.ExerciseModal;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ExerciseViewController implements Initializable, IView, IViewControllerManipulation<Exercise> {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView<Exercise> table;
    public Button btn_create;
    public Button btn_edit;
    public Button btn_out;
    public Button btn_create_client;
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());
        btn_edit.setDisable(true);

        setupListeners();
    }

    @Override
    public void setupListeners() {
        exerciseController = new ExerciseController();
        createColumn();
        list();
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btn_edit.setDisable(newSelection == null);
        });

        btn_create.setOnAction(e -> showCreateModal());
        btn_edit.setOnAction(e -> showEditModal());
        btn_create_client.setOnAction(e -> showCreateForClientModal());

        if (currentUser.getRoles().stream().anyMatch(role -> role.getRole().name().equals(RoleEnum.ROLE_TRAINER.name()))) {
            btn_create_client.setVisible(true);
        }

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
        TableColumn<Exercise, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Exercise, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Exercise, String> descriptionColumn = new TableColumn<>("Descripción");
        TableColumn<Exercise, String> explanationColumn = new TableColumn<>("Explicación");
        TableColumn<Exercise, String> videoUrlColumn = new TableColumn<>("Video URL");
        TableColumn<Exercise, Boolean> isPredefinedColumn = new TableColumn<>("Predefinido");
        TableColumn<Exercise, Set<Category>> categoriesColumn = new TableColumn<>("Categorías");
        TableColumn<Exercise, User> trainerColumn = new TableColumn<>("Entrenador");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        explanationColumn.setCellValueFactory(new PropertyValueFactory<>("explanation"));
        videoUrlColumn.setCellValueFactory(new PropertyValueFactory<>("videoUrl"));
        isPredefinedColumn.setCellValueFactory(new PropertyValueFactory<>("isPredefined"));
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));

        List.of(nameColumn, descriptionColumn, explanationColumn, videoUrlColumn).forEach(ConfigureColumn::configureTextColumn);
        ConfigureColumn.configureTrainerColumn(trainerColumn);
        ConfigureColumn.configureBooleanColumn(isPredefinedColumn);

        configureCategoriesColumn(categoriesColumn);

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn, explanationColumn, videoUrlColumn, isPredefinedColumn, categoriesColumn, trainerColumn);
    }


    private void configureCategoriesColumn(TableColumn<Exercise, Set<Category>> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(150);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Set<Category> categories, boolean empty) {
                super.updateItem(categories, empty);
                if (empty) {
                    setText(null);
                } else if (categories == null || categories.isEmpty()) {
                    setText("-");
                } else {
                    String categoryText = categories.stream().map(Category::getName).filter(Objects::nonNull).collect(Collectors.joining(", "));
                    setText(categoryText.length() > 80 ? categoryText.substring(0, 80) + "..." : categoryText);
                    setTooltip(new Tooltip(categoryText));
                }
            }
        });
    }

    @Override
    public void create(Exercise exercise) {
        try {
            exerciseController.create(exercise);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Exercise exercise) {
        try {
            exerciseController.update(exercise);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createForClient(Exercise entity) {
        try {
            exerciseController.createExerciseForClient(entity);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/exercise-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Ejercicio");
            modal.setScene(modalScene);
            modal.setResizable(false);

            ExerciseModal controller = loader.getController();
            controller.setParentController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showEditModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/exercise-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Ejercicio");
            modal.setScene(modalScene);
            modal.setResizable(false);

            ExerciseModal controller = loader.getController();
            controller.setParentController(this);
            controller.setExerciseToEdit(table.getSelectionModel().getSelectedItem());

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateForClientModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/exercise-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Ejercicio para cliente");
            modal.setScene(modalScene);
            modal.setResizable(false);

            ExerciseModal controller = loader.getController();
            controller.setParentController(this);
            controller.setCreateForClient(true);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {
        ObservableList<Exercise> exercises = FXCollections.observableArrayList();

        try {
            Set<Exercise> exerciseSet = exerciseController.listUserExercises(currentUser);
            exercises.addAll(exerciseSet);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        table.setItems(exercises);
    }
}
