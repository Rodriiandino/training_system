package training.system.core.domain.exercise;

import training.system.core.domain.category.Category;
import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExerciseDAO implements GenericDao<Exercise, Long>, IExercise {

    Connection connection;

    public ExerciseDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Exercise create(Exercise entity) throws DAOException {
        String INSERT_EXERCISE = """
                    INSERT INTO training_system.exercise (name, description, explanation, demo_video_url, is_predefined, user_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;
        String INSERT_EXERCISE_CATEGORY = """
                    INSERT INTO training_system.exercise_Category (exercise_id, category_id)
                    VALUES (?, ?)
                """;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(INSERT_EXERCISE, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getDescription());
                stmt.setString(3, entity.getExplanation());
                stmt.setString(4, entity.getVideoUrl());
                stmt.setBoolean(5, entity.getUser() == null);
                stmt.setLong(6, entity.getUser() != null ? entity.getUser().getId() : 0);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
            }

            for (Category category : entity.getCategories()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_EXERCISE_CATEGORY)) {
                    stmt.setLong(1, entity.getId());
                    stmt.setLong(2, category.getId());
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error creando el ejercicio", e);
        }
    }

    @Override
    public Exercise update(Exercise entity) throws DAOException {
        String UPDATE_EXERCISE = """
                UPDATE training_system.exercise
                SET name = ?,
                    description = ?,
                    explanation = ?,
                    demo_video_url = ?
                WHERE id = ?
                """;

        String DELETE_EXERCISE_CATEGORIES = """
                DELETE FROM training_system.exercise_category
                WHERE exercise_id = ?
                """;

        String INSERT_EXERCISE_CATEGORY = """
                INSERT INTO training_system.exercise_category (exercise_id, category_id)
                VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(UPDATE_EXERCISE)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getDescription());
                stmt.setString(3, entity.getExplanation());
                stmt.setString(4, entity.getVideoUrl());
                stmt.setLong(5, entity.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_EXERCISE_CATEGORIES)) {
                stmt.setLong(1, entity.getId());
                stmt.executeUpdate();
            }

            if (entity.getCategories() != null && !entity.getCategories().isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_EXERCISE_CATEGORY)) {
                    for (Category category : entity.getCategories()) {
                        stmt.setLong(1, entity.getId());
                        stmt.setLong(2, category.getId());
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
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error actualizando el ejercicio", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DAOException("error restaurando el autocommit", e);
            }
        }
    }

    @Override
    public Set<Exercise> list() throws DAOException {
        String sql = """
                SELECT
                    e.id AS exercise_id,
                    e.name AS exercise_name,
                    e.description AS exercise_description,
                    e.explanation,
                    e.demo_video_url,
                    p.id,
                    p.first_name,
                    p.email,
                    c.id AS category_id,
                    c.name AS category_name,
                    c.description AS category_description
                FROM training_system.exercise e
                LEFT JOIN training_system.user u ON e.user_id = u.id
                LEFT JOIN training_system.person p ON u.id = p.id
                LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
                LEFT JOIN training_system.category c ON ec.category_id = c.id
                """;

        Map<Long, Exercise> exerciseMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long exerciseId = rs.getLong("exercise_id");
                Exercise exercise = exerciseMap.get(exerciseId);

                if (exercise == null) {
                    if (rs.getLong("p.id") != 0) {
                        User user = new User();
                        user.setId(rs.getLong("p.id"));
                        user.setName(rs.getString("p.first_name"));
                        user.setEmail(rs.getString("p.email"));
                        exercise = new Exercise(exerciseId, rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>(), user);
                        exerciseMap.put(exerciseId, exercise);
                    } else {
                        exercise = new Exercise(exerciseId, rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>());
                        exerciseMap.put(exerciseId, exercise);
                    }
                }

                Category category = new Category(rs.getLong("category_id"), rs.getString("category_name"), rs.getString("category_description"));
                exercise.addCategory(category);
            }
        } catch (SQLException e) {
            throw new DAOException("error al listar los ejercicios", e);
        }

        return new HashSet<>(exerciseMap.values());
    }

    @Override
    public Exercise search(Long aLong) throws DAOException {
        String sql = """
                SELECT
                    e.id AS exercise_id,
                    e.name AS exercise_name,
                    e.description AS exercise_description,
                    e.explanation,
                    e.demo_video_url,
                    p.id,
                    p.first_name,
                    p.email,
                    p2.id AS trainer_id,
                    p2.first_name AS trainer_first_name,
                    p2.email AS trainer_email,
                    c.id AS category_id,
                    c.name AS category_name,
                    c.description AS category_description
                FROM training_system.exercise e
                LEFT JOIN training_system.user u ON e.user_id = u.id
                LEFT JOIN training_system.person p ON u.id = p.id
                LEFT JOIN training_system.user u2 ON e.trainer_id = u2.id
                LEFT JOIN training_system.person p2 ON u2.id = p2.id
                LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
                LEFT JOIN training_system.category c ON ec.category_id = c.id
                WHERE e.id = ?
                """;

        Exercise exercise = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (exercise == null) {
                        if (rs.getLong("p.id") != 0) {
                            User user = new User();
                            user.setId(rs.getLong("p.id"));
                            user.setName(rs.getString("p.first_name"));
                            user.setEmail(rs.getString("p.email"));

                            if (rs.getLong("trainer_id") != 0) {
                                User trainer = new User();
                                trainer.setId(rs.getLong("trainer_id"));
                                trainer.setName(rs.getString("trainer_first_name"));
                                trainer.setEmail(rs.getString("trainer_email"));
                                exercise = new Exercise(rs.getLong("exercise_id"), rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>(), user, trainer);
                            } else {
                                exercise = new Exercise(rs.getLong("exercise_id"), rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>(), user);
                            }
                        } else {
                            exercise = new Exercise(rs.getLong("exercise_id"), rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>());
                        }
                    }

                    Category category = new Category(rs.getLong("category_id"), rs.getString("category_name"), rs.getString("category_description"));
                    exercise.addCategory(category);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("error al buscar el ejercicio", e);
        }

        return exercise;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String DELETE_EXERCISE = """
                DELETE FROM training_system.exercise
                WHERE id = ?
                """;
        String DELETE_EXERCISE_CATEGORY = """
                DELETE FROM training_system.exercise_category
                WHERE exercise_id = ?
                """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_EXERCISE)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_EXERCISE_CATEGORY)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al eliminar el ejercicio", e);
        }
    }

    @Override
    public Exercise createExerciseForClient(Exercise exercise) throws DAOException {
        String INSERT_EXERCISE = """
                    INSERT INTO training_system.exercise (name, description, explanation, demo_video_url, is_predefined, user_id, trainer_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        String INSERT_EXERCISE_CATEGORY = """
                    INSERT INTO training_system.exercise_Category (exercise_id, category_id)
                    VALUES (?, ?)
                """;

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(INSERT_EXERCISE, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, exercise.getName());
                stmt.setString(2, exercise.getDescription());
                stmt.setString(3, exercise.getExplanation());
                stmt.setString(4, exercise.getVideoUrl());
                stmt.setBoolean(5, exercise.getUser() == null);
                stmt.setLong(6, exercise.getUser().getId());
                stmt.setLong(7, exercise.getTrainer().getId());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        exercise.setId(rs.getLong(1));
                    }
                }
            }

            for (Category category : exercise.getCategories()) {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_EXERCISE_CATEGORY)) {
                    stmt.setLong(1, exercise.getId());
                    stmt.setLong(2, category.getId());
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            return exercise;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error creando el ejercicio", e);
        }
    }

    @Override
    public Set<Exercise> listUserExercises(User user) throws DAOException {
        String sql = """
                SELECT
                    e.id AS exercise_id,
                    e.name AS exercise_name,
                    e.description AS exercise_description,
                    e.explanation,
                    e.demo_video_url,
                    e.is_predefined,
                    c.id AS category_id,
                    c.name AS category_name,
                    c.description AS category_description,
                    e.trainer_id,
                    pt.first_name AS trainer_first_name,
                    pt.email AS trainer_email
                FROM training_system.exercise e
                    LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
                    LEFT JOIN training_system.category c ON ec.category_id = c.id
                    LEFT JOIN training_system.user t ON e.trainer_id = t.id
                    LEFT JOIN training_system.person pt ON t.id = pt.id
                WHERE e.user_id = ? OR e.is_predefined = TRUE
                """;

        Map<Long, Exercise> exerciseMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long exerciseId = rs.getLong("exercise_id");
                    Exercise exercise = exerciseMap.get(exerciseId);
                    long trainerId = rs.getLong("trainer_id");

                    if (exercise == null) {
                        if (trainerId != 0) {
                            User trainer = new User();
                            trainer.setId(trainerId);
                            trainer.setName(rs.getString("trainer_first_name"));
                            trainer.setEmail(rs.getString("trainer_email"));
                            exercise = new Exercise(exerciseId, rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), rs.getBoolean("is_predefined"), new HashSet<>(), user, trainer);
                        } else {
                            exercise = new Exercise(exerciseId, rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), rs.getBoolean("is_predefined"), new HashSet<>(), user);
                        }

                        exerciseMap.put(exerciseId, exercise);
                    }

                    Category category = new Category(rs.getLong("category_id"), rs.getString("category_name"), rs.getString("category_description"));
                    exercise.addCategory(category);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("error al listar los ejercicios", e);
        }

        return new HashSet<>(exerciseMap.values());
    }
}
