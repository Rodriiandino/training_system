package training.system.console;

import training.system.console.tools.ClientValidator;
import training.system.console.tools.InputUtils;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.progress.Progress;
import training.system.core.domain.user.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProgressPanel {

    private Scanner scanner;

    public ProgressPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelProgress(Set<Exercise> exercises, Set<Progress> progress, Set<User> clients, User userAuth) {
        if (userAuth.isTrainer()) {
            panelProgressTrainer(exercises, progress, clients, userAuth);
        } else {
            panelProgressUser(exercises, progress, userAuth);
        }
    }

    private void panelProgressUser(Set<Exercise> exercises, Set<Progress> progress, User userAuth) {
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
                    showProgress(progress, userAuth);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 3);
    }

    private void panelProgressTrainer(Set<Exercise> exercises, Set<Progress> progress, Set<User> clients, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Progreso ---");
            System.out.println("1. Crear progreso");
            System.out.println("2. Ver progresos");
            System.out.println("3. Crear progreso para usuario");
            System.out.println("4. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createProgress(exercises, progress, userAuth);
                    break;
                case 2:
                    showProgress(progress, userAuth);
                    break;
                case 3:
                    createProgressForUser(exercises, progress, clients, userAuth);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 4);
    }

    private void createProgressForUser(Set<Exercise> exercises, Set<Progress> progress, Set<User> clients, User userAuth) {
        try {
            User client = ClientValidator.selectAndValidateClient(scanner, userAuth, clients);

            if (client == null) {
                return;
            }

            System.out.print("Ingrese las repeticiones: ");
            int repetitions = scanner.nextInt();
            System.out.print("Ingrese el peso: ");
            int weight = scanner.nextInt();
            System.out.print("Ingrese el tiempo: ");
            int time = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese el nombre del ejercicio: ");
            String exerciseName = scanner.nextLine();

            Set<Exercise> exercisesByUser = new HashSet<>();
            for (Exercise exercise : exercises) {
                boolean isVisibleToUser = exercise.getIsPredefined() || userAuth.equals(exercise.getUser());

                if (isVisibleToUser) {
                    exercisesByUser.add(exercise);
                }
            }

            if (exercisesByUser.isEmpty()) {
                System.out.println("No hay ejercicios registrados.");
                return;
            }

            Exercise exercise = exercisesByUser.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);

            if (exercise == null) {
                System.out.println("No se encontró el ejercicio.");
                return;
            }

            Progress p = new Progress(new Date(), repetitions, weight, time, client, userAuth, exercise);
            progress.add(p);
            System.out.println("Progreso creado exitosamente.");

        } catch (Exception e) {
            System.out.println("Error creando progreso: " + e.getMessage());
        }
    }

    private void createProgress(Set<Exercise> exercises, Set<Progress> progress, User userAuth) {
        try {
            System.out.print("Ingrese las repeticiones: ");
            int repetitions = scanner.nextInt();
            System.out.print("Ingrese el peso: ");
            int weight = scanner.nextInt();
            System.out.print("Ingrese el tiempo: ");
            int time = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese el nombre del ejercicio: ");
            String exerciseName = scanner.nextLine();

            Set<Exercise> exercisesByUser = new HashSet<>();
            for (Exercise exercise : exercises) {
                boolean isVisibleToUser = exercise.getIsPredefined() || userAuth.equals(exercise.getUser());

                if (isVisibleToUser) {
                    exercisesByUser.add(exercise);
                }
            }

            if (exercisesByUser.isEmpty()) {
                System.out.println("No hay ejercicios registrados.");
                return;
            }

            Exercise exercise = exercisesByUser.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);

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

    private void showProgress(Set<Progress> progress, User userAuth) {
        Set<Progress> progressByUser = new HashSet<>();

        for (Progress p : progress) {
            if (userAuth.equals(p.getUser())) {
                progressByUser.add(p);
            }
        }

        if (progressByUser.isEmpty()) {
            System.out.println("No hay progresos registrados.");
        } else {
            System.out.println("\n--- Progresos registrados ---");
            for (Progress p : progressByUser) {
                System.out.println(p);
            }
        }
    }
}
