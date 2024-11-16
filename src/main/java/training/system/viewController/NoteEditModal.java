package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import training.system.core.domain.note.Note;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class NoteEditModal implements Initializable, IViewModal, Validator {
    public TextField input_title;
    public TextField input_content;
    public TextField input_purpose;
    public DatePicker input_date;
    public Button btn_edit;
    public Label text_error;
    private NotesViewController notesViewController;
    private Note noteToEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListeners();
    }

    private void setupListeners() {
        validateFields();
        addValidators();
        btn_edit.setOnAction(e -> edit());
    }

    public void setNoteToEdit(Note note) {
        this.noteToEdit = note;
        input_title.setText(note.getTitle());
        input_content.setText(note.getContent());
        input_purpose.setText(note.getPurpose());
    }

    private void edit() {
        String title = input_title.getText().toLowerCase();
        String content = input_content.getText().toLowerCase();
        String purpose = input_purpose.getText().toLowerCase();
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


        Note note = new Note(noteToEdit.getId(), title, content, purpose, date, noteToEdit.getUser());
        notesViewController.edit(note);
        ((Stage) btn_edit.getScene().getWindow()).close();
    }

    @Override
    public <T> void setParentController(T controller) {
        this.notesViewController = (NotesViewController) controller;
    }

    @Override
    public void validateFields() {
        Validators.noteValidator(input_title, input_content, input_purpose, text_error, btn_edit, input_date);
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_title, input_content, input_purpose);
        input_date.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());
        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }
}
