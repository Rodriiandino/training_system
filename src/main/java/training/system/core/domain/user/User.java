package training.system.core.domain.user;

import training.system.core.domain.gym.Gym;

import java.util.HashSet;
import java.util.Set;

public class User extends Person {
    /**
     * Los roles de un usuario.
     */
    private Set<Role> roles = new HashSet<>();
    /**
     * El gimnasio en el que entrena el usuario.
     */
    private Gym gymTraining;
    /**
     * Los gimnasios en los que trabaja el usuario.
     */
    private Set<Gym> gymTrainer = new HashSet<>();
    /**
     * El gimnasio que administra el usuario.
     */
    private Gym gymManager;
    /**
     * Los clientes del usuario.
     */
    private Set<User> clients = new HashSet<>();
    /**
     * Los entrenadores del usuario.
     */
    private Set<User> trainers = new HashSet<>();


    public User() {
        super();
    }

    public User(String name, String lastName, String email, String password) {
        super(name, lastName, email, password);
        this.roles.add(new Role(RoleEnum.ROLE_USER));
    }

    public User(Long id, String name, String lastName, String email, String password, Set<Role> roles) {
        super(id, name, lastName, email, password);
        this.roles = roles;
    }

    public User(Long id, String name, String lastName, String email, String password, Set<Role> roles, Gym gymTraining, Set<User> trainers) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymTraining = gymTraining;
        this.trainers = trainers;
    }

    public User(Long id, String name, String lastName, String email, String password, Set<Role> roles, Set<Gym> gymTrainer, Set<User> clients) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymTrainer = gymTrainer;
        this.clients = clients;
    }

    public User(Long id, String name, String lastName, String email, String password, Set<Role> roles, Gym gymManager) {
        super(id, name, lastName, email, password);
        this.roles = roles;
        this.gymManager = gymManager;
    }

    public User(Long id, String name, String lastName, String email) {
        super(id, name, lastName, email);
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
            output.append(", gymTrainingName='").append(getGymTraining().getName()).append('\'');
        }

        if (isTrainer() && !getGymTrainer().isEmpty()) {
            output.append(", gymTrainerNames=[");
            getGymTrainer().forEach(gym -> output.append(gym.getName()).append(", "));
            output.setLength(output.length() - 2);
            output.append(']');
        }

        if (isAdministrator() && getGymManager() != null) {
            output.append(", gymManagerName='").append(getGymManager().getName()).append('\'');
        }

        if (isTrainer() && !getClients().isEmpty()) {
            output.append(", clientNames=[");
            getClients().forEach(client -> output.append(client.getName()).append(", "));
            output.setLength(output.length() - 2);
            output.append(']');
        }

        if (!getTrainers().isEmpty()) {
            output.append(", trainerNames=[");
            getTrainers().forEach(trainer -> output.append(trainer.getName()).append(", "));
            output.setLength(output.length() - 2);
            output.append(']');
        }

        output.append('}');
        return output.toString();
    }
}

