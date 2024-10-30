package training.system.core.domain.progress;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class ProgressDAO implements GenericDao<Progress, Long> {

    Connection connection;

    public ProgressDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Progress create(Progress entity) throws DAOException {
        return null;
    }

    @Override
    public Progress update(Progress entity) throws DAOException {
        return null;
    }

    @Override
    public Set<Progress> list() throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }
}
