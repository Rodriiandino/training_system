package training.system.core.domain.gym;

import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class GymDAO implements GenericDao<Gym, Long>, IGym {

    Connection connection;

    public GymDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Gym create(Gym entity) throws DAOException {
        String INSERT_GYM = "INSERT INTO training_system.gym (name, address) VALUES (?, ?)";
        String UPDATE_MANAGER = "UPDATE training_system.user SET gym_admin_id = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(INSERT_GYM, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getAddress());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
            }

            User manager = entity.getManagers().iterator().next();
            try (PreparedStatement stmt = connection.prepareStatement(UPDATE_MANAGER)) {
                stmt.setLong(1, entity.getId());
                stmt.setLong(2, manager.getId());
                stmt.executeUpdate();
            }

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error creando el gimnasio", e);
        }
    }

    @Override
    public Gym update(Gym entity) throws DAOException {
        String sql = "UPDATE training_system.gym SET name = ?, address = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getAddress());
            stmt.setLong(3, entity.getId());
            stmt.executeUpdate();

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al actualizar el gimnasio", e);
        }
    }

    @Override
    public Set<Gym> list() throws DAOException {
        String sql = "SELECT * FROM training_system.gym";

        Set<Gym> gyms = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Gym gym = new Gym(rs.getLong("id"), rs.getString("name"), rs.getString("address"));
                    gyms.add(gym);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar los gimnasios con sus usuarios", e);
        }

        return gyms;
    }

    @Override
    public Gym search(Long gymId) throws DAOException {
        String sql = "SELECT * FROM training_system.gym WHERE id = ?";

        Gym gym = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, gymId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    gym = new Gym(rs.getLong("id"), rs.getString("name"), rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el gimnasio con sus usuarios", e);
        }

        return gym;
    }


    @Override
    public boolean delete(Long id) throws DAOException {
        String sql = "DELETE FROM training_system.gym WHERE id = ?";

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
            throw new DAOException("error al eliminar el gimnasio", e);
        }
    }

    @Override
    public void addClientToGym(Long gymId, String clientEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_train_id = ? WHERE id = (SELECT id FROM training_system.person WHERE email = ?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setLong(1, gymId);
                stmt.setString(2, clientEmail);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Añaadir cliente al gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo cliente al gimnasio", e);
        }
    }

    @Override
    public void removeClientFromGym(Long gymId, String clientEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_train_id = NULL WHERE id = (SELECT id FROM training_system.person WHERE email = ?) AND gym_train_id = ?";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, clientEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Remover cliente del gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo cliente del gimnasio", e);
        }
    }

    @Override
    public void addTrainerToGym(Long gymId, String trainerEmail) throws DAOException {
        String updateUserSql = "INSERT INTO training_system.gym_worker_user (gym_id, user_id) VALUES (?, (SELECT id FROM training_system.person WHERE email = ?))";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, trainerEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Añadir entrenador al gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo entrenador al gimnasio", e);
        }
    }

    @Override
    public void removeTrainerFromGym(Long gymId, String trainerEmail) throws DAOException {
        String updateUserSql = "DELETE FROM training_system.gym_worker_user WHERE user_id = (SELECT id FROM training_system.person WHERE email = ?) AND gym_id = ?";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, trainerEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Remover entrenador del gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo entrenador del gimnasio", e);
        }
    }

    @Override
    public void addManagerToGym(Long gymId, String managerEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_admin_id = ? WHERE id = (SELECT id FROM training_system.person WHERE email = ?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setLong(1, gymId);
                stmt.setString(2, managerEmail);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Añadir gerente al gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo gerente al gimnasio", e);
        }
    }

    @Override
    public void removeManagerFromGym(Long gymId, String managerEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_admin_id = NULL WHERE id = (SELECT id FROM training_system.person WHERE email = ?) AND gym_admin_id = ?";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, managerEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Remover gerente del gimnasio falló, no se afectaron filas.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo gerente del gimnasio", e);
        }
    }

    @Override
    public Set<User> listGymClients(Long id) throws DAOException {
        String sql = "SELECT p.id, first_name, last_name, email FROM training_system.user u LEFT JOIN training_system.person p ON u.id = p.id WHERE u.gym_train_id = ?";

        Set<User> clients = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User client = new User(rs.getLong("p.id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar los clientes del gimnasio", e);
        }

        return clients;
    }

    @Override
    public Set<User> listGymTrainers(Long id) throws DAOException {
        String sql = "SELECT p.id, first_name, last_name, email FROM training_system.gym_worker_user g LEFT JOIN training_system.person p ON g.user_id = p.id WHERE g.gym_id = ?";

        Set<User> trainers = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User trainer = new User(rs.getLong("p.id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    trainers.add(trainer);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar los entrenadores del gimnasio", e);
        }

        return trainers;
    }

    @Override
    public Set<User> listGymManagers(Long id) throws DAOException {
        String sql = "SELECT p.id, first_name, last_name, email FROM training_system.user u LEFT JOIN training_system.person p ON u.id = p.id WHERE u.gym_admin_id = ?";

        Set<User> managers = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User manager = new User(rs.getLong("p.id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                    managers.add(manager);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar los gerentes del gimnasio", e);
        }

        return managers;
    }
}
