package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import training.system.utils.Validator;
import training.system.utils.Validators;

import java.net.URL;
import java.util.ResourceBundle;

public class GymCreateModal implements Initializable, Validator, IView {
    public TextField input_name;
    public TextField input_address;
    public Button btn_register;
    public Label text_error;
    private ProfileViewController profileViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListeners();
    }

    @Override
    public void setupListeners() {
        validateFields();
        addValidators();
        btn_register.setOnAction(e -> create());
    }

    public <T> void setParentController(T controller) {
        this.profileViewController = (ProfileViewController) controller;
    }

    private void create() {
        String name = input_name.getText();
        String address = input_address.getText().isEmpty() ? null : input_address.getText();

        profileViewController.createGym(name, address);
        ((Stage) btn_register.getScene().getWindow()).close();
    }

    @Override
    public void validateFields() {
        Validators.createGymValidator(input_name, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}
