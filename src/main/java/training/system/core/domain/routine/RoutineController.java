package training.system.core.domain.routine;

import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class RoutineController implements GenericController<Routine, Long> {

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
    public boolean delete(Long id) throws ControllerException {
        try {
            return routineDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar la rutina", e);
        }
    }
}
