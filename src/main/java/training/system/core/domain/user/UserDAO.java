package training.system.core.domain.user;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class UserDAO implements GenericDao<User, Long> {

    Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User entity) throws DAOException {
        return null;
    }

    @Override
    public Set<User> list() throws DAOException {
        return null;
    }

    @Override
    public User update(User entity) throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }
}
