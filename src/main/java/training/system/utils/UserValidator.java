package training.system.utils;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserValidator {

    public static void userLoginValidator(TextField input_email, PasswordField input_password, Label text_error, Button btn_in) {
        boolean isGmailValid = !input_email.getText().isEmpty() && input_email.getText().contains("@") && input_email.getText().contains(".com") && input_email.getText().length() > 10;
        boolean isPasswordValid = !input_password.getText().isEmpty() && input_password.getText().length() > 3 && input_password.getText().length() < 20;

        if (!isGmailValid) text_error.setText("Gmail invalido");
        else if (!isPasswordValid)
            text_error.setText("Contraseña invalida (min 3 caracteres, max 20) y no puede estar vacio");
        else text_error.setText("");

        boolean allFieldsValid = isGmailValid && isPasswordValid;

        btn_in.setDisable(!allFieldsValid);
    }

    public static void userRegisterValidator(TextField input_name, TextField input_lastName, TextField input_email, PasswordField input_password, PasswordField input_repeatPassword, Label text_error, Button btn_register) {
        boolean isNameValid = !input_name.getText().isEmpty() && input_name.getText().length() > 3 && input_name.getText().length() < 20;
        boolean isLastNameValid = !input_lastName.getText().isEmpty();
        boolean isGmailValid = !input_email.getText().isEmpty() && input_email.getText().contains("@") && input_email.getText().contains(".com") && input_email.getText().length() > 10;
        boolean isPasswordValid = !input_password.getText().isEmpty() && input_password.getText().length() > 3 && input_password.getText().length() < 20;
        boolean isRepeatPasswordValid = !input_repeatPassword.getText().isEmpty() && input_repeatPassword.getText().length() > 3 && input_repeatPassword.getText().length() < 20;
        boolean isRepeatPasswordEqualsPassword = input_repeatPassword.getText().equals(input_password.getText());

        if (!isNameValid) text_error.setText("Nombre invalido (min 3 caracteres, max 20) y no puede estar vacio");
        else if (!isLastNameValid)
            text_error.setText("Apellido invalido (min 3 caracteres, max 20) y no puede estar vacio");
        else if (!isGmailValid) text_error.setText("Gmail invalido");
        else if (!isPasswordValid)
            text_error.setText("Contraseña invalida (min 3 caracteres, max 20) y no puede estar vacio");
        else if (!isRepeatPasswordValid)
            text_error.setText("Repita la contraseña (min 3 caracteres, max 20) y no puede estar vacio");
        else if (!isRepeatPasswordEqualsPassword) text_error.setText("Las contraseñas no coinciden");
        else text_error.setText("");

        boolean allFieldsValid = isNameValid && isLastNameValid && isGmailValid && isPasswordValid && isRepeatPasswordValid && isRepeatPasswordEqualsPassword;

        btn_register.setDisable(!allFieldsValid);
    }
}
