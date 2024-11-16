package training.system.core.domain.progress;

import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ProgressDAO implements GenericDao<Progress, Long>, IProgress {

    Connection connection;

    public ProgressDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Progress create(Progress entity) throws DAOException {
        String sql = """
                INSERT INTO training_system.progress (progress_date, repetitions, weight, time, user_id, exercise_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setDate(1, new java.sql.Date(entity.getProgressDate().getTime()));
            stmt.setInt(2, entity.getRepetitions());
            stmt.setInt(3, entity.getWeight());
            stmt.setInt(4, entity.getTime());
            stmt.setLong(5, entity.getUser().getId());
            stmt.setLong(6, entity.getExercise().getId());
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
            throw new DAOException("Error creando el progreso", e);
        }
    }

    @Override
    public Progress update(Progress entity) throws DAOException {
        String sql = """
                UPDATE training_system.progress
                SET progress_date = ?, repetitions = ?, weight = ?, time = ?, exercise_id = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setDate(1, new java.sql.Date(entity.getProgressDate().getTime()));
            stmt.setInt(2, entity.getRepetitions());
            stmt.setInt(3, entity.getWeight());
            stmt.setInt(4, entity.getTime());
            stmt.setLong(5, entity.getExercise().getId());
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error actualizando el progreso", e);
        }
    }

    @Override
    public Set<Progress> list() throws DAOException {
        String sql = """
                SELECT p.id, p.progress_date, p.repetitions, p.weight, p.time, per.id, per.first_name, per.email, e.id, e.name, e.description
                FROM training_system.progress p
                LEFT JOIN training_system.user u ON p.user_id = u.id
                LEFT JOIN training_system.person per ON u.id = per.id
                LEFT JOIN training_system.exercise e ON p.exercise_id = e.id
                """;

        Set<Progress> progressSet = new HashSet<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("first_name"));
                user.setEmail(rs.getString("email"));

                Exercise exercise = new Exercise(rs.getLong("id"), rs.getString("name"), rs.getString("description"));

                Progress progress = new Progress(rs.getLong("id"), rs.getDate("progress_date"), rs.getInt("repetitions"), rs.getInt("weight"), rs.getInt("time"), user, exercise);

                progressSet.add(progress);
            }
        } catch (SQLException e) {
            throw new DAOException("Error listando los progresos", e);
        }

        return progressSet;
    }

    @Override
    public Progress search(Long aLong) throws DAOException {
        String sql = """
                SELECT p.id, p.progress_date, p.repetitions, p.weight, p.time, per.id, per.first_name, per.email, per2.id, per2.first_name, per2.email, e.id, e.name, e.description
                FROM training_system.progress p
                LEFT JOIN training_system.user u ON p.user_id = u.id
                LEFT JOIN training_system.person per ON u.id = per.id
                LEFT JOIN training_system.user u2 ON p.trainer_id = u2.id
                LEFT JOIN training_system.person per2 ON u2.id = per2.id
                LEFT JOIN training_system.exercise e ON p.exercise_id = e.id
                WHERE p.id = ?
                """;

        Progress progress = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("first_name"));
                user.setEmail(rs.getString("email"));

                Exercise exercise = new Exercise(rs.getLong("id"), rs.getString("name"), rs.getString("description"));

                if (rs.getLong("per2.id") != 0) {
                    User trainer = new User();
                    trainer.setId(rs.getLong("per2.id"));
                    trainer.setName(rs.getString("per2.first_name"));
                    trainer.setEmail(rs.getString("per2.email"));

                    progress = new Progress(rs.getLong("id"), rs.getDate("progress_date"), rs.getInt("repetitions"), rs.getInt("weight"), rs.getInt("time"), user, trainer, exercise);
                } else {
                    progress = new Progress(rs.getLong("id"), rs.getDate("progress_date"), rs.getInt("repetitions"), rs.getInt("weight"), rs.getInt("time"), user, exercise);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error buscando el progreso", e);
        }

        return progress;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String sql = """
                DELETE FROM training_system.progress
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error eliminando el progreso", e);
        }
    }

    @Override
    public Progress createProgressForClient(Progress progress) throws DAOException {
        String sql = """
                INSERT INTO training_system.progress (progress_date, repetitions, weight, time, user_id, trainer_id, exercise_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setDate(1, new java.sql.Date(progress.getProgressDate().getTime()));
            stmt.setInt(2, progress.getRepetitions());
            stmt.setInt(3, progress.getWeight());
            stmt.setInt(4, progress.getTime());
            stmt.setLong(5, progress.getUser().getId());
            stmt.setLong(6, progress.getTrainer().getId());
            stmt.setLong(6, progress.getExercise().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    progress.setId(rs.getLong(1));
                }
            }

            connection.commit();
            return progress;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error creando el progreso", e);
        }
    }

    @Override
    public Set<Progress> listUserProgress(User user) throws DAOException {
        String sql = """
                SELECT
                    p.id,
                    p.progress_date,
                    p.repetitions,
                    p.weight,
                    p.time,
                    t.id,
                    t.first_name,
                    t.email,
                    e.id,
                    e.name,
                    e.description
                FROM training_system.progress p
                LEFT JOIN training_system.user u ON p.trainer_id = u.id
                LEFT JOIN training_system.person t ON u.id = t.id
                LEFT JOIN training_system.exercise e ON p.exercise_id = e.id
                WHERE p.user_id = ?
                """;

        Set<Progress> progressSet = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Progress progress;

                if (rs.getLong("t.id") != 0) {
                    User trainer = new User();
                    trainer.setId(rs.getLong("t.id"));
                    trainer.setName(rs.getString("t.first_name"));
                    trainer.setEmail(rs.getString("t.email"));

                    progress = new Progress(rs.getLong("id"), rs.getDate("progress_date"), rs.getInt("repetitions"), rs.getInt("weight"), rs.getInt("time"), user, trainer, new Exercise(rs.getLong("e.id"), rs.getString("e.name"), rs.getString("e.description")));
                } else {
                    progress = new Progress(rs.getLong("id"), rs.getDate("progress_date"), rs.getInt("repetitions"), rs.getInt("weight"), rs.getInt("time"), user, new Exercise(rs.getLong("e.id"), rs.getString("e.name"), rs.getString("e.description")));
                }

                progressSet.add(progress);
            }
        } catch (SQLException e) {
            throw new DAOException("Error listando los progresos", e);
        }

        return progressSet;
    }
}
