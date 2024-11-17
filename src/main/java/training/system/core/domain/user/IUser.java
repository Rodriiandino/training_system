package training.system.core.domain.user;

import java.util.Set;

public interface IUser {
    User authenticate(String email, String password) throws Exception;
    void becomeTrainer(Long id) throws Exception;
    void becomeAdministrator(Long id) throws Exception;
    boolean isEmailAlreadyRegistered(String email) throws Exception;
    Set<User> listClients(User trainer) throws Exception;
}
