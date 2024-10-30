package training.system.core.domain.user;

import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class UserController implements GenericController<User, Long> {

    private final UserDAO userDAO;

    public UserController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.userDAO = new UserDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de usuario", e);
        }
    }

    @Override
    public User create(User entity) throws ControllerException {
        try {
            return userDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear el usuario", e);
        }
    }

    @Override
    public User update(User entity) throws ControllerException {
        try {
            return userDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public Set<User> list() throws ControllerException {
        try {
            return userDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar los usuarios", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return userDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar el usuario", e);
        }
    }
}
