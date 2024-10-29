package training.system.core.domain.category;

import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class CategoryController implements GenericController<Category, Long> {
    private final CategoryDAO categoryDAO;

    public CategoryController() {
        var factory = new ConnectionFactory();
        this.categoryDAO = new CategoryDAO(factory.getConnection());
    }

    @Override
    public Category create(Category entity) {
        try {
            return categoryDAO.create(entity);
        } catch (Exception e) {
            System.err.println("Error al crear la categoría");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Category update(Category entity) {
        try {
            return categoryDAO.update(entity);
        } catch (Exception e) {
            System.err.println("Error al actualizar la categoría");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<Category> list() {
        try {
            return categoryDAO.list();
        } catch (Exception e) {
            System.err.println("Error al listar las categorías");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            return categoryDAO.delete(id);
        } catch (Exception e) {
            System.err.println("Error al eliminar la categoría");
            e.printStackTrace();
            return false;
        }
    }
}
