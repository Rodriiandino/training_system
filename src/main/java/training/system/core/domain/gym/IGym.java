package training.system.core.domain.gym;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IGym {
    void addClientToGym(Long id, String clientEmail) throws Exception;

    void removeClientFromGym(Long id, String clientEmail) throws Exception;

    void addTrainerToGym(Long id, String clientEmail) throws Exception;

    void removeTrainerFromGym(Long id, String clientEmail) throws Exception;

    void addManagerToGym(Long id, String clientEmail) throws Exception;

    void removeManagerFromGym(Long id, String clientEmail) throws Exception;

    Set<User> listGymClients(Long id) throws Exception;

    Set<User> listGymTrainers(Long id) throws Exception;

    Set<User> listGymManagers(Long id) throws Exception;
}
