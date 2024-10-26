package training.system.console;

import training.system.console.tools.ClientValidator;
import training.system.console.tools.InputUtils;
import training.system.core.domain.note.Note;
import training.system.core.domain.user.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NotePanel {
    private Scanner scanner;

    public NotePanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void panelNote(Set<Note> notes, Set<User> clients, User userAuth) {
        if (userAuth.isTrainer()) {
            panelNoteTrainer(notes, clients, userAuth);
        } else {
            panelNoteUser(notes, userAuth);
        }
    }

    private void panelNoteUser(Set<Note> notes, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Notas ---");
            System.out.println("1. Crear nota");
            System.out.println("2. Ver notas");
            System.out.println("3. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createNote(notes, userAuth);
                    break;
                case 2:
                    showNotes(notes, userAuth);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 3);
    }

    private void panelNoteTrainer(Set<Note> notes, Set<User> clients, User userAuth) {
        int option;
        do {
            System.out.println("\n--- Panel de Notas ---");
            System.out.println("1. Crear nota");
            System.out.println("2. Ver notas");
            System.out.println("3. Crear nota para usuario");
            System.out.println("4. Salir");

            option = InputUtils.getValidatedInput(scanner, "Ingrese su opción: ");

            switch (option) {
                case 1:
                    createNote(notes, userAuth);
                    break;
                case 2:
                    showNotes(notes, userAuth);
                    break;
                case 3:
                    createNoteForUser(notes, clients, userAuth);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        } while (option != 4);
    }

    private void createNoteForUser(Set<Note> notes, Set<User> clients, User userAuth) {
        try {
            User client = ClientValidator.selectAndValidateClient(scanner, userAuth, clients);

            if (client == null) {
                return;
            }

            System.out.print("Ingrese el título: ");
            String title = scanner.nextLine();
            System.out.print("Ingrese el contenido: ");
            String content = scanner.nextLine();
            System.out.print("Ingrese el propósito: ");
            String purpose = scanner.nextLine();

            Note note = new Note(title, content, purpose, new Date(), client, userAuth);
            notes.add(note);
            System.out.println("Nota creada exitosamente.");

        } catch (Exception e) {
            System.out.println("Error creando nota: " + e.getMessage());
        }
    }

    private void createNote(Set<Note> notes, User userAuth) {
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

    private void showNotes(Set<Note> notes, User userAuth) {
        Set<Note> notesByUser = new HashSet<>();

        for (Note note : notes) {
            if (userAuth.equals(note.getUser())) {
                notesByUser.add(note);
            }
        }

        if (notesByUser.isEmpty()) {
            System.out.println("No hay notas registradas.");
        } else {
            System.out.println("\n--- Notas registradas ---");
            for (Note note : notesByUser) {
                System.out.println(note);
            }
        }
    }
}
