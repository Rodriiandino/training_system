package training.system.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import training.system.core.domain.category.Category;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.exercise.ExerciseController;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

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

        videoUrlColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String videoUrl, boolean empty) {
                super.updateItem(videoUrl, empty);
                setText(empty ? null : (videoUrl == null ? "Sin video" : videoUrl));
            }
        });

        categoriesColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Set<Category> categories, boolean empty) {
                super.updateItem(categories, empty);
                if (empty) {
                    setText(null);
                } else if (categories == null) {
                    setText("Sin categorías");
                } else {
                    StringBuilder categoryNames = new StringBuilder();
                    for (Category category : categories) {
                        if (category.getName() != null) {
                            categoryNames.append(category.getName()).append(", ");
                        }
                    }

                    if (!categoryNames.isEmpty()) {
                        categoryNames.setLength(categoryNames.length() - 2);
                        setText(categoryNames.toString());
                    } else {
                        setText("Sin categorías");
                    }
                }
            }
        });

        trainerColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(User trainer, boolean empty) {
                super.updateItem(trainer, empty);
                if (empty) {
                    setText(null);
                } else if (trainer == null) {
                    setText("Sin entrenador");
                } else {
                    setText(trainer.getName());
                }
            }
        });

        table.getColumns().addAll(nameColumn, descriptionColumn, explanationColumn, videoUrlColumn, isPredefinedColumn, categoriesColumn, trainerColumn);
    }

    @Override
    public void edit() {

    }

    @Override
    public void create() {

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
