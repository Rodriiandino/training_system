package training.system.viewController;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import training.system.core.domain.gym.Gym;
import training.system.core.domain.gym.GymController;
import training.system.core.domain.user.Role;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;
import training.system.core.domain.user.UserController;
import training.system.core.exception.ControllerException;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;
import training.system.viewController.interfaces.IView;
import training.system.viewController.modals.GymCreateModal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ProfileViewController implements Initializable, Validator, IView {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public Button btn_out;
    public TextField input_name;
    public Button btn_edit_name;
    public TextField input_lastname;
    public Button btn_edit_lastname;
    public TextField input_email;
    public Button btn_edit_email;
    public Label label_rol;
    public Button btn_trainer;
    public Button btn_manager;
    public Button btn_active_edit;
    public Button btn_create_gym;
    public Button btn_in_gym;
    public HBox container_info_gym;
    public Label text_gym_train;
    public Label text_gym_admin;
    public HBox container_gym_work;
    public ListView<Gym> list_gym_work;
    public HBox container_gym_train;
    public HBox container_gym_admin;
    private SessionManager sessionManager;
    private UserController userController;
    private GymController gymController;
    User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userController = new UserController();
        gymController = new GymController();
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();

        setupListeners();
    }

    @Override
    public void setupListeners() {
        addValidators();
        user_name.setText(currentUser.getName());
        input_name.setText(currentUser.getName());
        input_lastname.setText(currentUser.getLastName());
        input_email.setText(currentUser.getEmail());

        boolean isTrainer = currentUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_TRAINER));
        boolean isManager = currentUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_ADMINISTRATOR));
        boolean administerAGym = currentUser.getGymManager() != null;
        boolean trainInAGym = currentUser.getGymTraining() != null;
        boolean workInAGym = currentUser.getGymTrainer() != null && !currentUser.getGymTrainer().isEmpty();

        label_rol.setText("Usuario" + (isTrainer ? " - Entrenador" : "") + (isManager ? " - Administrador" : ""));

        configureButton(btn_trainer, !isTrainer);
        configureButton(btn_manager, !isManager);
        configureButton(btn_create_gym, isManager && !administerAGym);
        configureButton(btn_in_gym, administerAGym);
        configureContainer(container_info_gym, administerAGym || trainInAGym || workInAGym);

        configureContainer(container_gym_work, workInAGym);
        configureContainer(container_gym_train, trainInAGym);
        configureContainer(container_gym_admin, administerAGym);
        if (administerAGym) text_gym_admin.setText(currentUser.getGymManager().getName());
        if (trainInAGym) text_gym_train.setText(currentUser.getGymTraining().getName());
        if (workInAGym) {
            list_gym_work.getItems().addAll(currentUser.getGymTrainer());
            list_gym_work.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Gym item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getName() == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
        }

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

        btn_in_gym.setOnAction(e -> {
            ScreenTransitionUtil.changeScreen(this, "/training/system/view/gym-management-view.fxml", btn_in_gym);
        });

        btn_active_edit.setOnAction(e -> {
            if (btn_active_edit.getText().equals("Editar Perfil")) {
                activeEdit();
            } else {
                outEdit();
            }
        });

        btn_edit_name.setOnAction(e -> editName());
        btn_edit_lastname.setOnAction(e -> editLastName());
        btn_edit_email.setOnAction(e -> editEmail());

        btn_trainer.setOnAction(e -> becomeTrainer());
        btn_manager.setOnAction(e -> becomeManager());
        btn_create_gym.setOnAction(e -> showModalCreteGym());
    }

    private void activeEdit() {
        input_name.setEditable(true);
        input_lastname.setEditable(true);
        input_email.setEditable(true);
        btn_edit_name.setVisible(true);
        btn_edit_lastname.setVisible(true);
        btn_edit_email.setVisible(true);
        btn_active_edit.setText("Salir de edición");
    }

    private void outEdit() {
        input_name.setEditable(false);
        input_lastname.setEditable(false);
        input_email.setEditable(false);
        btn_edit_name.setVisible(false);
        btn_edit_lastname.setVisible(false);
        btn_edit_email.setVisible(false);

        input_name.setText(currentUser.getName());
        input_lastname.setText(currentUser.getLastName());
        input_email.setText(currentUser.getEmail());
        btn_active_edit.setText("Editar Perfil");
    }

    private void editName() {
        String name = input_name.getText().toLowerCase();
        User newUser = new User(currentUser.getId(), name, currentUser.getLastName(), currentUser.getEmail(), currentUser.getRoles());

        try {
            userController.update(newUser);
            updateCurrentUser();
            user_name.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editLastName() {
        String lastName = input_lastname.getText().toLowerCase();
        User newUser = new User(currentUser.getId(), currentUser.getName(), lastName, currentUser.getEmail(), currentUser.getRoles());

        try {
            userController.update(newUser);
            updateCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editEmail() {
        String email = input_email.getText().toLowerCase();
        User newUser = new User(currentUser.getId(), currentUser.getName(), currentUser.getLastName(), email, currentUser.getRoles());

        try {
            userController.update(newUser);
            updateCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void becomeTrainer() {
        try {
            userController.becomeTrainer(currentUser.getId());
            User newUser = new User(currentUser.getId(), currentUser.getName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getRoles());
            newUser.addRole(new Role(RoleEnum.ROLE_TRAINER));
            updateCurrentUser();

            boolean isTrainer = newUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_TRAINER));
            boolean isManager = newUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_ADMINISTRATOR));

            configureButton(btn_trainer, !isTrainer);

            label_rol.setText("Usuario" + (isTrainer ? " - Entrenador" : "") + (isManager ? " - Administrador" : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void becomeManager() {
        try {
            userController.becomeAdministrator(currentUser.getId());
            User newUser = new User(currentUser.getId(), currentUser.getName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getRoles());
            newUser.addRole(new Role(RoleEnum.ROLE_ADMINISTRATOR));
            updateCurrentUser();

            boolean isTrainer = newUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_TRAINER));
            boolean isManager = newUser.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleEnum.ROLE_ADMINISTRATOR));

            configureButton(btn_manager, !isManager);
            configureButton(btn_create_gym, isManager);

            label_rol.setText("Usuario" + (isTrainer ? " - Entrenador" : "") + (isManager ? " - Administrador" : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showModalCreteGym() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/training/system/view/gym-create-modal.fxml"));
            Parent root = loader.load();

            Scene modalScene = new Scene(root);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Crear Elemento");
            modal.setScene(modalScene);
            modal.setResizable(false);

            GymCreateModal controller = loader.getController();
            controller.setParentController(this);

            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createGym(String name, String address) {
        Set<User> managers = Set.of(currentUser);
        Gym gym = new Gym(name, address, managers);
        try {
            gymController.create(gym);
            updateCurrentUser();
            configureContainer(container_info_gym, true);
            configureContainer(container_gym_admin, true);
            text_gym_admin.setText(gym.getName());
            configureButton(btn_create_gym, false);
            configureButton(btn_in_gym, true);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    private void configureButton(Button button, boolean visible) {
        button.setVisible(visible);
        button.setManaged(visible);
    }

    private void configureContainer(HBox container, boolean visible) {
        container.setVisible(visible);
        container.setManaged(visible);
    }

    private void updateCurrentUser() {
        try {
            sessionManager.setCurrentUser(userController.search(currentUser.getId()));
            currentUser = sessionManager.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validateFields() {
        Validators.editUserValidator(input_name, input_lastname, input_email, btn_edit_name, btn_edit_lastname, btn_edit_email);
    }

    @Override
    public void addValidators() {
        input_name.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_lastname.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_email.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}