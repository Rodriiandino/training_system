package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.Validators;
import training.system.utils.Validator;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationViewController implements Initializable, Validator {
    public Label text_error;
    public Button btn_register;
    public Button btn_back;
    public PasswordField input_password;
    public PasswordField input_repeatPassword;
    public TextField input_email;
    public TextField input_name;
    public TextField input_lastName;

    private UserController userController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new UserController();

        validateFields();
        addValidators();
        btn_register.setOnAction(e -> register());

        btn_back.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/login-view.fxml", btn_back);
        });
    }

    private void register() {
        String name = input_name.getText().toLowerCase();
        String lastName = input_lastName.getText().toLowerCase();
        String email = input_email.getText().toLowerCase();
        String password = input_password.getText();


        boolean isUserRegister = false;
        try {
            isUserRegister = userController.isEmailAlreadyRegistered(email);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        if (isUserRegister) {
            text_error.setText("El email ya está registrado");
            return;
        }

        User usuario = new User(name, lastName, email, password);

        try {
            userController.create(usuario);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        ScreenTransitionUtil.changeScreen(this, "/training/system/view/login-view.fxml", btn_register);
    }

    @Override
    public void validateFields() {
        Validators.userRegisterValidator(input_name, input_lastName, input_email, input_password, input_repeatPassword, text_error, btn_register);
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_lastName.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_email.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_password.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_repeatPassword.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

}
