package training.system.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import training.system.core.domain.gym.GymController;
import training.system.core.domain.user.User;
import training.system.utils.ScreenTransitionUtil;
import training.system.utils.SessionManager;
import training.system.utils.Validator;
import training.system.utils.Validators;
import training.system.viewController.interfaces.IView;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class GymManagementViewController implements Initializable, Validator, IView {
    public Text user_name;
    public Button exercise_section;
    public Button routine_section;
    public Button progress_section;
    public Button note_section;
    public Button user_section;
    public Button btn_out;
    public ListView<User> list_trainer;
    public TextField input_new_trainer;
    public Button btn_new_trainer;
    public ListView<User> list_client;
    public TextField input_new_client;
    public Button btn_new_client;
    public ListView<User> list_admin;
    public TextField input_new_admin;
    public Button btn_new_admin;
    public TableView<User> table_trainer_client;
    public TextField input_trainer_attach;
    public Button btn_attach;
    public TextField input_client_attach;
    public Label text_error_trainer;
    public Label text_error_client;
    public Label text_error_admin;
    public Label text_error_attach;
    private SessionManager sessionManager;
    private GymController gymController;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        user_name.setText(currentUser.getName());
        gymController = new GymController();

        setupListeners();
    }

    @Override
    public void setupListeners() {
        addValidators();
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

        listTrainers();
        listClients();
        listAdmins();
        listTrainerClients();

        btn_new_trainer.setOnAction(e -> addTrainer());
        btn_new_client.setOnAction(e -> addClient());
        btn_new_admin.setOnAction(e -> addAdmin());
        btn_attach.setOnAction(e -> attachTrainerClient());
    }

    private void listTrainers() {
        Set<User> trainers = new HashSet<>();
        try {
            trainers = gymController.listGymTrainers(currentUser.getGymManager().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (trainers != null && !trainers.isEmpty()) {
            list_trainer.getItems().clear();
            for (User trainer : trainers) {
                list_trainer.getItems().add(trainer);
            }

            list_trainer.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getEmail() == null) {
                        setText(null);
                    } else {
                        setText(item.getEmail());
                    }
                }
            });
        }
    }

    private void listClients() {
        Set<User> clients = new HashSet<>();
        try {
            clients = gymController.listGymClients(currentUser.getGymManager().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clients != null && !clients.isEmpty()) {
            list_client.getItems().clear();
            for (User client : clients) {
                list_client.getItems().add(client);
            }

            list_client.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getEmail() == null) {
                        setText(null);
                    } else {
                        setText(item.getEmail());
                    }
                }
            });
        }
    }

    private void listAdmins() {
        Set<User> admins = new HashSet<>();
        try {
            admins = gymController.listGymManagers(currentUser.getGymManager().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (admins != null && !admins.isEmpty()) {
            list_admin.getItems().clear();
            for (User admin : admins) {
                list_admin.getItems().add(admin);
            }

            list_admin.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getEmail() == null) {
                        setText(null);
                    } else {
                        setText(item.getEmail());
                    }
                }
            });
        }
    }

    private void listTrainerClients() {
        Set<User> trainerClients = new HashSet<>();

        try {
            trainerClients = gymController.listAttachedTrainersToUser(currentUser.getGymManager().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (trainerClients != null && !trainerClients.isEmpty()) {
            table_trainer_client.getItems().clear();

            TableColumn<User, String> clientColumn = new TableColumn<>("Cliente");
            clientColumn.setCellValueFactory(data -> {
                String fullName = data.getValue().getName() + " " + data.getValue().getLastName();
                return new SimpleStringProperty(fullName);
            });

            TableColumn<User, String> trainersColumn = new TableColumn<>("Entrenador(es)");
            trainersColumn.setCellValueFactory(data -> {
                Set<User> trainers = data.getValue().getTrainers();
                if (trainers == null || trainers.isEmpty()) {
                    return new SimpleStringProperty("Sin entrenador");
                }

                String trainersList = trainers.stream().map(trainer -> trainer.getName() + " " + trainer.getLastName()).collect(Collectors.joining(", "));

                return new SimpleStringProperty(trainersList);
            });


            clientColumn.prefWidthProperty().bind(table_trainer_client.widthProperty().multiply(0.4));
            trainersColumn.prefWidthProperty().bind(table_trainer_client.widthProperty().multiply(0.6));


            table_trainer_client.getColumns().clear();
            table_trainer_client.getColumns().addAll(clientColumn, trainersColumn);

            table_trainer_client.getItems().addAll(trainerClients);
        }
    }

    private void addTrainer() {
        String trainerName = input_new_trainer.getText().toLowerCase();
        if (trainerName.isEmpty()) {
            return;
        }

        try {
            boolean added = gymController.addTrainerToGym(currentUser.getGymManager().getId(), trainerName);
            if (added) {
                text_error_trainer.setText("Entrenador agregado correctamente");
                listTrainers();
            }
            if (!added) {
                text_error_trainer.setVisible(true);
                text_error_trainer.setText("Error al agregar el entrenador");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClient() {
        String clientName = input_new_client.getText().toLowerCase();
        if (clientName.isEmpty()) {
            return;
        }

        try {
            boolean added = gymController.addClientToGym(currentUser.getGymManager().getId(), clientName);
            if (added) {
                text_error_client.setText("Cliente agregado correctamente");
                listClients();
            }
            if (!added) {
                text_error_client.setVisible(true);
                text_error_client.setText("Error al agregar el cliente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdmin() {
        String adminName = input_new_admin.getText().toLowerCase();
        if (adminName.isEmpty()) {
            return;
        }

        try {
            boolean added = gymController.addManagerToGym(currentUser.getGymManager().getId(), adminName);
            if (added) {
                text_error_admin.setText("Gerente agregado correctamente");
                listAdmins();
            }
            if (!added) {
                text_error_admin.setVisible(true);
                text_error_admin.setText("Error al agregar el gerente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attachTrainerClient() {
        String trainerName = input_trainer_attach.getText().toLowerCase();
        String clientName = input_client_attach.getText().toLowerCase();
        if (trainerName.isEmpty() || clientName.isEmpty()) {
            return;
        }

        try {
            boolean attached = gymController.attachTrainerToUser(currentUser.getGymManager().getId(), trainerName, clientName);
            if (attached) {
                text_error_attach.setText("Entrenador vinculado con el cliente correctamente");
            }
            if (!attached) {
                text_error_attach.setVisible(true);
                text_error_attach.setText("Error al vincular el entrenador con el cliente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validateFields() {
        Validators.gymManagementValidator(input_new_trainer, input_new_client, input_new_admin, btn_new_trainer, btn_new_client, btn_new_admin, input_client_attach, input_trainer_attach, btn_attach);
    }

    @Override
    public void addValidators() {
        input_new_trainer.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_new_client.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_new_admin.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_client_attach.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        input_trainer_attach.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }
}