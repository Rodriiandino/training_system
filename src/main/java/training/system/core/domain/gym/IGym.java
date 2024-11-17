package training.system.core.domain.gym;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IGym {
    boolean addClientToGym(Long id, String clientEmail) throws Exception;

    boolean removeClientFromGym(Long id, String clientEmail) throws Exception;

    boolean addTrainerToGym(Long id, String clientEmail) throws Exception;

    boolean removeTrainerFromGym(Long id, String clientEmail) throws Exception;

    boolean addManagerToGym(Long id, String clientEmail) throws Exception;

    boolean removeManagerFromGym(Long id, String clientEmail) throws Exception;

    Set<User> listGymClients(Long id) throws Exception;

    Set<User> listGymTrainers(Long id) throws Exception;

    Set<User> listGymManagers(Long id) throws Exception;

    boolean attachTrainerToUser(Long gymId, String trainerEmail, String userEmail) throws Exception;

    Set<User> listAttachedTrainersToUser(Long gymId) throws Exception;
}
