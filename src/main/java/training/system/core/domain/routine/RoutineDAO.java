package training.system.core.domain.routine;

import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoutineDAO implements GenericDao<Routine, Long>, IRoutine {

    Connection connection;

    public RoutineDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Routine create(Routine entity) throws DAOException {
        String INSERT_ROUTINE = """
                INSERT INTO training_system.routine (name, description, user_id)
                VALUES (?, ?, ?)
                """;
        String INSERT_ROUTINE_EXERCISE = """
                INSERT INTO training_system.routine_exercise (routine_id, exercise_id)
                VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROUTINE, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getDescription());
                stmt.setLong(3, entity.getUser().getId());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
            }

            for (Exercise exercise : entity.getExercises()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROUTINE_EXERCISE)) {
                    stmt.setLong(1, entity.getId());
                    stmt.setLong(2, exercise.getId());
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            return entity;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback de la rutina", rollbackEx);
            }
            throw new DAOException("Error creando la rutina", e);
        }
    }

    @Override
    public Routine update(Routine entity) throws DAOException {
        String UPDATE_ROUTINE = """
            UPDATE training_system.routine
            SET name = ?, 
                description = ?
            WHERE id = ?
            """;

        String DELETE_ROUTINE_EXERCISES = """
            DELETE FROM training_system.routine_exercise
            WHERE routine_id = ?
            """;

        String INSERT_ROUTINE_EXERCISE = """
            INSERT INTO training_system.routine_exercise (routine_id, exercise_id)
            VALUES (?, ?)
            """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(UPDATE_ROUTINE)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getDescription());
                stmt.setLong(3, entity.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_ROUTINE_EXERCISES)) {
                stmt.setLong(1, entity.getId());
                stmt.executeUpdate();
            }

            if (entity.getExercises() != null && !entity.getExercises().isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROUTINE_EXERCISE)) {
                    for (Exercise exercise : entity.getExercises()) {
                        stmt.setLong(1, entity.getId());
                        stmt.setLong(2, exercise.getId());
                        stmt.executeUpdate();
                    }
                }
            }

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback de la rutina", rollbackEx);
            }
            throw new DAOException("Error actualizando la rutina", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DAOException("Error restaurando el autocommit", e);
            }
        }
    }

    @Override
    public Set<Routine> list() throws DAOException {
        String sql = """
                SELECT r.id, r.name, r.description, per.id, per.first_name, per.email
                FROM training_system.routine r
                LEFT JOIN training_system.user u ON r.user_id = u.id
                LEFT JOIN training_system.person per ON u.id = per.id
                """;

        Set<Routine> routines = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    User user = new User();
                    user.setId(rs.getLong("per.id"));
                    user.setName(rs.getString("per.first_name"));
                    user.setEmail(rs.getString("email"));

                    Routine routine = new Routine(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        user
                    );
                    routines.add(routine);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error listando las rutinas", e);
        }

        return routines;
    }

    @Override
    public Routine search(Long aLong) throws DAOException {
        String sql = """
                SELECT r.id, r.name, r.description, per.id, per.first_name, per.email, per2.id, per2.first_name, per2.email, ex.id, ex.name, ex.description
                FROM training_system.routine r
                LEFT JOIN training_system.user u ON r.user_id = u.id
                LEFT JOIN training_system.person per ON u.id = per.id
                LEFT JOIN training_system.user u2 ON r.trainer_id = u2.id
                LEFT JOIN training_system.person per2 ON u2.id = per2.id
                LEFT JOIN training_system.routine_exercise re ON r.id = re.routine_id
                LEFT JOIN training_system.exercise ex ON re.exercise_id = ex.id
                WHERE r.id = ?
                """;

        Routine routine = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (routine == null) {
                        User user = new User();
                        user.setId(rs.getLong("per.id"));
                        user.setName(rs.getString("per.first_name"));
                        user.setEmail(rs.getString("per.email"));

                        if (rs.getLong("per2.id") != 0) {
                            User trainer = new User();
                            trainer.setId(rs.getLong("per2.id"));
                            trainer.setName(rs.getString("per2.first_name"));
                            trainer.setEmail(rs.getString("per2.email"));
                            routine = new Routine(
                                rs.getLong("r.id"),
                                rs.getString("r.name"),
                                rs.getString("r.description"),
                                user,
                                trainer,
                                new HashSet<>()
                            );
                        } else {
                            routine = new Routine(
                                rs.getLong("r.id"),
                                rs.getString("r.name"),
                                rs.getString("r.description"),
                                user,
                                new HashSet<>()
                            );
                        }

                    }
                    Exercise exercise = new Exercise(
                        rs.getLong("ex.id"),
                        rs.getString("ex.name"),
                        rs.getString("ex.description")
                    );
                    routine.addExercise(exercise);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error buscando la rutina", e);
        }

        return routine;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String DELETE_ROUTINE = """
                DELETE FROM training_system.routine
                WHERE id = ?
                """;
        String DELETE_ROUTINE_EXERCISE = """
                DELETE FROM training_system.routine_exercise
                WHERE routine_id = ?
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_ROUTINE)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_ROUTINE_EXERCISE)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback de la rutina", rollbackEx);
            }
            throw new DAOException("Error eliminando la rutina", e);
        }
    }

    @Override
    public Routine createRoutineForClient(Routine routine) throws DAOException {
        String INSERT_ROUTINE = """
                INSERT INTO training_system.routine (name, description, user_id, trainer_id)
                VALUES (?, ?, ?, ?)
                """;
        String INSERT_ROUTINE_EXERCISE = """
                INSERT INTO training_system.routine_exercise (routine_id, exercise_id)
                VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROUTINE, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, routine.getName());
                stmt.setString(2, routine.getDescription());
                stmt.setLong(3, routine.getUser().getId());
                stmt.setLong(4, routine.getTrainer().getId());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        routine.setId(rs.getLong(1));
                    }
                }
            }

            for (Exercise exercise : routine.getExercises()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROUTINE_EXERCISE)) {
                    stmt.setLong(1, routine.getId());
                    stmt.setLong(2, exercise.getId());
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            return routine;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback de la rutina", rollbackEx);
            }
            throw new DAOException("Error creando la rutina", e);
        }
    }

    @Override
    public Set<Routine> listUserRoutines(User user) throws DAOException {
        String sql = """
                SELECT r.id, r.name, r.description, t.id, t.first_name, t.email, e.id, e.name, e.description
                FROM training_system.routine r
                LEFT JOIN training_system.user u ON r.trainer_id = u.id
                LEFT JOIN training_system.person t ON u.id = t.id
                LEFT JOIN training_system.routine_exercise re ON r.id = re.routine_id
                LEFT JOIN training_system.exercise e ON re.exercise_id = e.id
                WHERE r.user_id = ?
                """;

        Map<Long, Routine> routinesMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Routine routine = routinesMap.get(rs.getLong("r.id"));
                    if (routine == null) {
                        if (rs.getLong("t.id") != 0) {
                            User trainer = new User();
                            trainer.setId(rs.getLong("t.id"));
                            trainer.setName(rs.getString("t.first_name"));
                            trainer.setEmail(rs.getString("t.email"));
                            routine = new Routine(
                                rs.getLong("r.id"),
                                rs.getString("r.name"),
                                rs.getString("r.description"),
                                user,
                                trainer,
                                new HashSet<>()
                            );
                        } else {
                            routine = new Routine(
                                rs.getLong("r.id"),
                                rs.getString("r.name"),
                                rs.getString("r.description"),
                                user,
                                new HashSet<>()
                            );
                        }
                        routinesMap.put(routine.getId(), routine);
                    }

                    Exercise exercise = new Exercise(
                        rs.getLong("e.id"),
                        rs.getString("e.name"),
                        rs.getString("e.description")
                    );
                    routine.addExercise(exercise);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error listando las rutinas del usuario", e);
        }

        return new HashSet<>(routinesMap.values());
    }
}
