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
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.routine.RoutineController;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ConfigureColumn;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;
import training.system.viewController.interfaces.IView;
import training.system.viewController.interfaces.IViewControllerManipulation;
import training.system.viewController.modals.RoutineModal;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class RoutineViewController implements Initializable, IView, IViewControllerManipulation<Routine> {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView<Routine> table;
    public Button btn_create;
    public Button btn_edit;
    public Button btn_out;
    public Button btn_create_client;
    private SessionManager sessionManager;
    private RoutineController routineController;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());
        btn_edit.setDisable(true);

        setupListeners();
    }

    public void setupListeners() {
        routineController = new RoutineController();
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
        TableColumn<Routine, String> idColumn = new TableColumn<>("ID");
        TableColumn<Routine, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Routine, String> descriptionColumn = new TableColumn<>("Descripción");
        TableColumn<Routine, User> trainerColumn = new TableColumn<>("Entrenador");
        TableColumn<Routine, Set<Exercise>> exercisesColumn = new TableColumn<>("Ejercicios");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        exercisesColumn.setCellValueFactory(new PropertyValueFactory<>("exercises"));


        List.of(nameColumn, descriptionColumn).forEach(ConfigureColumn::configureTextColumn);
        ConfigureColumn.configureTrainerColumn(trainerColumn);

        exercisesColumn.setStyle("-fx-alignment: CENTER;");
        exercisesColumn.setMinWidth(150);
        exercisesColumn.setMaxWidth(250);
        exercisesColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Set<Exercise> exercises, boolean empty) {
                super.updateItem(exercises, empty);
                if (empty) {
                    setText(null);
                } else if (exercises == null) {
                    setText("Sin ejercicios");
                } else {
                    StringBuilder exercisesString = new StringBuilder();
                    for (Exercise exercise : exercises) {
                        if (exercise != null) {
                            exercisesString.append(exercise.getName()).append(", ");
                        }
                    }
                    exercisesString.setLength(exercisesString.length() - 2);

                    setText(exercisesString.length() > 80 ? exercisesString.substring(0, 80) + "..." : exercisesString.toString());

                    setTooltip(new Tooltip(exercisesString.toString()));
                }
            }
        });

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn, trainerColumn, exercisesColumn);
    }


    @Override
    public void create(Routine routine) {
        try {
            routineController.create(routine);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Routine routine) {
        try {
            routineController.update(routine);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createForClient(Routine entity) {
        try {
            routineController.createRoutineForClient(entity);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/routine-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Rutina");
            modal.setScene(modalScene);
            modal.setResizable(false);

            RoutineModal controller = loader.getController();
            controller.setParentController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showEditModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/routine-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Rutina");
            modal.setScene(modalScene);
            modal.setResizable(false);

            RoutineModal controller = loader.getController();
            controller.setParentController(this);
            controller.setRoutineToEdit(table.getSelectionModel().getSelectedItem());

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateForClientModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/routine-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Rutina para Cliente");
            modal.setScene(modalScene);
            modal.setResizable(false);

            RoutineModal controller = loader.getController();
            controller.setParentController(this);
            controller.setCreateForClient(true);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {
        ObservableList<Routine> routines = FXCollections.observableArrayList();

        try {
            Set<Routine> routineSet = routineController.listUserRoutines(currentUser);
            routines.addAll(routineSet);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        table.setItems(routines);
    }
}