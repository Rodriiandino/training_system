package training.system.core.domain.gym;

import training.system.core.domain.user.User;

import java.util.List;

public class Gym {
    private int id;
    private String name;
    private String address;
    private List<User> clients;
    private List<User> trainers;
    private List<User> managers;

    public Gym(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Gym(int id, String name, String address, List<User> clients, List<User> trainers, List<User> managers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.clients = clients;
        this.trainers = trainers;
        this.managers = managers;
    }

    @Override
    public String toString() {
        return "Gym{" + "id=" + id + ", name='" + name + '\'' + ", address='" + address + '\'' + ", clients=" + clients + ", trainers=" + trainers + ", managers=" + managers + '}';
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

    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        this.clients = clients;
    }

    public List<User> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<User> trainers) {
        this.trainers = trainers;
    }

    public List<User> getManagers() {
        return managers;
    }

    public void setManagers(List<User> managers) {
        this.managers = managers;
    }
}
