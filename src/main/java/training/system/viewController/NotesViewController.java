package training.system.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import training.system.core.domain.note.Note;
import training.system.core.domain.note.NoteController;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ConfigureColumn;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class NotesViewController implements Initializable, IView, IViewControllerManipulation<Note> {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView<Note> table;
    public Button btn_create;
    public Button btn_edit;
    public Button btn_out;
    public Button btn_create_client;
    private SessionManager sessionManager;
    private NoteController noteController;
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
        noteController = new NoteController();
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
        TableColumn<Note, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Note, String> titleColumn = new TableColumn<>("Titulo");
        TableColumn<Note, String> contentColumn = new TableColumn<>("Contenido");
        TableColumn<Note, String> purposeColumn = new TableColumn<>("Proposito");
        TableColumn<Note, Date> dateColumn = new TableColumn<>("Fecha");
        TableColumn<Note, User> trainerColumn = new TableColumn<>("Entrenador");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("noteDate"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));

        List.of(titleColumn, contentColumn, purposeColumn).forEach(ConfigureColumn::configureTextColumn);
        ConfigureColumn.configureTrainerColumn(trainerColumn);
        ConfigureColumn.configureDateColumn(dateColumn);

        table.getColumns().addAll(idColumn, titleColumn, contentColumn, purposeColumn, dateColumn, trainerColumn);
    }

    @Override
    public void create(Note note) {
        try {
            noteController.create(note);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Note note) {
        try {
            noteController.update(note);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createForClient(Note entity) {
        try {
            noteController.createNoteForClient(entity);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/note-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Nota");
            modal.setScene(modalScene);
            modal.setResizable(false);

            NoteModal controller = loader.getController();
            controller.setParentController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showEditModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/note-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Nota");
            modal.setScene(modalScene);
            modal.setResizable(false);

            NoteModal controller = loader.getController();
            controller.setParentController(this);
            controller.setNoteToEdit(table.getSelectionModel().getSelectedItem());

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateForClientModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/note-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Nota para cliente");
            modal.setScene(modalScene);
            modal.setResizable(false);

            NoteModal controller = loader.getController();
            controller.setParentController(this);
            controller.setCreateForClient(true);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {
        ObservableList<Note> notes = FXCollections.observableArrayList();

        try {
            Set<Note> noteSet = noteController.listUserNotes(currentUser);
            notes.addAll(noteSet);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        table.setItems(notes);
    }
}
