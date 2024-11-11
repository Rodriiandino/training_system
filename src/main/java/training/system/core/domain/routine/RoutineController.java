package training.system.core.domain.routine;

import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class RoutineController implements GenericController<Routine, Long>, IRoutine {

    private final RoutineDAO routineDAO;

    public RoutineController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.routineDAO = new RoutineDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de rutina", e);
        }
    }

    @Override
    public Routine create(Routine entity) throws ControllerException {
        try {
            return routineDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear la rutina", e);
        }
    }

    @Override
    public Routine update(Routine entity) throws ControllerException {
        try {
            return routineDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar la rutina", e);
        }
    }

    @Override
    public Set<Routine> list() throws ControllerException {
        try {
            return routineDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar las rutinas", e);
        }
    }

    @Override
    public Routine search(Long aLong) throws ControllerException {
        try {
            return routineDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar la rutina", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return routineDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar la rutina", e);
        }
    }

    @Override
    public Routine createRoutineForClient(Routine routine, User client, User trainer) throws ControllerException {
        try {
            return routineDAO.createRoutineForClient(routine, client, trainer);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear la rutina para el cliente", e);
        }
    }

    @Override
    public Set<Routine> listUserRoutines(User user) throws ControllerException {
        try {
            return routineDAO.listUserRoutines(user);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar las rutinas del usuario", e);
        }
    }
}
