package training.system.console;

import training.system.core.domain.gym.Gym;
import training.system.core.domain.user.User;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GymPanel {
    private static Scanner scanner;

    public GymPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelGym(Set<User> users, User userAuth, Set<Gym> gyms) {
        int option;
        do {
            System.out.println("\n--- Panel de Gimnasios ---");
            System.out.println("1. Crear gimnasio");
            System.out.println("2. Añadir entrenador a gimnasio");
            System.out.println("3. Añadir cliente a gimnasio");
            System.out.println("4. Vincular entrenador con cliente");
            System.out.println("5. Ver usuarios");
            System.out.println("6. Ver datos del gimnasio");
            System.out.println("7. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createGym(userAuth, gyms);
                    break;
                case 2:
                    addTrainerToGym(userAuth, users);
                    break;
                case 3:
                    addClientToGym(userAuth, users);
                    break;
                case 4:
                    linkTrainerWithClient(userAuth);
                    break;
                case 5:
                    viewUsers(users);
                    break;
                case 6:
                    if (!userAuth.isAdministrator()) {
                        System.out.println("No sos administrador.");
                        break;
                    }
                    if (userAuth.getGymManager() == null) {
                        System.out.println("No tenés un gimnasio asignado.");
                        break;
                    }
                    System.out.println(userAuth.getGymManager().toString());
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 7);
    }

    private void createGym(User userAuth, Set<Gym> gyms) {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para crear un gimnasio.");
            return;
        }

        try {
            System.out.print("Ingrese el nombre del gimnasio: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la dirección: ");
            String address = scanner.nextLine();

            Set<User> managers = new HashSet<>();
            managers.add(userAuth);
            Gym gym = new Gym(name, address, managers);
            gyms.add(gym);
            userAuth.setGymManager(gym);
            System.out.println("Gimnasio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando gimnasio: " + e.getMessage());
        }
    }

    private void addTrainerToGym(User userAuth, Set<User> users) {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para añadir un entrenador a un gimnasio.");
            return;
        }
        if (userAuth.getGymManager() == null) {
            System.out.println("No tenés un gimnasio asignado.");
            return;
        }
        Gym gym = userAuth.getGymManager();
        try {
            System.out.print("Ingrese el correo electrónico del entrenador: ");
            String email = scanner.nextLine();
            User trainer = users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
            if (trainer == null || !trainer.isTrainer()) {
                System.out.println("No se encontró el entrenador o no es un entrenador.");
                return;
            }
            gym.addTrainer(trainer);
            System.out.println("Entrenador añadido al gimnasio exitosamente.");
        } catch (Exception e) {
            System.out.println("Error añadiendo entrenador al gimnasio: " + e.getMessage());
        }
    }

    private void addClientToGym(User userAuth, Set<User> users) {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para añadir un cliente a un gimnasio.");
            return;
        }
        if (userAuth.getGymManager() == null) {
            System.out.println("No tenés un gimnasio asignado.");
            return;
        }
        Gym gym = userAuth.getGymManager();
        try {
            System.out.print("Ingrese el correo electrónico del cliente: ");
            String email = scanner.nextLine();
            User client = users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
            if (client == null) {
                System.out.println("No se encontró el cliente.");
                return;
            }
            gym.addClient(client);
            client.setGymTraining(gym);
            System.out.println("Cliente añadido al gimnasio exitosamente.");
        } catch (Exception e) {
            System.out.println("Error añadiendo cliente al gimnasio: " + e.getMessage());
        }
    }

    private void linkTrainerWithClient(User userAuth) {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para vincular un entrenador con un cliente.");
            return;
        }
        if (userAuth.getGymManager() == null) {
            System.out.println("No tenés un gimnasio asignado.");
            return;
        }
        Gym gym = userAuth.getGymManager();
        try {
            System.out.print("Ingrese el correo electrónico del entrenador: ");
            String emailTrainer = scanner.nextLine();
            User trainer = gym.getTrainers().stream().filter(u -> u.getEmail().equals(emailTrainer)).findFirst().orElse(null);
            if (trainer == null) {
                System.out.println("No se encontró el entrenador en el gimnasio.");
                return;
            }
            System.out.print("Ingrese el correo electrónico del cliente: ");
            String emailClient = scanner.nextLine();
            User client = gym.getClients().stream().filter(u -> u.getEmail().equals(emailClient)).findFirst().orElse(null);
            if (client == null) {
                System.out.println("No se encontró el cliente.");
                return;
            }
            trainer.addClient(client);
            System.out.println("Entrenador vinculado con cliente exitosamente.");
        } catch (Exception e) {
            System.out.println("Error vinculando entremador con cliente: " + e.getMessage());
        }
    }

    private void viewUsers(Set<User> users) {
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.println("\n--- Usuarios registrados ---");
            for (User user : users) {
                System.out.println(user.toString());
            }
        }
    }
}
