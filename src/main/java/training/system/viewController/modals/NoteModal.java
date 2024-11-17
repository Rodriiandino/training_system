package training.system.viewController.modals;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import training.system.core.domain.note.Note;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;
import training.system.viewController.NotesViewController;
import training.system.viewController.interfaces.IView;
import training.system.viewController.interfaces.IViewModal;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class NoteModal implements Initializable, IViewModal, Validator, IView {

    public TextField input_title;
    public TextField input_content;
    public TextField input_purpose;
    public DatePicker input_date;
    public Button btn_register;
    public Label text_error;
    public Label text_title;
    public HBox trainer_container;
    public ListView<User> list_clients;
    private Note noteToEdit;
    private NotesViewController notesViewController;
    private UserController userController;
    private SessionManager sessionManager;
    private User currentUser;
    private boolean isCreateForClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        userController = new UserController();
        configureContainer(trainer_container, false);
        setupListeners();
    }

    @Override
    public void setupListeners() {
        validateFields();
        addValidators();
        btn_register.setOnAction(event -> {
            if (noteToEdit == null) {
                if (isCreateForClient) {
                    createForClient();
                } else {
                    create();
                }
            } else {
                edit();
            }
        });
    }

    @Override
    public <T> void setParentController(T controller) {
        this.notesViewController = (NotesViewController) controller;
    }

    @Override
    public void create() {
        String title = input_title.getText().toLowerCase();
        String content = input_content.getText().toLowerCase();
        String purpose = input_purpose.getText().toLowerCase();
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


        Note note = new Note(title, content, purpose, date, currentUser);
        notesViewController.create(note);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void edit() {
        String title = input_title.getText().toLowerCase();
        String content = input_content.getText().toLowerCase();
        String purpose = input_purpose.getText().toLowerCase();
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


        Note note = new Note(noteToEdit.getId(), title, content, purpose, date, noteToEdit.getUser());
        notesViewController.edit(note);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void createForClient() {
        String title = input_title.getText().toLowerCase();
        String content = input_content.getText().toLowerCase();
        String purpose = input_purpose.getText().toLowerCase();
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        User client = list_clients.getSelectionModel().getSelectedItem();

        Note note = new Note(title, content, purpose, date, client, currentUser);
        notesViewController.createForClient(note);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    public void setNoteToEdit(Note note) {
        this.noteToEdit = note;
        input_title.setText(note.getTitle());
        input_content.setText(note.getContent());
        input_purpose.setText(note.getPurpose());

        updateModalMode();
    }

    public void setCreateForClient(boolean createForClient) {
        isCreateForClient = createForClient;

        updateModalMode();
    }

    @Override
    public void updateModalMode() {
        if (noteToEdit == null) {
            if (isCreateForClient) {
                text_title.setText("Crear Nota para Cliente");
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
        Validators.noteValidator(input_title, input_content, input_purpose, text_error, btn_register, input_date);
        if (isCreateForClient) {
            Validators.listValidator(list_clients, text_error, btn_register);
        }
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_title, input_content, input_purpose);
        input_date.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());
        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }

    private void configureContainer(HBox container, boolean visible) {
        container.setVisible(visible);
        container.setManaged(visible);
    }
}
