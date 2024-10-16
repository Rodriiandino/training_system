package training.system.core.domain.user;

import training.system.core.domain.gym.Gym;

import java.util.List;
import java.util.Set;

public class User extends Person {
    private Set<Role> roles;
    private Gym gymTraining;
    private List<Gym> gymTrainer;
    private Gym gymManager;
    private List<User> clients;
    private List<User> trainers;

    public User(String name, String lastName, String email, String password) {
        super(name, lastName, email, password);
        Role role = new Role(RoleEnum.ROLE_USER);
        this.roles = Set.of(role);
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles) {
        super(id, name, lastName, email, password);
        this.roles = roles;
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles, Gym gymTraining, List<User> trainers) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymTraining = gymTraining;
        this.trainers = trainers;
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles, List<Gym> gymTrainer, List<User> clients) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymTrainer = gymTrainer;
        this.clients = clients;
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles, Gym gymManager) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymManager = gymManager;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean isUser() {
        return roles.contains(new Role(RoleEnum.ROLE_USER));
    }

    public boolean isTrainer() {
        return roles.contains(new Role(RoleEnum.ROLE_TRAINER));
    }

    public boolean isAdministrator() {
        return roles.contains(new Role(RoleEnum.ROLE_ADMINISTRATOR));
    }

    public Gym getGymTraining() {
        return gymTraining;
    }

    public void setGymTraining(Gym gymTraining) {
        this.gymTraining = gymTraining;
    }

    public List<Gym> getGymTrainer() {
        if (isTrainer()) {
            return gymTrainer;
        }
        throw new IllegalStateException("El usuario no es entrenador.");
    }

    public void setGymTrainer(List<Gym> gymTrainer) {
        if (isTrainer()) {
            this.gymTrainer = gymTrainer;
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public Gym getGymManager() {
        if (isAdministrator()) {
            return gymManager;
        }
        throw new IllegalStateException("El usuario no es administrador.");
    }

    public void setGymManager(Gym gymManager) {
        if (isAdministrator()) {
            this.gymManager = gymManager;
        } else {
            throw new IllegalStateException("El usuario no es administrador.");
        }
    }

    public List<User> getClients() {
        if (isTrainer()) {
            return clients;
        }
        throw new IllegalStateException("El usuario no es entrenador.");
    }

    public void setClients(List<User> clients) {
        if (isTrainer()) {
            this.clients = clients;
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public List<User> getTrainers() {
        if (isAdministrator()) {
            return trainers;
        }
        throw new IllegalStateException("El usuario no es administrador.");
    }

    public void setTrainers(List<User> trainers) {
        if (isAdministrator()) {
            this.trainers = trainers;
        } else {
            throw new IllegalStateException("El usuario no es administrador.");
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("User{");
        output.append("id=").append(getId());
        output.append(", name='").append(getName()).append('\'');
        output.append(", lastName='").append(getLastName()).append('\'');
        output.append(", email='").append(getEmail()).append('\'');
        output.append(", roles=").append(roles);
        if (getGymTraining() != null) {
            output.append(", gymTraining=").append(getGymTraining());
        }

        if (getGymTrainer() != null) {
            output.append(", gymTrainer=").append(getGymTrainer());
        }

        if (getGymManager() != null) {
            output.append(", gymManager=").append(getGymManager());
        }

        if (getClients() != null) {
            output.append(", clients=").append(getClients());
        }

        if (getTrainers() != null) {
            output.append(", trainers=").append(getTrainers());
        }

        output.append('}');
        return output.toString();
    }
}

