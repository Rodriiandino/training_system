package training.system.core.domain.gym;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IGym {
    void addClientToGym(String gymName, String clientEmail) throws Exception;

    void removeClientFromGym(String gymName, String clientEmail) throws Exception;

    void addTrainerToGym(String gymName, String clientEmail) throws Exception;

    void removeTrainerFromGym(String gymName, String clientEmail) throws Exception;

    void addManagerToGym(String gymName, String clientEmail) throws Exception;

    void removeManagerFromGym(String gymName, String clientEmail) throws Exception;

    Set<User> listGymClients(String gymName) throws Exception;

    Set<User> listGymTrainers(String gymName) throws Exception;

    Set<User> listGymManagers(String gymName) throws Exception;
}
