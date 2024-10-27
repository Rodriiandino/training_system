package training.system.console;

import training.system.console.tools.InputUtils;
import training.system.core.domain.user.Role;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;

import java.util.Scanner;

public class UserPanel {
    private Scanner scanner;

    public UserPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelUser(User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Usuario ---");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Modificar mis datos");
            System.out.println("3. Convertirme en entrenador");
            System.out.println("4. Convertirme en administrador");
            System.out.println("5. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    System.out.println(userAuth.toString());
                    break;
                case 2:
                    updateUser(userAuth);
                    break;
                case 3:
                    becomeTrainer(userAuth);
                    break;
                case 4:
                    becomeAdministrator(userAuth);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 5);
    }


    private void updateUser(User userAuth) {
        int option;
        do {
            System.out.println("\n--- Modificar Usuario ---");
            System.out.println("1. Modificar nombre");
            System.out.println("2. Modificar apellido");
            System.out.println("3. Modificar correo electrónico");
            System.out.println("4. Modificar contraseña");
            System.out.println("5. Mostrar mis datos");
            System.out.println("6. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

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

    private void becomeTrainer(User userAuth) {
        if (userAuth.isTrainer()) {
            System.out.println("Ya eres un entrenador.");
            return;
        }
        userAuth.addRole(new Role(RoleEnum.ROLE_TRAINER));
        System.out.println("Ahora eres un entrenador.");
    }

    private void becomeAdministrator(User userAuth) {
        if (userAuth.isAdministrator()) {
            System.out.println("Ya eres un administrador.");
            return;
        }
        userAuth.addRole(new Role(RoleEnum.ROLE_ADMINISTRATOR));
        System.out.println("Ahora eres un administrador.");
    }
}


