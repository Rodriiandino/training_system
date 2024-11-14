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
        String INSERT_PERSON = "INSERT INTO training_system.person (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        String INSERT_USER = "INSERT INTO training_system.user (id) VALUES (?)";
        String INSERT_USER_ROLE = "INSERT INTO training_system.user_roles (user_id, role_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            long personId;

            try (PreparedStatement personStmt = connection.prepareStatement(INSERT_PERSON, Statement.RETURN_GENERATED_KEYS)) {
                personStmt.setString(1, entity.getName());
                personStmt.setString(2, entity.getLastName());
                personStmt.setString(3, entity.getEmail());
                personStmt.setString(4, entity.getPassword());
                personStmt.executeUpdate();

                try (ResultSet personKeys = personStmt.getGeneratedKeys()) {
                    if (personKeys.next()) {
                        personId = personKeys.getLong(1);
                        entity.setId(personId);
                    } else {
                        throw new DAOException("Creating person failed, no generated key obtained.");
                    }
                }
            }

            try (PreparedStatement userStmt = connection.prepareStatement(INSERT_USER)) {
                userStmt.setLong(1, personId);
                userStmt.executeUpdate();
            }

            try (PreparedStatement userRoleStmt = connection.prepareStatement(INSERT_USER_ROLE)) {
                userRoleStmt.setLong(1, personId);
                userRoleStmt.setLong(2, 1L); // Assuming role ID 1 is the default role
                userRoleStmt.executeUpdate();
            }

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error al crear el usuario", e);
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
    public User authenticate(String email, String password) throws DAOException {
        String OBTAIN_USER = "SELECT * FROM training_system.person WHERE email = ? AND password = ?";
        String OBTAIN_ROLES = "SELECT r.id, r.role_name FROM training_system.role r JOIN training_system.user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
        User user = null;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(OBTAIN_USER)) {
                stmt.setString(1, email);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        user = new User(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    }
                }
            }

            if (user != null) {
                try (PreparedStatement stmt = connection.prepareStatement(OBTAIN_ROLES)) {
                    stmt.setLong(1, user.getId());

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            user.addRole(new Role(rs.getLong("id"), RoleEnum.valueOf(rs.getString("role_name"))));
                        }
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DAOException("error al autenticar el usuario", e);
        }

        return user;
    }

    @Override
    public void becomeTrainer(Long id) throws DAOException {
        String sql = "INSERT INTO training_system.user_roles (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, 3L);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("error al asignar el rol de entrenador", e);
        }
    }

    @Override
    public void becomeAdministrator(Long id) throws DAOException {
        String sql = "INSERT INTO training_system.user_roles (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, 2L);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("error al asignar el rol de administrador", e);
        }
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) throws DAOException {
        String sql = "SELECT * FROM training_system.person WHERE email = ?";
        boolean isEmailAlreadyRegistered = false;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                isEmailAlreadyRegistered = rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("error al verificar si el email ya está registrado", e);
        }

        return isEmailAlreadyRegistered;
    }
}
