package training.system.core.domain.user;

public interface IUser {
    boolean authenticate(String email, String password) throws Exception;
    void becomeTrainer() throws Exception;
    void becomeAdministrator() throws Exception;
}
