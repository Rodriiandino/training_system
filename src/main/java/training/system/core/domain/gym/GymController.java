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
    public boolean addClientToGym(Long id, String clientEmail) throws ControllerException {
        try {
            return gymDAO.addClientToGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el cliente al gimnasio", e);
        }
    }

    @Override
    public boolean removeClientFromGym(Long id, String clientEmail) throws ControllerException {
        try {
            return gymDAO.removeClientFromGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el cliente del gimnasio", e);
        }
    }

    @Override
    public boolean addTrainerToGym(Long id, String clientEmail) throws ControllerException {
        try {
            return gymDAO.addTrainerToGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el entrenador al gimnasio", e);
        }
    }

    @Override
    public boolean removeTrainerFromGym(Long id, String clientEmail) throws ControllerException {
        try {
            return gymDAO.removeTrainerFromGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el entrenador del gimnasio", e);
        }
    }

    @Override
    public boolean addManagerToGym(Long id, String clientEmail) throws ControllerException {
        try {
            return gymDAO.addManagerToGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al agregar el gerente al gimnasio", e);
        }
    }

    @Override
    public boolean removeManagerFromGym(Long id, String clientEmail) throws ControllerException {
        try {
            return  gymDAO.removeManagerFromGym(id, clientEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el gerente del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymClients(Long id) throws ControllerException {
        try {
            return gymDAO.listGymClients(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los clientes del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymTrainers(Long id) throws ControllerException {
        try {
            return gymDAO.listGymTrainers(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los entrenadores del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymManagers(Long id) throws ControllerException {
        try {
            return gymDAO.listGymManagers(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los gerentes del gimnasio", e);
        }
    }

    @Override
    public boolean attachTrainerToUser(Long gymId, String trainerEmail, String userEmail) throws ControllerException {
        try {
            return gymDAO.attachTrainerToUser(gymId, trainerEmail, userEmail);
        } catch (DAOException e) {
            throw new ControllerException("Error al adjuntar entrenador al usuario", e);
        }
    }

    @Override
    public Set<User> listAttachedTrainersToUser(Long gymId) throws ControllerException {
        try {
            return gymDAO.listAttachedTrainersToUser(gymId);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los entrenadores adjuntos al usuario", e);
        }
    }
}
