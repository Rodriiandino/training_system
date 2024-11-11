package training.system.core.domain.user;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO implements GenericDao<User, Long>, IUser {

    Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User entity) throws DAOException {
        String sql = "INSERT INTO training_system.person (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        String sqlUser = "INSERT INTO training_system.user (id) VALUES (?)";
        String sqlUserRole = "INSERT INTO training_system.user_roles (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser)) {
                stmtUser.setLong(1, entity.getId());
                stmtUser.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("error al crear el usuario", e);
            }

            try (PreparedStatement stmtUserRole = connection.prepareStatement(sqlUserRole)) {
                stmtUserRole.setLong(1, entity.getId());
                stmtUserRole.setLong(2, 1L);
                stmtUserRole.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("error al crear el rol del usuario", e);
            }

            connection.commit();

            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al crear el usuario", e);
        }
    }

    @Override
    public User update(User entity) throws DAOException {
        String sql = "UPDATE training_system.person SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getPassword());
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();

            connection.commit();

            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al actualizar el usuario", e);
        }
    }

    @Override
    public Set<User> list() throws DAOException {
        String sql = "SELECT * FROM training_system.person";
        Set<User> users = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new DAOException("error al listar los usuarios", e);
        }

        return users;
    }

    @Override
    public User search(Long aLong) throws DAOException {
        String sql = "SELECT * FROM training_system.person WHERE id = ?";
        User user = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("error al buscar el usuario", e);
        }

        return user;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String sql = "DELETE FROM training_system.person WHERE id = ?";

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
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al eliminar el usuario", e);
        }
    }


    @Override
    public boolean authenticate(String email, String password) throws DAOException {
        return false;
    }

    @Override
    public void becomeTrainer() throws DAOException {

    }

    @Override
    public void becomeAdministrator() throws DAOException {

    }
}
