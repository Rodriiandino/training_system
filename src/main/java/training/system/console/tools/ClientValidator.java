package training.system.console.tools;

import training.system.core.domain.user.User;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class ClientValidator {
    public static User selectAndValidateClient(Scanner scanner, User userAuth, Set<User> clients) {

        System.out.println("Tus clientes son:");
        for (User client : userAuth.getClients()) {
            System.out.println(client.toString());
        }

        System.out.println("Ingrese el correo del cliente: ");
        String email = scanner.nextLine();

        Optional<User> clientOpt = clients.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();

        if (!clientOpt.isPresent()) {
            System.out.println("No se encontró el cliente.");
            return null;
        }

        User client = clientOpt.get();

        if (!userAuth.getGymTrainer().contains(client.getGymTraining())) {
            System.out.println("No pertenece al mismo gimnasio que el cliente.");
            return null;
        }

        if (!client.getTrainers().contains(userAuth) || !userAuth.getClients().contains(client)) {
            System.out.println("No tiene este cliente asignado.");
            return null;
        }

        return client;
    }
}
