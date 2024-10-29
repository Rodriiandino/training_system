package training.system.core.domain.user;

import training.system.core.generic.GenericController;

import java.util.Set;

public class UserController implements GenericController<User, Long> {
    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public Set<User> list() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
