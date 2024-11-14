package training.system.core.domain.user;

public interface IUser {
    User authenticate(String email, String password) throws Exception;
    void becomeTrainer(Long id) throws Exception;
    void becomeAdministrator(Long id) throws Exception;
}
