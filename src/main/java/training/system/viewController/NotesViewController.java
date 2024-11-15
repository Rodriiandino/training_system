package training.system.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import training.system.core.domain.note.Note;
import training.system.core.domain.note.NoteController;
import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.Date;
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

        table.getColumns().addAll(titleColumn, contentColumn, purposeColumn, dateColumn, trainerColumn);
    }

    @Override
    public void edit() {

    }

    @Override
    public void create() {

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
