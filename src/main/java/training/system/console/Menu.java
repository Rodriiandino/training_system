package training.system.console;

import training.system.core.domain.category.Category;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.gym.Gym;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.user.Role;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private User userAuth = null;
    private List<User> users = new ArrayList<>();
    private List<Gym> gyms = new ArrayList<>();
    private List<Exercise> exercises = new ArrayList<>();
    private List<Routine> routines = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    private List<String> progress = new ArrayList<>();

    public void start() {
        int option;
        if (userAuth == null) {
            do {
                System.out.println("\n--- Sistema Integral de Gestión de Entrenamientos y Progreso Físico ---");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrarse");
                System.out.println("3. Salir");
                System.out.print("Ingrese su opción: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        userAuth = login();
                        if (userAuth != null) {
                            menu();
                        }
                        break;
                    case 2:
                        createUser();
                        break;
                    case 3:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor intente de nuevo.");
                }
            } while (option != 3);
        } else {
            menu();
        }
        scanner.close();
    }

    private User login() {
        System.out.print("Ingrese su correo electrónico: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Inicio de sesión exitoso.");
                return user;
            }
        }
        System.out.println("Correo electrónico o contraseña incorrectos.");
        return null;
    }

    private void menu() {
        int option;
        do {
            System.out.println("\n--- Training Management System ---");
            System.out.println("1. Ingresar al panel de usuario");
            System.out.println("2. Ingresar al panel de gimnasios");
            System.out.println("3. Ingresar al panel de ejercicios");
            System.out.println("4. Ingresar al panel de rutinas");
            System.out.println("5. Ingresar al panel de categorías");
            System.out.println("6. Ingresar al panel de notas");
            System.out.println("7. Ingresar al panel de progreso");
            System.out.println("8. Salir");
            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    panelUser();
                    break;
                case 2:
                    panelGym();
                    break;
                case 3:
                    panelExercise();
                    break;
                case 4:
                    panelRoutine();
                    break;
                case 5:
                    panelCategory();
                    break;
                case 6:
                    panelNote();
                    break;
                case 7:
                    panelProgress();
                    break;
                case 8:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 8);
    }

    private void createUser() {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese el apellido: ");
            String lastName = scanner.nextLine();
            System.out.print("Ingrese el correo electrónico: ");
            String email = scanner.nextLine();
            System.out.print("Ingrese la contraseña: ");
            String password = scanner.nextLine();

            User user = new User(name, lastName, email, password);
            users.add(user);
            System.out.println("Usuario creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando usuario: " + e.getMessage());
        }
    }

    private void panelUser() {
        int option;
        do {
            System.out.println("\n--- Panel de Usuario ---");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Modificar mis datos");
            System.out.println("3. Convertirme en entrenador");
            System.out.println("4. Convertirme en administrador");
            System.out.println("5. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println(userAuth.toString());
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    becomeTrainer();
                    break;
                case 4:
                    becomeAdministrator();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 5);
    }

    private void updateUser() {
        int option;
        do {
            System.out.println("\n--- Modificar Usuario ---");
            System.out.println("1. Modificar nombre");
            System.out.println("2. Modificar apellido");
            System.out.println("3. Modificar correo electrónico");
            System.out.println("4. Modificar contraseña");
            System.out.println("5. Mostrar mis datos");
            System.out.println("6. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre: ");
                    String name = scanner.nextLine();
                    userAuth.setName(name);
                    System.out.println("Nombre modificado exitosamente.");
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo apellido: ");
                    String lastName = scanner.nextLine();
                    userAuth.setLastName(lastName);
                    System.out.println("Apellido modificado exitosamente.");
                    break;
                case 3:
                    System.out.print("Ingrese el nuevo correo electrónico: ");
                    String email = scanner.nextLine();
                    userAuth.setEmail(email);
                    System.out.println("Correo electrónico modificado exitosamente.");
                    break;
                case 4:
                    System.out.print("Ingrese la nueva contraseña: ");
                    String password = scanner.nextLine();
                    userAuth.setPassword(password);
                    System.out.println("Contraseña modificada exitosamente.");
                    break;
                case 5:
                    System.out.println(userAuth.toString());
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 6);
    }

    private void becomeTrainer() {
        if (userAuth.isTrainer()) {
            System.out.println("Ya eres un entrenador.");
            return;
        }
        userAuth.addRole(new Role(RoleEnum.ROLE_TRAINER));
        System.out.println("Ahora eres un entrenador.");
    }

    private void becomeAdministrator() {
        if (userAuth.isAdministrator()) {
            System.out.println("Ya eres un administrador.");
            return;
        }
        userAuth.addRole(new Role(RoleEnum.ROLE_ADMINISTRATOR));
        System.out.println("Ahora eres un administrador.");
    }

    private void panelGym() {
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
                    createGym();
                    break;
                case 2:
                    addTrainerToGym();
                    break;
                case 3:
                    addClientToGym();
                    break;
                case 4:
                    linkTrainerWithClient();
                    break;
                case 5:
                    viewUsers();
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
                    userAuth.getGymManager().toString();
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 7);
    }

    private void createGym() {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para crear un gimnasio.");
            return;
        }

        try {
            System.out.print("Ingrese el nombre del gimnasio: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la dirección: ");
            String address = scanner.nextLine();

            Gym gym = new Gym(name, address);
            gyms.add(gym);
            userAuth.setGymManager(gym);
            System.out.println("Gimnasio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando gimnasio: " + e.getMessage());
        }
    }

    private void addTrainerToGym() {
        if (!userAuth.isAdministrator()) {
            System.out.println("No tienes permisos para añadir un entrenador a un gimnasio.");
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

    private void addClientToGym() {
        if (!userAuth.isTrainer()) {
            System.out.println("No tienes permisos para añadir un cliente a un gimnasio.");
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
            System.out.println("Cliente añadido al gimnasio exitosamente.");
        } catch (Exception e) {
            System.out.println("Error añadiendo cliente al gimnasio: " + e.getMessage());
        }
    }

    private void linkTrainerWithClient() {
        if (!userAuth.isTrainer()) {
            System.out.println("No tienes permisos para vincular un entrenador con un cliente.");
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

    private void panelExercise() {
    }

    private void createExercise() {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.print("Ingrese la explicación: ");
            String explanation = scanner.nextLine();
            System.out.print("Ingrese la URL del video: ");
            String videoUrl = scanner.nextLine();
            System.out.print("¿Es predefinido? (S/N): ");
            boolean isPredefined = scanner.nextLine().equalsIgnoreCase("S");

            Exercise exercise = new Exercise(name, description, explanation, videoUrl, isPredefined);
            exercises.add(exercise);
            System.out.println("Ejercicio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando ejercicio: " + e.getMessage());
        }
    }

    private void panelRoutine() {
    }

    private void panelCategory() {
    }

    private void panelNote() {
    }

    private void panelProgress() {
    }

    private void viewUsers() {
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
