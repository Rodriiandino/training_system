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
    public void addClientToGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public void removeClientFromGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public void addTrainerToGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public void removeTrainerFromGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public void addManagerToGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public void removeManagerFromGym(String gymName, String clientEmail) throws DAOException {

    }

    @Override
    public Set<User> listGymClients(String gymName) throws DAOException {
        return Set.of();
    }

    @Override
    public Set<User> listGymTrainers(String gymName) throws DAOException {
        return Set.of();
    }

    @Override
    public Set<User> listGymManagers(String gymName) throws DAOException {
        return Set.of();
    }
}
