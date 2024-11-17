package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;
import training.system.utils.Validators;
import training.system.utils.Validator;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable, Validator, IView {
    public TextField input_email;
    public PasswordField input_password;
    public Button btn_in;
    public Button btn_register;
    public Label text_error;

    private UserController userController;
    private SessionManager sessionManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionManager = SessionManager.getInstance();
        setupListeners();
    }

    @Override
    public void setupListeners() {
        validateFields();
        addValidators();

        userController = new UserController();

        btn_in.setOnAction(event -> {
            try {
                login();
            } catch (ControllerException e) {
                throw new RuntimeException(e);
            }
        });

        btn_register.setOnAction(event -> ScreenTransitionUtil.changeScreen(this, "/training/system/view/registration-view.fxml", btn_register));
    }

    private void login() throws ControllerException {
        String email = input_email.getText().toLowerCase();
        String password = input_password.getText();

        User userRegister = null;

        try {
            userRegister = userController.authenticate(email, password);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        if (userRegister != null) {
            sessionManager.setCurrentUser(userRegister);
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/main-view.fxml", btn_in);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al iniciar sesión");
            alert.setHeaderText("Usuario no registrado");
            alert.setContentText("Este usuario no está registrado en el sistema");

            alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/training/system/styles/delete.css")).toExternalForm());

            alert.showAndWait();
        }
    }

    @Override
    public void validateFields() {
        Validators.userLoginValidator(input_email, input_password, text_error, btn_in);
    }

    @Override
    public void addValidators() {
        List<TextField> textFields = List.of(input_email, input_password);

        textFields.forEach(textField -> textField.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }
}
