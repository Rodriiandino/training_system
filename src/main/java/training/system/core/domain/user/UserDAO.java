package training.system.core.domain.user;

import training.system.core.generic.GenericDao;

import java.util.Set;

public class UserDAO implements GenericDao<User, Long> {
    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public Set<User> list() {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
