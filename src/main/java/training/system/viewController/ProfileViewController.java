package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import training.system.core.domain.user.User;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    private SessionManager sessionManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        User currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());

        setupListeners();
    }

    private void setupListeners() {
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
    }
}