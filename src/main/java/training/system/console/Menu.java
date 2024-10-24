package training.system.console;

import training.system.core.domain.category.Category;
import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.gym.Gym;
import training.system.core.domain.note.Note;
import training.system.core.domain.progress.Progress;
import training.system.core.domain.routine.Routine;
import training.system.core.domain.user.Role;
import training.system.core.domain.user.RoleEnum;
import training.system.core.domain.user.User;

import java.util.*;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private User userAuth = null;
    private Set<User> users = new HashSet<>();
    private Set<Gym> gyms = new HashSet<>();
    private Set<Exercise> exercises = new HashSet<>();
    private Set<Routine> routines = new HashSet<>();
    private Set<Category> categories = new HashSet<>();
    private Set<Note> notes = new HashSet<>();
    private Set<Progress> progress = new HashSet<>();

    private UserPanel userPanel = new UserPanel(scanner);
    private GymPanel gymPanel = new GymPanel(scanner);

    public void start() {
        initializeData();

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

    public void initializeData() {
        User user1 = new User("rodrigo", "andino", "andino@", "1234");
        user1.addRole(new Role(RoleEnum.ROLE_ADMINISTRATOR));
        User user2 = new User("gonzalo", "sanchez", "sanchez@", "1234");
        user2.addRole(new Role(RoleEnum.ROLE_TRAINER));
        User user3 = new User("mili", "gomez", "gomez@", "1234");

        Category category1 = new Category("piernas", "ejercicios de piernas");
        Category category2 = new Category("brazos", "ejercicios de brazos");
        Category category3 = new Category("abdominales", "ejercicios de abdominales");

        Exercise exercise1 = new Exercise("sentadillas", "ejercicio de piernas", "ejercicio de piernas", "URL", true, Set.of(category1));
        Exercise exercise2 = new Exercise("flexiones", "ejercicio de brazos", "ejercicio de brazos", "URL", true, Set.of(category2));
        Exercise exercise3 = new Exercise("plancha", "ejercicio de abdominales", "ejercicio de abdominales", "URL", true, Set.of(category3));

        users.addAll(List.of(user1, user2, user3));
        categories.addAll(List.of(category1, category2, category3));
        exercises.addAll(List.of(exercise1, exercise2, exercise3));
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
                    userPanel.panelUser(userAuth);
                    break;
                case 2:
                    gymPanel.panelGym(users, userAuth, gyms);
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

    private void panelExercise() {
        int option;
        do {
            System.out.println("\n--- Panel de Ejercicios ---");
            System.out.println("1. Crear ejercicio");
            System.out.println("2. Ver ejercicios");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createExercise();
                    break;
                case 2:
                    if (exercises.isEmpty()) {
                        System.out.println("No hay ejercicios registrados.");
                    } else {
                        System.out.println("\n--- Ejercicios registrados ---");
                        for (Exercise exercise : exercises) {
                            System.out.println(exercise.toString());
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
            int option;
            Set<Category> categoriesToAdd = new HashSet<>();
            do {
                System.out.println("Ingrese una opción: ");
                System.out.println("1. Añadir categorías al ejercicio");
                System.out.println("2. Ver categorias");
                System.out.println("3. Salir");

                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        categoriesToAdd = addCategoryToExercise();
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

            Exercise exercise = new Exercise(name, description, explanation, videoUrl, false, categoriesToAdd);
            exercises.add(exercise);
            System.out.println("Ejercicio creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando ejercicio: " + e.getMessage());
        }
    }

    private Set<Category> addCategoryToExercise() {
        Set<Category> categoriesToAdd = new HashSet<>();
        int option;
        do {
            System.out.println("Añadir Categorias al ejercicio");
            System.out.println("1. Añadir categoría");
            System.out.println("2. Ver categorias por añadir");
            System.out.println("3. Eliminar categoría");
            System.out.println("4. Guardar categorías añadidas y salir");
            System.out.println("5. Cancelar");

            option = scanner.nextInt();
            scanner.nextLine();
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

    private void panelRoutine() {
        int option;
        do {
            System.out.println("\n--- Panel de Rutinas ---");
            System.out.println("1. Crear rutina");
            System.out.println("2. Ver rutinas");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createRoutine();
                    break;
                case 2:
                    if (routines.isEmpty()) {
                        System.out.println("No hay rutinas registradas.");
                    } else {
                        System.out.println("\n--- Rutinas registradas ---");
                        for (Routine routine : routines) {
                            System.out.println(routine.toString());
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

    private void createRoutine() {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();
            System.out.print("Añadir ejercicios a la rutina");
            Set<Exercise> exercisesToAdd = new HashSet<>();
            int option;
            do {
                System.out.println("1. Añadir ejercicio");
                System.out.println("2. Ver ejercicios por añadir");
                System.out.println("3. Eliminar ejercicio");
                System.out.println("4. Guardar ejercicios añadidos y salir");
                System.out.println("5. Cancelar");

                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        System.out.print("Ingrese el nombre del ejercicio a añadir: ");
                        String exerciseName = scanner.nextLine();
                        Exercise exerciseToAdd = exercises.stream().filter(e -> e.getName().equals(exerciseName)).findFirst().orElse(null);
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

    private void panelCategory() {
        int option;
        do {
            System.out.println("\n--- Panel de Categorías ---");
            System.out.println("1. Crear categoría");
            System.out.println("2. Ver categorías");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createCategory();
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
    }

    private void createCategory() {
        try {
            System.out.print("Ingrese el nombre: ");
            String name = scanner.nextLine();
            System.out.print("Ingrese la descripción: ");
            String description = scanner.nextLine();

            Category category = new Category(name, description);
            categories.add(category);
            System.out.println("Categoría creada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error creando categoría: " + e.getMessage());
        }
    }

    private void panelNote() {
        int option;
        do {
            System.out.println("\n--- Panel de Notas ---");
            System.out.println("1. Crear nota");
            System.out.println("2. Ver notas");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createNote();
                    break;
                case 2:
                    if (notes.isEmpty()) {
                        System.out.println("No hay notas registradas.");
                    } else {
                        System.out.println("\n--- Notas registradas ---");
                        for (Note note : notes) {
                            System.out.println(note);
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

    private void createNote() {
        try {
            System.out.print("Ingrese el título: ");
            String title = scanner.nextLine();
            System.out.print("Ingrese el contenido: ");
            String content = scanner.nextLine();
            System.out.print("Ingrese el propósito: ");
            String purpose = scanner.nextLine();

            Note note = new Note(title, content, purpose, new Date(), userAuth);
            notes.add(note);
            System.out.println("Nota creada exitosamente.");

        } catch (Exception e) {
            System.out.println("Error creando nota: " + e.getMessage());
        }
    }

    private void panelProgress() {
        int option;
        do {
            System.out.println("\n--- Panel de Progreso ---");
            System.out.println("1. Crear progreso");
            System.out.println("2. Ver progresos");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createProgress();
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

    private void createProgress() {
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
