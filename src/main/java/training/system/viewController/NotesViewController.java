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

public class NotesViewController implements Initializable, IView, IViewControllerManipulation {
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
    private SessionManager sessionManager;
    private NoteController noteController;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());

        setupListeners();
    }

    public void setupListeners() {
        noteController = new NoteController();
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
        TableColumn<Note, String> titleColumn = new TableColumn<>("Titulo");
        TableColumn<Note, String> contentColumn = new TableColumn<>("Contenido");
        TableColumn<Note, String> purposeColumn = new TableColumn<>("Proposito");
        TableColumn<Note, Date> dateColumn = new TableColumn<>("Fecha");
        TableColumn<Note, User> trainerColumn = new TableColumn<>("Entrenador");

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("noteDate"));
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));

        List.of(titleColumn, contentColumn, purposeColumn).forEach(ConfigureColumn::configureTextColumn);
        ConfigureColumn.configureTrainerColumn(trainerColumn);
        ConfigureColumn.configureDateColumn(dateColumn);

        table.getColumns().addAll(titleColumn, contentColumn, purposeColumn, dateColumn, trainerColumn);
    }

    @Override
    public void edit() {
        showEditModal();
    }

    @Override
    public void create() {
        showCreateModal();
    }

    public void create(String title, String content, String purpose, Date date) {
        Note note = new Note(title, content, purpose, date, currentUser);

        try {
            noteController.create(note);
            list();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/note-create-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Elemento");
            modal.setScene(modalScene);
            modal.setResizable(false);

            NoteCreateModal controller = loader.getController();
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
