package training.system.core.domain.gym;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class GymDAO implements GenericDao<Gym, Long> {

    Connection connection;

    public GymDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Gym create(Gym entity) throws DAOException {
        return null;
    }

    @Override
    public Gym update(Gym entity) throws DAOException {
        return null;
    }

    @Override
    public Set<Gym> list() throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }
}
