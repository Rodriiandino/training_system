package training.system.core.domain.gym;

import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class GymController implements GenericController<Gym, Long> {

    private final GymDAO gymDAO;

    public GymController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.gymDAO = new GymDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de gimnasio", e);
        }
    }

    @Override
    public Gym create(Gym entity) throws ControllerException {
        try {
            return gymDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el gimnasio", e);
        }
    }

    @Override
    public Gym update(Gym entity) throws ControllerException {
        try {
            return gymDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar el gimnasio", e);
        }
    }

    @Override
    public Set<Gym> list() throws ControllerException {
        try {
            return gymDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los gimnasios", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return gymDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el gimnasio", e);
        }
    }
}
