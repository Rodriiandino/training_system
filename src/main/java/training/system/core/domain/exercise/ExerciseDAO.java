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
        String sql = """
                    UPDATE training_system.exercise
                    SET name = ?, description = ?, explanation = ?, demo_video_url = ? 
                    WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setString(3, entity.getExplanation());
            stmt.setString(4, entity.getVideoUrl());
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
            throw new DAOException("error actualizando el ejercicio", e);
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
                    c.id AS category_id,
                    c.name AS category_name,
                    c.description AS category_description
                FROM training_system.exercise e
                LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
                LEFT JOIN training_system.category c ON ec.category_id = c.id
                """;

        Map<Long, Exercise> exerciseMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long exerciseId = rs.getLong("exercise_id");
                Exercise exercise = exerciseMap.get(exerciseId);

                if (exercise == null) {
                    exercise = new Exercise(exerciseId, rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>());
                    exerciseMap.put(exerciseId, exercise);
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
                    c.id AS category_id,
                    c.name AS category_name,
                    c.description AS category_description
                FROM training_system.exercise e
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
                        exercise = new Exercise(rs.getLong("exercise_id"), rs.getString("exercise_name"), rs.getString("exercise_description"), rs.getString("explanation"), rs.getString("demo_video_url"), true, new HashSet<>());
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
        String sql = "DELETE FROM training_system.exercise WHERE id = ?";

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
                throw new DAOException("error durante el rollback", rollbackEx);
            }
            throw new DAOException("error al eliminar el ejercicio", e);
        }
    }

    @Override
    public Exercise createExerciseForClient(Exercise exercise) throws DAOException {
        return null;
    }

    @Override
    public Set<Exercise> listUserExercises(User user) throws DAOException {
        return Set.of();
    }
}
