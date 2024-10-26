package training.system.console;

import training.system.console.tools.InputUtils;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.progress.Progress;
import training.system.core.domain.user.User;

import java.util.Date;
import java.util.Scanner;
import java.util.Set;

public class ProgressPanel {

    private static Scanner scanner;

    public ProgressPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelProgress(Set<Exercise> exercises, Set<Progress> progress, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Progreso ---");
            System.out.println("1. Crear progreso");
            System.out.println("2. Ver progresos");
            System.out.println("3. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createProgress(exercises, progress, userAuth);
                    break;
                case 2:
                    if (progress.isEmpty()) {
                        System.out.println("No hay progresos registrados.");
                    } else {
                        System.out.println("\n--- Progresos registrados ---");
                        for (Progress p : progress) {
                            System.out.println(p);
                        }
                    }
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 3);
    }

    private void createProgress(Set<Exercise> exercises, Set<Progress> progress, User userAuth) {
        try {
            System.out.print("Ingrese las repeticiones: ");
            int repetitions = scanner.nextInt();
            System.out.print("Ingrese el peso: ");
            int weight = scanner.nextInt();
            System.out.print("Ingrese el tiempo: ");
            int time = scanner.nextInt();
            System.out.print("Ingrese el nombre del ejercicio: ");
            String exerciseName = scanner.nextLine();
            Exercise exercise = exercises.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);
            if (exercise == null) {
                System.out.println("No se encontró el ejercicio.");
                return;
            }
            Progress p = new Progress(new Date(), repetitions, weight, time, userAuth, exercise);
            progress.add(p);
            System.out.println("Progreso creado exitosamente.");

        } catch (Exception e) {
            System.out.println("Error creando progreso: " + e.getMessage());
        }
    }
}
