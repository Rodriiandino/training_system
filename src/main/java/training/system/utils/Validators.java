package training.system.utils;


import javafx.scene.control.*;
import training.system.core.domain.exercise.Exercise;

public class Validators {

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

    public static void createExerciseValidator(TextField input_name, TextField input_description, TextField input_explanation, Label text_error, Button btn_register) {
        boolean isNameValid = !input_name.getText().isEmpty() && input_name.getText().length() > 5 && input_name.getText().length() < 255;
        boolean isDescriptionValid = !input_description.getText().isEmpty() && input_description.getText().length() > 10 && input_description.getText().length() < 255;
        boolean isExplanationValid = !input_explanation.getText().isEmpty() && input_explanation.getText().length() > 10 && input_explanation.getText().length() < 255;

        if (!isNameValid) text_error.setText("Nombre invalido (min 5 caracteres, max 255 y no puede estar vacio");
        else if (!isDescriptionValid)
            text_error.setText("Descripcion invalida (min 10 caracteres, max 255) y no puede estar vacio");
        else if (!isExplanationValid)
            text_error.setText("Explicacion invalida (min 10 caracteres, max 255) y no puede estar vacio");
        else text_error.setText("");

        boolean allFieldsValid = isNameValid && isDescriptionValid && isExplanationValid;

        btn_register.setDisable(!allFieldsValid);
    }

    public static void createNoteValidator(TextField input_title, TextField input_content, TextField input_purpose, Label text_error, Button btn_register) {
        boolean isTitleValid = !input_title.getText().isEmpty() && input_title.getText().length() > 5 && input_title.getText().length() < 255;
        boolean isContentValid = !input_content.getText().isEmpty() && input_content.getText().length() > 10 && input_content.getText().length() < 255;
        boolean isPurposeValid = !input_purpose.getText().isEmpty() && input_purpose.getText().length() > 10 && input_purpose.getText().length() < 255;

        if (!isTitleValid) text_error.setText("Titulo invalido (min 5 caracteres, max 255 y no puede estar vacio");
        else if (!isContentValid)
            text_error.setText("Contenido invalido (min 10 caracteres, max 255) y no puede estar vacio");
        else if (!isPurposeValid)
            text_error.setText("Proposito invalido (min 10 caracteres, max 255) y no puede estar vacio");
        else text_error.setText("");

        boolean allFieldsValid = isTitleValid && isContentValid && isPurposeValid;

        btn_register.setDisable(!allFieldsValid);
    }

    public static void createProgressValidator(ListView<Exercise> list_view, DatePicker input_date, Label text_error, Button btn_register) {
        boolean isDateSelected = input_date.getValue() != null;
        boolean isExerciseSelected = list_view.getSelectionModel().getSelectedItem() != null;

        if (!isDateSelected) text_error.setText("Seleccione una fecha");
        else if (!isExerciseSelected) text_error.setText("Seleccione un ejercicio");
        else text_error.setText("");

        boolean allFieldsValid = isDateSelected && isExerciseSelected;

        btn_register.setDisable(!allFieldsValid);
    }

    public static void createRoutineValidator(TextField input_name, ListView<Exercise> list_view, Label text_error, Button btn_register) {
        boolean isNameValid = !input_name.getText().isEmpty() && input_name.getText().length() > 5 && input_name.getText().length() < 255;
        boolean isExerciseSelected = list_view.getSelectionModel().getSelectedItem() != null;

        if (!isNameValid) text_error.setText("Nombre invalido (min 5 caracteres, max 255 y no puede estar vacio");
        else if (!isExerciseSelected) text_error.setText("Seleccione un ejercicio");
        else text_error.setText("");

        boolean allFieldsValid = isNameValid && isExerciseSelected;

        btn_register.setDisable(!allFieldsValid);
    }
}
