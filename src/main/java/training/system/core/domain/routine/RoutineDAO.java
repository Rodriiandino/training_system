package training.system.core.domain.routine;

import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class RoutineDAO implements GenericDao<Routine, Long>, IRoutine {

    Connection connection;

    public RoutineDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Routine create(Routine entity) throws DAOException {
        return null;
    }

    @Override
    public Routine update(Routine entity) throws DAOException {
        return null;
    }

    @Override
    public Set<Routine> list() throws DAOException {
        return null;
    }

    @Override
    public Routine search(Long aLong) throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }

    @Override
    public Routine createRoutineForClient(Routine routine, User client, User trainer) throws DAOException {
        return null;
    }

    @Override
    public Set<Routine> listUserRoutines(User user) throws DAOException {
        return Set.of();
    }
}
