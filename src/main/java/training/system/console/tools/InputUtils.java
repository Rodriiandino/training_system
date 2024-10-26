package training.system.console.tools;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtils {
    /**
     * Método para validar y obtener una entrada numérica entera del usuario.
     *
     * @param scanner Scanner para leer la entrada del usuario.
     * @param prompt Mensaje de solicitud para mostrar al usuario.
     * @return Un número entero válido ingresado por el usuario.
     */
    public static int getValidatedInput(Scanner scanner, String prompt) {
        int input = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        }
        return input;
    }
}
