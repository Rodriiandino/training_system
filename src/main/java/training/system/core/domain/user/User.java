package training.system.core.domain.user;

import training.system.core.domain.gym.Gym;

import java.util.HashSet;
import java.util.Set;

public class User extends Person {
    private Set<Role> roles = new HashSet<>();
    private Gym gymTraining;
    private Set<Gym> gymTrainer = new HashSet<>();
    private Gym gymManager;
    private Set<User> clients = new HashSet<>();
    private Set<User> trainers = new HashSet<>();

    public User(String name, String lastName, String email, String password) {
        super(name, lastName, email, password);
        this.roles.add(new Role(RoleEnum.ROLE_USER));
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles) {
        super(id, name, lastName, email, password);
        this.roles = roles;
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles, Gym gymTraining, Set<User> trainers) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymTraining = gymTraining;
        this.trainers = trainers;
    }

    public User(int id, String name, String lastName, String email, String password, Set<Role> roles, Set<Gym> gymTrainer, Set<User> clients) {
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

    public Set<Gym> getGymTrainer() {
        if (isTrainer()) {
            return gymTrainer;
        }
        throw new IllegalStateException("El usuario no es entrenador.");
    }

    public void addGymTrainer(Gym gymTrainer) {
        if (isTrainer()) {
            this.gymTrainer.add(gymTrainer);
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public void removeGymTrainer(Gym gymTrainer) {
        if (isTrainer()) {
            this.gymTrainer.remove(gymTrainer);
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

    public Set<User> getClients() {
        if (isTrainer()) {
            return clients;
        }
        throw new IllegalStateException("El usuario no es entrenador.");
    }

    public void addClient(User client) {
        if (isTrainer()) {
            this.clients.add(client);
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public void addClients(Set<User> clients) {
        if (isTrainer()) {
            this.clients.addAll(clients);
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public void removeClient(User client) {
        if (isTrainer()) {
            this.clients.remove(client);
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public void removeClients(Set<User> clients) {
        if (isTrainer()) {
            this.clients.removeAll(clients);
        } else {
            throw new IllegalStateException("El usuario no es entrenador.");
        }
    }

    public Set<User> getTrainers() {
        return trainers;
    }

    public void addTrainer(User trainer) {
        this.trainers.add(trainer);
    }

    public void addTrainers(Set<User> trainers) {
        this.trainers.addAll(trainers);
    }

    public void removeTrainer(User trainer) {
        this.trainers.remove(trainer);
    }

    public void removeTrainers(Set<User> trainers) {
        this.trainers.removeAll(trainers);
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

        if (isTrainer() && !getGymTrainer().isEmpty()) {
            output.append(", gymTrainer=").append(getGymTrainer());
        }

        if (isAdministrator() && getGymManager() != null) {
            output.append(", gymManager=").append(getGymManager());
        }

        if (isTrainer() && !getClients().isEmpty()) {
            output.append(", clients=").append(getClients());
        }

        if (!getTrainers().isEmpty()) {
            output.append(", trainers=").append(getTrainers());
        }

        output.append('}');
        return output.toString();
    }
}

