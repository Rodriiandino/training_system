package training.system.core.domain.gym;

import training.system.core.domain.user.RoleEnum;
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
    public boolean addClientToGym(Long gymId, String clientEmail) throws DAOException {
        String checkUserSql = """
                SELECT u.gym_train_id
                FROM training_system.person p
                LEFT JOIN training_system.user u ON p.id = u.id
                WHERE p.email = ?""";

        String updateUserSql = "UPDATE training_system.user SET gym_train_id = ? WHERE id = (SELECT id FROM training_system.person WHERE email = ?)";

        try {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, clientEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                    if (rs.getLong("gym_train_id") != 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement updateStmt = connection.prepareStatement(updateUserSql)) {
                updateStmt.setLong(1, gymId);
                updateStmt.setString(2, clientEmail);
                return updateStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo cliente al gimnasio", e);
        }
    }

    @Override
    public boolean removeClientFromGym(Long gymId, String clientEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_train_id = NULL WHERE id = (SELECT id FROM training_system.person WHERE email = ?) AND gym_train_id = ?";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, clientEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo cliente del gimnasio", e);
        }
        return true;
    }

    @Override
    public boolean addTrainerToGym(Long gymId, String trainerEmail) throws DAOException {
        String checkUserSql = """
                SELECT p.id, r.role_name
                FROM training_system.person p
                LEFT JOIN training_system.user_roles ur ON p.id = ur.user_id
                LEFT JOIN training_system.role r ON ur.role_id = r.id
                WHERE p.email = ?""";

        String insertTrainerSql = "INSERT INTO training_system.gym_worker_user (gym_id, user_id) VALUES (?, ?)";

        try {
            Long userId = null;
            boolean isTrainer = false;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, trainerEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    while (rs.next()) {
                        userId = rs.getLong("id");
                        if (RoleEnum.ROLE_TRAINER.name().equals(rs.getString("role_name"))) {
                            isTrainer = true;
                        }
                    }
                }
            }

            if (userId == null || !isTrainer) {
                return false;
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertTrainerSql)) {
                insertStmt.setLong(1, gymId);
                insertStmt.setLong(2, userId);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo entrenador al gimnasio", e);
        }
    }

    @Override
    public boolean removeTrainerFromGym(Long gymId, String trainerEmail) throws DAOException {
        String checkUserSql = "SELECT id FROM training_system.person WHERE email = ?";
        String deleteTrainerSql = "DELETE FROM training_system.gym_worker_user WHERE gym_id = ? AND user_id = ?";

        try {
            Long userId = null;
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, trainerEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getLong("id");
                    } else {
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteTrainerSql)) {
                deleteStmt.setLong(1, gymId);
                deleteStmt.setLong(2, userId);
                int affectedRows = deleteStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo entrenador del gimnasio", e);
        }
    }

    @Override
    public boolean addManagerToGym(Long gymId, String managerEmail) throws DAOException {
        String checkUserSql = """
                SELECT p.id, u.gym_admin_id, r.role_name
                FROM training_system.person p
                LEFT JOIN training_system.user u ON p.id = u.id
                LEFT JOIN training_system.user_roles ur ON p.id = ur.user_id
                LEFT JOIN training_system.role r ON ur.role_id = r.id
                WHERE p.email = ?""";

        String updateUserSql = "UPDATE training_system.user SET gym_admin_id = ? WHERE id = ?";

        try {
            Long userId = null;
            boolean isManager = false;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, managerEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    while (rs.next()) {
                        userId = rs.getLong("id");
                        if (rs.getLong("gym_admin_id") != 0) {
                            return false;
                        }
                        if (RoleEnum.ROLE_ADMINISTRATOR.name().equals(rs.getString("role_name"))) {
                            isManager = true;
                        }
                    }
                }
            }

            if (userId == null || !isManager) {
                return false;
            }

            try (PreparedStatement updateStmt = connection.prepareStatement(updateUserSql)) {
                updateStmt.setLong(1, gymId);
                updateStmt.setLong(2, userId);
                return updateStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error añadiendo gerente al gimnasio", e);
        }
    }

    @Override
    public boolean removeManagerFromGym(Long gymId, String managerEmail) throws DAOException {
        String updateUserSql = "UPDATE training_system.user SET gym_admin_id = NULL WHERE id = (SELECT id FROM training_system.person WHERE email = ?) AND gym_admin_id = ?";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(updateUserSql)) {
                stmt.setString(1, managerEmail);
                stmt.setLong(2, gymId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error removiendo gerente del gimnasio", e);
        }
        return true;
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

    @Override
    public boolean attachTrainerToUser(Long gymId, String trainerEmail, String userEmail) throws DAOException {

        String checkGymClientSql = """
                SELECT u.id AS client_id, u.gym_train_id AS client_gym_id
                FROM training_system.person p
                LEFT JOIN training_system.user u ON p.id = u.id
                WHERE p.email = ?""";

        String checkTrainerSql = """
                SELECT p.id AS trainer_id, g.gym_id AS trainer_gym_id
                FROM training_system.person p
                LEFT JOIN training_system.gym_worker_user g ON p.id = g.user_id
                WHERE p.email = ?""";

        String insertSql = "INSERT INTO training_system.trainer_client (trainer_id, client_id) VALUES (?, ?)";

        try {
            Long clientId = null;
            Long clientGymId = null;
            Long trainerId = null;
            Set<Long> trainerGymIds = new HashSet<>();

            try (PreparedStatement checkStmt = connection.prepareStatement(checkGymClientSql)) {
                checkStmt.setString(1, userEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                    clientId = rs.getLong("client_id");
                    clientGymId = rs.getLong("client_gym_id");
                }
            }

            try (PreparedStatement checkStmt = connection.prepareStatement(checkTrainerSql)) {
                checkStmt.setString(1, trainerEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    while (rs.next()) {
                        trainerId = rs.getLong("trainer_id");
                        trainerGymIds.add(rs.getLong("trainer_gym_id"));
                    }
                }
            }

            if (clientId == null || clientGymId == null || trainerId == null || !trainerGymIds.contains(gymId) || !trainerGymIds.contains(clientGymId)) {
                return false;
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setLong(1, trainerId);
                insertStmt.setLong(2, clientId);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al adjuntar entrenador al usuario", e);
        }
    }
}
