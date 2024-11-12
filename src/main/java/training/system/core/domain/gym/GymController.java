package training.system.core.domain.gym;

import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class GymController implements GenericController<Gym, Long>, IGym {

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
    public Gym search(Long aLong) throws ControllerException {
        try {
            return gymDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar el gimnasio", e);
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

    @Override
    public void addClientToGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.addClientToGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el cliente al gimnasio", e);
        }
    }

    @Override
    public void removeClientFromGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.removeClientFromGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el cliente del gimnasio", e);
        }
    }

    @Override
    public void addTrainerToGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.addTrainerToGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el entrenador al gimnasio", e);
        }
    }

    @Override
    public void removeTrainerFromGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.removeTrainerFromGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el entrenador del gimnasio", e);
        }
    }

    @Override
    public void addManagerToGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.addManagerToGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el gerente al gimnasio", e);
        }
    }

    @Override
    public void removeManagerFromGym(String gymName, String clientEmail) throws ControllerException {
        try {
            gymDAO.removeManagerFromGym(gymName, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el gerente del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymClients(String gymName) throws ControllerException {
        try {
            return gymDAO.listGymClients(gymName);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los clientes del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymTrainers(String gymName) throws ControllerException {
        try {
            return gymDAO.listGymTrainers(gymName);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los entrenadores del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymManagers(String gymName) throws ControllerException {
        try {
            return gymDAO.listGymManagers(gymName);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los gerentes del gimnasio", e);
        }
    }
}
