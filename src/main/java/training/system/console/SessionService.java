package training.system.console;

import training.system.core.domain.user.User;

import java.util.Scanner;
import java.util.Set;

public class SessionService {
    private Scanner scanner;

    public SessionService(Scanner scanner) {
        this.scanner = scanner;
    }

    public User login(Set<User> users) {
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

    public void register(Set<User> users) {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese el apellido: ");
            String lastName = scanner.nextLine();
            System.out.print("Ingrese el correo electrónico: ");
            String email = scanner.nextLine();
            System.out.print("Ingrese la contraseña: ");
            String password = scanner.nextLine();

            if (users.stream().anyMatch(user -> user.getEmail().equals(email))) {
                throw new Exception("El correo electrónico ya está registrado.");
            }

            User user = new User(name, lastName, email, password);
            users.add(user);
            System.out.println("Usuario creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando usuario: " + e.getMessage());
        }
    }
}
