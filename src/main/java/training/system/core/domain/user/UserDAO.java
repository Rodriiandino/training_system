package training.system.core.domain.user;

import training.system.core.domain.gym.Gym;
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
        String sql = "UPDATE training_system.person SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getLastName());
            stmt.setString(3, entity.getEmail());
            stmt.setLong(4, entity.getId());
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
        String sql = """
            SELECT 
                p.id, p.first_name, p.last_name, p.email,
                r.id as role_id, r.role_name,
                g_ad.id as admin_gym_id, g_ad.name as admin_gym_name, g_ad.address as admin_gym_address,
                g_tr.id as train_gym_id, g_tr.name as train_gym_name, g_tr.address as train_gym_address,
                gw.id as worker_gym_id, gw.name as worker_gym_name, gw.address as worker_gym_address
            FROM training_system.person p
            LEFT JOIN training_system.user_roles ur ON p.id = ur.user_id
            LEFT JOIN training_system.role r ON ur.role_id = r.id
            LEFT JOIN training_system.user u ON p.id = u.id
            LEFT JOIN training_system.gym g_ad ON u.gym_admin_id = g_ad.id
            LEFT JOIN training_system.gym g_tr ON u.gym_train_id = g_tr.id
            LEFT JOIN training_system.gym_worker_user gwu ON u.id = gwu.user_id
            LEFT JOIN training_system.gym gw ON gwu.gym_id = gw.id
            WHERE p.id = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);

            try (ResultSet rs = stmt.executeQuery()) {
                User user = null;
                Set<Role> roles = new HashSet<>();
                Set<Gym> workerGyms = new HashSet<>();
                Gym adminGym = null;
                Gym trainingGym = null;

                while (rs.next()) {
                    if (user == null) {
                        user = new User(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email")
                        );
                    }

                    long roleId = rs.getLong("role_id");
                    if (roleId != 0) {
                        roles.add(new Role(roleId, RoleEnum.valueOf(rs.getString("role_name"))));
                    }

                    if (adminGym == null) {
                        long adminGymId = rs.getLong("admin_gym_id");
                        if (adminGymId != 0) {
                            adminGym = new Gym(
                                    adminGymId,
                                    rs.getString("admin_gym_name"),
                                    rs.getString("admin_gym_address")
                            );
                            user.setGymManager(adminGym);
                        }
                    }

                    if (trainingGym == null) {
                        long trainGymId = rs.getLong("train_gym_id");
                        if (trainGymId != 0) {
                            trainingGym = new Gym(
                                    trainGymId,
                                    rs.getString("train_gym_name"),
                                    rs.getString("train_gym_address")
                            );
                            user.setGymTraining(trainingGym);
                        }
                    }

                    long workerGymId = rs.getLong("worker_gym_id");
                    if (workerGymId != 0 && workerGyms.stream().noneMatch(gym -> gym.getId() == workerGymId)) {
                        Gym workerGym = new Gym(
                                workerGymId,
                                rs.getString("worker_gym_name"),
                                rs.getString("worker_gym_address")
                        );
                        workerGyms.add(workerGym);
                    }
                }

                if (user != null) {
                    roles.forEach(user::addRole);
                    workerGyms.forEach(user::addGymTrainer);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("error al buscar el usuario", e);
        }

        return null;
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
        String sql = """
            SELECT 
                p.id, p.first_name, p.last_name, p.email,
                r.id as role_id, r.role_name,
                g_ad.id as admin_gym_id, g_ad.name as admin_gym_name, g_ad.address as admin_gym_address,
                g_tr.id as train_gym_id, g_tr.name as train_gym_name, g_tr.address as train_gym_address,
                gw.id as worker_gym_id, gw.name as worker_gym_name, gw.address as worker_gym_address
            FROM training_system.person p
            LEFT JOIN training_system.user_roles ur ON p.id = ur.user_id
            LEFT JOIN training_system.role r ON ur.role_id = r.id
            LEFT JOIN training_system.user u ON p.id = u.id
            LEFT JOIN training_system.gym g_ad ON u.gym_admin_id = g_ad.id
            LEFT JOIN training_system.gym g_tr ON u.gym_train_id = g_tr.id
            LEFT JOIN training_system.gym_worker_user gwu ON u.id = gwu.user_id
            LEFT JOIN training_system.gym gw ON gwu.gym_id = gw.id
            WHERE p.email = ? AND p.password = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                User user = null;
                Set<Role> roles = new HashSet<>();
                Set<Gym> workerGyms = new HashSet<>();
                Gym adminGym = null;
                Gym trainingGym = null;

                while (rs.next()) {
                    if (user == null) {
                        user = new User(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email")
                        );
                    }

                    long roleId = rs.getLong("role_id");
                    if (roleId != 0) {
                        roles.add(new Role(roleId, RoleEnum.valueOf(rs.getString("role_name"))));
                    }

                    if (adminGym == null) {
                        long adminGymId = rs.getLong("admin_gym_id");
                        if (adminGymId != 0) {
                            adminGym = new Gym(
                                    adminGymId,
                                    rs.getString("admin_gym_name"),
                                    rs.getString("admin_gym_address")
                            );
                            user.setGymManager(adminGym);
                        }
                    }

                    if (trainingGym == null) {
                        long trainGymId = rs.getLong("train_gym_id");
                        if (trainGymId != 0) {
                            trainingGym = new Gym(
                                    trainGymId,
                                    rs.getString("train_gym_name"),
                                    rs.getString("train_gym_address")
                            );
                            user.setGymTraining(trainingGym);
                        }
                    }

                    long workerGymId = rs.getLong("worker_gym_id");
                    if (workerGymId != 0 && workerGyms.stream().noneMatch(gym -> gym.getId() == workerGymId)) {
                        Gym workerGym = new Gym(
                                workerGymId,
                                rs.getString("worker_gym_name"),
                                rs.getString("worker_gym_address")
                        );
                        workerGyms.add(workerGym);
                    }
                }

                if (user != null) {
                    roles.forEach(user::addRole);
                    workerGyms.forEach(user::addGymTrainer);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al autenticar el usuario", e);
        }

        return null;
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

    @Override
    public Set<User> listClients(User trainer) throws DAOException {
        String sql = """ 
                SELECT c.id AS client_id, c.first_name AS client_first_name, c.last_name AS client_last_name, c.email AS client_email
                FROM training_system.user u
                    JOIN training_system.person c ON u.id = c.id
                    JOIN training_system.trainer_Client tc ON u.id = tc.client_id
                    JOIN training_system.person t ON tc.trainer_id = t.id
                WHERE tc.trainer_id = ?""";

        Set<User> clients = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, trainer.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User client = new User(
                            rs.getLong("client_id"),
                            rs.getString("client_first_name"),
                            rs.getString("client_last_name"),
                            rs.getString("client_email")
                    );

                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("error al listar los clientes", e);
        }

        return clients;
    }
}
