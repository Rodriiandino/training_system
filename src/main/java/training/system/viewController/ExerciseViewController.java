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
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ConfigureColumn;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ExerciseViewController implements Initializable, IView, IViewControllerManipulation {
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
    private SessionManager sessionManager;
    private ExerciseController exerciseController;
    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());

        setupListeners();
    }

    @Override
    public void setupListeners() {
        exerciseController = new ExerciseController();
        createColumn();
        list();
        btn_create.setOnAction(e -> create());
        btn_edit.setOnAction(e -> edit());

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
        TableColumn<Exercise, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Exercise, String> descriptionColumn = new TableColumn<>("Descripción");
        TableColumn<Exercise, String> explanationColumn = new TableColumn<>("Explicación");
        TableColumn<Exercise, String> videoUrlColumn = new TableColumn<>("Video URL");
        TableColumn<Exercise, Boolean> isPredefinedColumn = new TableColumn<>("Predefinido");
        TableColumn<Exercise, Set<Category>> categoriesColumn = new TableColumn<>("Categorías");
        TableColumn<Exercise, User> trainerColumn = new TableColumn<>("Entrenador");

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

        table.getColumns().addAll(nameColumn, descriptionColumn, explanationColumn, videoUrlColumn, isPredefinedColumn, categoriesColumn, trainerColumn);
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
    public void edit() {
        showEditModal();
    }

    @Override
    public void create() {
        showCreateModal();
    }

    public void create(String name, String description, String explanation, String videoUrl, Set<Category> categories) {
        Exercise exercise = new Exercise(name, description, explanation, videoUrl, false, categories, currentUser);

        try {
            exerciseController.create(exercise);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/exercise-create-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Elemento");
            modal.setScene(modalScene);
            modal.setResizable(false);

            ExerciseCreateModal controller = loader.getController();
            controller.setParentController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showEditModal() {

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
