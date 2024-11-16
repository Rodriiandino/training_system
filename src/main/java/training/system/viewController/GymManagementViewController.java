package training.system.viewController;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import training.system.core.domain.user.User;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagementViewController implements Initializable {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public TableView table;
    public Button btn_out;
    public ListView list_trainer;
    public TextField input_new_trainer;
    public Button btn_new_trainer;
    public ListView list_client;
    public TextField input_new_client;
    public Button btn_new_client;
    public ListView list_admin;
    public TextField input_new_admin;
    public Button btn_new_admin;
    public TableView table_trainer_client;
    public TextField input_trainer_attach;
    public Button btn_attach;
    public TextField input_client_attach;
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

        btn_out.setOnAction(e -> {
            sessionManager.closeSession();
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/login-view.fxml", btn_out);
        });
    }


    private void listTrainers() {}
    private void listClients() {}
    private void listAdmins() {}
    private void listTrainerClients() {}

    private void addTrainer() {}
    private void addClient() {}
    private void addAdmin() {}
    private void attachTrainerClient() {}
}