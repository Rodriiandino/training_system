package training.system.console;

import training.system.console.tools.InputUtils;
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
    private final Scanner scanner = new Scanner(System.in);
    private User userAuth = null;
    private final Set<User> users = new HashSet<>();
    private final Set<Gym> gyms = new HashSet<>();
    private final Set<Exercise> exercises = new HashSet<>();
    private final Set<Routine> routines = new HashSet<>();
    private final Set<Category> categories = new HashSet<>();
    private final Set<Note> notes = new HashSet<>();
    private final Set<Progress> progress = new HashSet<>();

    private final SessionService sessionService = new SessionService(scanner);
    private final UserPanel userPanel = new UserPanel(scanner);
    private final GymPanel gymPanel = new GymPanel(scanner);
    private final ExercisePanel exercisePanel = new ExercisePanel(scanner);
    private final RoutinePanel routinePanel = new RoutinePanel(scanner);
    private final NotePanel notePanel = new NotePanel(scanner);
    private final ProgressPanel progressPanel = new ProgressPanel(scanner);

    public void start() {
        initializeData();

        int option;
        if (userAuth == null) {
            do {
                System.out.println("\n--- Sistema Integral de Gestión de Entrenamientos y Progreso Físico ---");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrarse");
                System.out.println("3. Salir");
                option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

                switch (option) {
                    case 1:
                        userAuth = sessionService.login(users);
                        if (userAuth != null) {
                            menu();
                        }
                        break;
                    case 2:
                        sessionService.register(users);
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

        Exercise exercise1 = new Exercise("sentadillas",
                "ejercicio de piernas",
                "ejercicio de piernas",
                "URL",
                true,
                Set.of(category1));
        Exercise exercise2 = new Exercise("flexiones",
                "ejercicio de brazos",
                "ejercicio de brazos",
                "URL",
                true,
                Set.of(category2));
        Exercise exercise3 = new Exercise("plancha",
                "ejercicio de abdominales",
                "ejercicio de abdominales",
                "URL",
                true,
                Set.of(category3));

        users.addAll(List.of(user1, user2, user3));
        categories.addAll(List.of(category1, category2, category3));
        exercises.addAll(List.of(exercise1, exercise2, exercise3));
    }

    private void menu() {
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Ingresar al panel de usuario");
            System.out.println("2. Ingresar al panel de gimnasios");
            System.out.println("3. Ingresar al panel de ejercicios");
            System.out.println("4. Ingresar al panel de rutinas");
            System.out.println("5. Ingresar al panel de notas");
            System.out.println("6. Ingresar al panel de progreso");
            System.out.println("7. Salir");
            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    userPanel.panelUser(userAuth);
                    break;
                case 2:
                    gymPanel.panelGym(users, userAuth, gyms);
                    break;
                case 3:
                    exercisePanel.panelExercise(exercises, categories, users, userAuth);
                    break;
                case 4:
                    routinePanel.panelRoutine(routines, exercises, users,userAuth);
                    break;
                case 5:
                    notePanel.panelNote(notes, users, userAuth);
                    break;
                case 6:
                    progressPanel.panelProgress(exercises, progress, users, userAuth);
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 7);
    }
}
