package training.system.core.domain.gym;

import training.system.core.domain.user.User;

import java.util.HashSet;
import java.util.Set;

public class Gym {
    private int id;
    private String name;
    private String address;
    private Set<User> clients = new HashSet<>();
    private Set<User> trainers = new HashSet<>();
    private Set<User> managers;

    public Gym(String name, String address, Set<User> managers) {
        this.name = name;
        this.address = address;
        this.managers = managers;
    }

    public Gym(int id, String name, String address, Set<User> clients, Set<User> trainers, Set<User> managers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.clients = clients;
        this.trainers = trainers;
        this.managers = managers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<User> getClients() {
        return clients;
    }

    public Set<User> getTrainers() {
        return trainers;
    }

    public Set<User> getManagers() {
        return managers;
    }

    public void addClient(User client) {
        this.clients.add(client);
    }

    public void addClients(Set<User> clients) {
        this.clients.addAll(clients);
    }

    public void addTrainer(User trainer) {
        this.trainers.add(trainer);
    }

    public void addTrainers(Set<User> trainers) {
        this.trainers.addAll(trainers);
    }

    public void addManager(User manager) {
        this.managers.add(manager);
    }

    public void addManagers(Set<User> managers) {
        this.managers.addAll(managers);
    }

    public void removeClient(User client) {
        this.clients.remove(client);
    }

    public void removeClients(Set<User> clients) {
        this.clients.removeAll(clients);
    }

    public void removeTrainer(User trainer) {
        this.trainers.remove(trainer);
    }

    public void removeTrainers(Set<User> trainers) {
        this.trainers.removeAll(trainers);
    }

    public void removeManager(User manager) {
        this.managers.remove(manager);
    }

    public void removeManagers(Set<User> managers) {
        this.managers.removeAll(managers);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Gym{");
        output.append("id=").append(id);
        output.append(", name='").append(name).append('\'');
        output.append(", address='").append(address).append('\'');
        if (!clients.isEmpty()) {
            output.append(", clients=[");
            clients.forEach(client -> output.append(client.getName()).append(", "));
            output.setLength(output.length() - 2);
            output.append("]");
        }
        if (!trainers.isEmpty()) {
            output.append(", trainers=[");
            trainers.forEach(trainer -> output.append(trainer.getName()).append(", "));
            output.setLength(output.length() - 2);
            output.append("]");
        }
        output.append(", managers=[");
        managers.forEach(manager -> output.append(manager.getName()).append(", "));
        output.setLength(output.length() - 2);
        output.append("]");
        output.append('}');
        return output.toString();
    }
}
