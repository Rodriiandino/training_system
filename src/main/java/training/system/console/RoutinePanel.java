package training.system.console;

import training.system.console.tools.ClientValidator;
import training.system.console.tools.InputUtils;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.user.User;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class RoutinePanel {
    private final Scanner scanner;

    public RoutinePanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelRoutine(Set<Routine> routines, Set<Exercise> exercises, Set<User> clients, User userAuth) {
        if (userAuth.isTrainer()) {
            panelRoutineTrainer(routines, exercises, clients, userAuth);
        } else {
            panelRoutineUser(routines, exercises, userAuth);
        }
    }

    private void panelRoutineUser(Set<Routine> routines, Set<Exercise> exercises, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Rutinas ---");
            System.out.println("1. Crear rutina");
            System.out.println("2. Ver rutinas");
            System.out.println("3. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createRoutine(routines, exercises, userAuth);
                    break;
                case 2:
                    showRoutines(routines, userAuth);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 3);
    }

    private void panelRoutineTrainer(Set<Routine> routines, Set<Exercise> exercises, Set<User> clients, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Rutinas ---");
            System.out.println("1. Crear rutina");
            System.out.println("2. Ver rutinas");
            System.out.println("3. Crear rutina para usuario");
            System.out.println("4. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createRoutine(routines, exercises, userAuth);
                    break;
                case 2:
                    showRoutines(routines, userAuth);
                    break;
                case 3:
                    createRoutineForUser(routines, exercises, clients, userAuth);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 4);
    }

    private void createRoutineForUser(Set<Routine> routines, Set<Exercise> exercises, Set<User> clients, User userAuth) {
        try {
            User client = ClientValidator.selectAndValidateClient(scanner, userAuth, clients);

            if (client == null) {
                return;
            }

            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.println("Añadir ejercicios a la rutina");
            Set<Exercise> exercisesToAdd = new HashSet<>();
            Set<Exercise> exercisesByUser = new HashSet<>();
            for (Exercise exercise : exercises) {
                boolean isVisibleToUser = exercise.getIsPredefined() || userAuth.equals(exercise.getUser());

                if (isVisibleToUser) {
                    exercisesByUser.add(exercise);
                }
            }

            int option;
            do {
                System.out.println("1. Añadir ejercicio");
                System.out.println("2. Ver ejercicios por añadir");
                System.out.println("3. Eliminar ejercicio");
                System.out.println("4. Guardar ejercicios añadidos y salir");
                System.out.println("5. Cancelar");

                option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

                switch (option) {
                    case 1:
                        System.out.print("Ingrese el nombre del ejercicio a añadir: ");
                        String exerciseName = scanner.nextLine();
                        Exercise exerciseToAdd = exercisesByUser.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);
                        if (exerciseToAdd == null) {
                            System.out.println("No se encontró el ejercicio.");
                        } else {
                            exercisesToAdd.add(exerciseToAdd);
                            System.out.println("Ejercicio añadido exitosamente.");
                        }
                        break;
                    case 2:
                        System.out.println("Ejercicios por añadir");
                        if (exercisesToAdd.isEmpty()) {
                            System.out.println("No hay ejercicios añadidos.");
                        } else {
                            for (Exercise exercise : exercisesToAdd) {
                                System.out.println(exercise.toString());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el nombre del ejercicio a eliminar: ");
                        String exerciseNameToRemove = scanner.nextLine();
                        Exercise exerciseToRemove = exercisesToAdd.stream().filter(e -> e.getName().equals(exerciseNameToRemove)).findFirst().orElse(null);
                        if (exerciseToRemove == null) {
                            System.out.println("No se encontró el ejercicio.");
                        } else {
                            exercisesToAdd.remove(exerciseToRemove);
                            System.out.println("Ejercicio eliminado exitosamente.");
                        }
                        break;
                    case 4:
                        System.out.println("Guardando ejercicios añadidos...");
                        break;
                    case 5:
                        exercisesToAdd.clear();
                        System.out.println("Cancelando...");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor intente de nuevo.");
                }
            } while (option != 4 && option != 5);

            Routine routine = new Routine(name, description, client, userAuth, exercisesToAdd);
            routines.add(routine);
            System.out.println("Rutina creada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando rutina: " + e.getMessage());
        }
    }

    private void createRoutine(Set<Routine> routines, Set<Exercise> exercises, User userAuth) {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.println("Añadir ejercicios a la rutina");
            Set<Exercise> exercisesToAdd = new HashSet<>();
            Set<Exercise> exercisesByUser = new HashSet<>();
            for (Exercise exercise : exercises) {
                boolean isVisibleToUser = exercise.getIsPredefined() || userAuth.equals(exercise.getUser());

                if (isVisibleToUser) {
                    exercisesByUser.add(exercise);
                }
            }

            int option;
            do {
                System.out.println("1. Añadir ejercicio");
                System.out.println("2. Ver ejercicios por añadir");
                System.out.println("3. Eliminar ejercicio");
                System.out.println("4. Guardar ejercicios añadidos y salir");
                System.out.println("5. Cancelar");

                option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

                switch (option) {
                    case 1:
                        System.out.print("Ingrese el nombre del ejercicio a añadir: ");
                        String exerciseName = scanner.nextLine();
                        Exercise exerciseToAdd = exercisesByUser.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);
                        if (exerciseToAdd == null) {
                            System.out.println("No se encontró el ejercicio.");
                        } else {
                            exercisesToAdd.add(exerciseToAdd);
                            System.out.println("Ejercicio añadido exitosamente.");
                        }
                        break;
                    case 2:
                        System.out.println("Ejercicios por añadir");
                        if (exercisesToAdd.isEmpty()) {
                            System.out.println("No hay ejercicios añadidos.");
                        } else {
                            for (Exercise exercise : exercisesToAdd) {
                                System.out.println(exercise.toString());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el nombre del ejercicio a eliminar: ");
                        String exerciseNameToRemove = scanner.nextLine();
                        Exercise exerciseToRemove = exercisesToAdd.stream().filter(e -> e.getName().equals(exerciseNameToRemove)).findFirst().orElse(null);
                        if (exerciseToRemove == null) {
                            System.out.println("No se encontró el ejercicio.");
                        } else {
                            exercisesToAdd.remove(exerciseToRemove);
                            System.out.println("Ejercicio eliminado exitosamente.");
                        }
                        break;
                    case 4:
                        System.out.println("Guardando ejercicios añadidos...");
                        break;
                    case 5:
                        exercisesToAdd.clear();
                        System.out.println("Cancelando...");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor intente de nuevo.");
                }
            } while (option != 4 && option != 5);

            Routine routine = new Routine(name, description, userAuth, exercisesToAdd);
            routines.add(routine);
            System.out.println("Rutina creada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando rutina: " + e.getMessage());
        }
    }

    private void showRoutines(Set<Routine> routines, User userAuth) {
        Set<Routine> routinesByUser = new HashSet<>();

        for (Routine routine : routines) {
            if (userAuth.equals(routine.getUser())) {
                routinesByUser.add(routine);
            }
        }

        if (routinesByUser.isEmpty()) {
            System.out.println("No hay rutinas registradas.");
        } else {
            System.out.println("\n--- Rutinas registradas ---");
            for (Routine routine : routinesByUser) {
                System.out.println(routine);
            }
        }
    }
}
