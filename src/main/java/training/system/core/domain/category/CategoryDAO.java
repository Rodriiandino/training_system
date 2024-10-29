package training.system.core.domain.category;

import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.Set;

public class CategoryDAO implements GenericDao<Category, Long> {

    Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Category create(Category entity) {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
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
                throw new RuntimeException("Error al hacer rollback", rollbackEx);
            }
            throw new RuntimeException("Error al crear la categoría", e);
        }
    }

    @Override
    public Category update(Category entity) {
        return null;
    }

    @Override
    public Set<Category> list() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
