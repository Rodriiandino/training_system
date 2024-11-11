package training.system.core.domain.category;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CategoryDAO implements GenericDao<Category, Long> {

    Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Category create(Category entity) throws DAOException {
        String sql = "INSERT INTO training_system.category (name, description) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }

            connection.commit();

            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error al crear la categoría", e);
        }
    }

    @Override
    public Category update(Category entity) throws DAOException {
        String sql = "UPDATE training_system.category SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setLong(3, entity.getId());
            stmt.executeUpdate();

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error al actualizar la categoría", e);
        }
    }

    @Override
    public Set<Category> list() throws DAOException {
        String sql = "SELECT * FROM training_system.category";
        Set<Category> categories = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar las categorías", e);
        }
        return categories;
    }

    @Override
    public Category search(Long aLong) throws DAOException {
        String sql = "SELECT * FROM training_system.category WHERE id = ?";
        Category category = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category = new Category(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar la categoría", e);
        }
        return category;
    }


    @Override
    public boolean delete(Long id) throws DAOException {
        String sql = "DELETE FROM training_system.category WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setLong(1, id);

            int rowsAffected = stmt.executeUpdate();

            connection.commit();
            return rowsAffected  > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error al eliminar la categoría", e);
        }
    }
}
