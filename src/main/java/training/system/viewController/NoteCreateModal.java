package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class NoteCreateModal implements Initializable, IViewModal, Validator {

    public TextField input_title;
    public TextField input_content;
    public TextField input_purpose;
    public DatePicker input_date;
    public Button btn_register;
    public Label text_error;
    NotesViewController notesViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListeners();
    }

    private void setupListeners() {
        validateFields();
        addValidators();
        btn_register.setOnAction(e -> create());
    }

    @Override
    public <T> void setParentController(T controller) {
        this.notesViewController = (NotesViewController) controller;
    }

    private void create() {
        String title = input_title.getText().toLowerCase();
        String content = input_content.getText().toLowerCase();
        String purpose = input_purpose.getText().toLowerCase();
        LocalDate localDate = input_date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        notesViewController.create(title, content, purpose, date);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void validateFields() {
        Validators.createNoteValidator(input_title, input_content, input_purpose, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_title, input_content, input_purpose);

        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }
}
