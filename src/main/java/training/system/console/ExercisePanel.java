package training.system.console;

import training.system.console.tools.ClientValidator;
import training.system.console.tools.InputUtils;
import training.system.core.domain.category.Category;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExercisePanel {
    private Scanner scanner;

    public ExercisePanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelExercise(Set<Exercise> exercises, Set<Category> categories, Set<User> clients, User userAuth) {
        if (userAuth.isTrainer()) {
            panelExerciseTrainer(exercises, categories, clients, userAuth);
        } else {
            panelExerciseUser(exercises, categories, userAuth);
        }
    }

    private void panelExerciseUser(Set<Exercise> exercises, Set<Category> categories, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Ejercicios ---");
            System.out.println("1. Crear ejercicio");
            System.out.println("2. Ver ejercicios");
            System.out.println("3. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createExercise(categories, exercises, userAuth);
                    break;
                case 2:
                    showExercises(exercises, userAuth);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 3);
    }

    private void panelExerciseTrainer(Set<Exercise> exercises, Set<Category> categories, Set<User> clients, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Ejercicios ---");
            System.out.println("1. Crear ejercicio");
            System.out.println("2. Ver ejercicios");
            System.out.println("3. Crear ejercicio para cliente");
            System.out.println("4. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createExercise(categories, exercises, userAuth);
                    break;
                case 2:
                    showExercises(exercises, userAuth);
                    break;
                case 3:
                    createExerciseForClient(categories, exercises, clients, userAuth);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 4);
    }

    private void createExerciseForClient(Set<Category> categories, Set<Exercise> exercises, Set<User> clients, User userAuth) {
        try {
            User client = ClientValidator.selectAndValidateClient(scanner, userAuth, clients);

            if (client == null) {
                return;
            }

            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.print("Ingrese la explicación: ");
            String explanation = scanner.nextLine();
            System.out.print("Ingrese la URL del video: ");
            String videoUrl = scanner.nextLine();
            int option;
            Set<Category> categoriesToAdd = new HashSet<>();
            do {
                System.out.println("Ingrese una opción: ");
                System.out.println("1. Añadir categorías al ejercicio");
                System.out.println("2. Ver categorias");
                System.out.println("3. Salir");

                option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

                switch (option) {
                    case 1:
                        categoriesToAdd = addCategoryToExercise(categories);
                        break;
                    case 2:
                        if (categories.isEmpty()) {
                            System.out.println("No hay categorías registradas.");
                        } else {
                            System.out.println("\n--- Categorías registradas ---");
                            for (Category category : categories) {
                                System.out.println(category.toString());
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

            Exercise exercise = new Exercise(name, description, explanation, videoUrl, false, categoriesToAdd, client, userAuth);
            exercises.add(exercise);
            System.out.println("Ejercicio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando ejercicio: " + e.getMessage());
        }
    }

    private void createExercise(Set<Category> categories, Set<Exercise> exercises, User userAuth) {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.print("Ingrese la explicación: ");
            String explanation = scanner.nextLine();
            System.out.print("Ingrese la URL del video: ");
            String videoUrl = scanner.nextLine();
            int option;
            Set<Category> categoriesToAdd = new HashSet<>();
            do {
                System.out.println("Ingrese una opción: ");
                System.out.println("1. Añadir categorías al ejercicio");
                System.out.println("2. Ver categorias");
                System.out.println("3. Salir");

                option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

                switch (option) {
                    case 1:
                        categoriesToAdd = addCategoryToExercise(categories);
                        break;
                    case 2:
                        if (categories.isEmpty()) {
                            System.out.println("No hay categorías registradas.");
                        } else {
                            System.out.println("\n--- Categorías registradas ---");
                            for (Category category : categories) {
                                System.out.println(category.toString());
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

            Exercise exercise = new Exercise(name, description, explanation, videoUrl, false, categoriesToAdd, userAuth);
            exercises.add(exercise);
            System.out.println("Ejercicio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando ejercicio: " + e.getMessage());
        }
    }

    private Set<Category> addCategoryToExercise(Set<Category> categories) {
        Set<Category> categoriesToAdd = new HashSet<>();
        int option;
        do {
            System.out.println("Añadir Categorias al ejercicio");
            System.out.println("1. Añadir categoría");
            System.out.println("2. Ver categorias por añadir");
            System.out.println("3. Eliminar categoría");
            System.out.println("4. Guardar categorías añadidas y salir");
            System.out.println("5. Cancelar");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    System.out.print("Ingrese el nombre de la categoría a añadir: ");
                    String categoryName = scanner.nextLine();
                    Category categoryToAdd = categories.stream().filter(c -> c.getName().equals(categoryName)).findFirst().orElse(null);
                    if (categoryToAdd == null) {
                        System.out.println("No se encontró la categoría.");
                    } else {
                        categoriesToAdd.add(categoryToAdd);
                        System.out.println("Categoría añadida exitosamente.");
                    }
                    break;
                case 2:
                    System.out.println("Categorias por añadir");
                    if (categoriesToAdd.isEmpty()) {
                        System.out.println("No hay categorías añadidas.");
                    } else {
                        for (Category category : categoriesToAdd) {
                            System.out.println(category.toString());
                        }
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el id de la categoría a eliminar: ");
                    int categoryIdToRemove = scanner.nextInt();
                    Category categoryToRemove = categoriesToAdd.stream().filter(c -> c.getId() == categoryIdToRemove).findFirst().orElse(null);
                    if (categoryToRemove == null) {
                        System.out.println("No se encontró la categoría.");
                    } else {
                        categoriesToAdd.remove(categoryToRemove);
                        System.out.println("Categoría eliminada exitosamente.");
                    }
                    break;
                case 4:
                    System.out.println("Guardando categorías añadidas...");
                    break;
                case 5:
                    categoriesToAdd.clear();
                    System.out.println("Cancelando...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 4 && option != 5);

        return categoriesToAdd;
    }

    private void showExercises(Set<Exercise> exercises, User userAuth) {
        Set<Exercise> exercisesByUser = new HashSet<>();

        for (Exercise exercise : exercises) {
            boolean isVisibleToUser = exercise.getIsPredefined() || userAuth.equals(exercise.getUser());

            if (isVisibleToUser) {
                exercisesByUser.add(exercise);
            }
        }

        if (exercisesByUser.isEmpty()) {
            System.out.println("No hay ejercicios registrados.");
        } else {
            System.out.println("\n--- Ejercicios registrados ---");
            for (Exercise exercise : exercisesByUser) {
                System.out.println(exercise);
            }
        }
    }
}


