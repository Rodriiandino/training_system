package training.system.core.domain.exercise;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class ExerciseDAO implements GenericDao<Exercise, Long> {

    Connection connection;

    public ExerciseDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Exercise create(Exercise entity) throws DAOException {
        return null;
    }

    @Override
    public Exercise update(Exercise entity) throws DAOException {
        return null;
    }

    @Override
    public Set<Exercise> list() throws DAOException {
        return null;
    }

    @Override
    public Exercise search(Long aLong) throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }
}
