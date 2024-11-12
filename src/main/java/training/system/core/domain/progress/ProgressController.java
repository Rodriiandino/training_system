package training.system.core.domain.progress;

import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class ProgressController implements GenericController<Progress, Long>, IProgress {

    private final ProgressDAO progressDAO;

    public ProgressController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.progressDAO = new ProgressDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de progreso", e);
        }
    }

    @Override
    public Progress create(Progress entity) throws ControllerException {
        try {
            return progressDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el progreso", e);
        }
    }

    @Override
    public Progress update(Progress entity) throws ControllerException {
        try {
            return progressDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar el progreso", e);
        }
    }

    @Override
    public Set<Progress> list() throws ControllerException {
        try {
            return progressDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los progresos", e);
        }
    }

    @Override
    public Progress search(Long aLong) throws ControllerException {
        try {
            return progressDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar el progreso", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return progressDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el progreso", e);
        }
    }

    @Override
    public Progress createProgressForClient(Progress progress) throws ControllerException {
        try {
            return progressDAO.createProgressForClient(progress);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el progreso para el cliente", e);
        }
    }

    @Override
    public Set<Progress> listUserProgress(User user) throws ControllerException {
        try {
            return progressDAO.listUserProgress(user);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los progresos del usuario", e);
        }
    }
}
