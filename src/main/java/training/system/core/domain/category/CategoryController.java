package training.system.core.domain.category;

import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class CategoryController implements GenericController<Category, Long> {
    private final CategoryDAO categoryDAO;

    public CategoryController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.categoryDAO = new CategoryDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de categoría", e);
        }
    }

    @Override
    public Category create(Category entity) throws ControllerException {
        try {
            return categoryDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear la categoría", e);
        }
    }

    @Override
    public Category update(Category entity) throws ControllerException {
        try {
            return categoryDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar la categoría", e);
        }
    }

    @Override
    public Set<Category> list() throws ControllerException {
        try {
            return categoryDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar las categorías", e);
        }
    }

    @Override
    public Category search(Long aLong) throws ControllerException {
        try {
            return categoryDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar la categoría", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return categoryDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar la categoría", e);
        }
    }
}
