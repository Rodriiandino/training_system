package training.system.core.domain.note;

import training.system.core.domain.user.User;
import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class NoteDAO implements GenericDao<Note, Long>, INote {

    Connection connection;

    public NoteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Note create(Note entity) throws DAOException {
        String sql = """
                INSERT INTO training_system.note (title, content, purpose, note_date, user_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getContent());
            stmt.setString(3, entity.getPurpose());
            stmt.setDate(4, new java.sql.Date(entity.getNoteDate().getTime()));
            stmt.setLong(5, entity.getUser().getId());
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
            throw new DAOException("Error creando la nota", e);
        }
    }

    @Override
    public Note update(Note entity) throws DAOException {
        String sql = """
                UPDATE training_system.note
                SET title = ?, content = ?, purpose = ?, note_date = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getContent());
            stmt.setString(3, entity.getPurpose());
            stmt.setDate(4, new java.sql.Date(entity.getNoteDate().getTime()));
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();

            connection.commit();
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DAOException("Error durante el rollback", rollbackEx);
            }
            throw new DAOException("Error actualizando la nota", e);
        }
    }

    @Override
    public Set<Note> list() throws DAOException {
        String sql = """
                    SELECT
                    n.id AS note_id,
                    n.title AS note_title,
                    n.content AS note_content,
                    n.purpose AS note_purpose,
                    n.note_date AS note_date,
                    u.id AS user_id,
                    p.first_name AS user_first_name,
                    p.last_name AS user_last_name,
                    p.email AS user_email,
                    u2.id AS trainer_id,
                    p2.first_name AS trainer_first_name,
                    p2.last_name AS trainer_last_name,
                    p2.email AS trainer_email
                        FROM training_system.note n
                            LEFT JOIN training_system.user u ON n.user_id = u.id
                            LEFT JOIN training_system.person p ON u.id = p.id
                            LEFT JOIN training_system.user u2 ON n.trainer_id = u2.id
                            LEFT JOIN training_system.person p2 ON u2.id = p2.id
                """;

        Set<Note> notes = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Note note;

                User user = new User(
                        rs.getLong("user_id"),
                        rs.getString("user_first_name"),
                        rs.getString("user_last_name"),
                        rs.getString("user_email")
                );

                if (rs.getLong("trainer_id") == 0) {
                    note = new Note(
                            rs.getLong("note_id"),
                            rs.getString("note_title"),
                            rs.getString("note_content"),
                            rs.getString("note_purpose"),
                            rs.getDate("note_date"),
                            user
                    );

                } else {
                    User trainer = new User(
                            rs.getLong("trainer_id"),
                            rs.getString("trainer_first_name"),
                            rs.getString("trainer_last_name"),
                            rs.getString("trainer_email")
                    );

                    note = new Note(
                            rs.getLong("note_id"),
                            rs.getString("note_title"),
                            rs.getString("note_content"),
                            rs.getString("note_purpose"),
                            rs.getDate("note_date"),
                            user,
                            trainer
                    );
                }

                notes.add(note);
            }
        } catch (SQLException e) {
            throw new DAOException("Error listando notas", e);
        }

        return notes;
    }

    @Override
    public Note search(Long aLong) throws DAOException {
        String sql = """
                SELECT
                n.id AS note_id,
                n.title AS note_title,
                n.content AS note_content,
                n.purpose AS note_purpose,
                n.note_date AS note_date,
                u.id AS user_id,
                p.first_name AS user_first_name,
                p.last_name AS user_last_name,
                p.email AS user_email,
                u2.id AS trainer_id,
                p2.first_name AS trainer_first_name,
                p2.last_name AS trainer_last_name,
                p2.email AS trainer_email
                    FROM training_system.note n
                        LEFT JOIN training_system.user u ON n.user_id = u.id
                        LEFT JOIN training_system.person p ON u.id = p.id
                        LEFT JOIN training_system.user u2 ON n.trainer_id = u2.id
                        LEFT JOIN training_system.person p2 ON u2.id = p2.id
                WHERE n.id = ?
                """;

        Note note = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, aLong);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getLong("user_id"),
                            rs.getString("user_first_name"),
                            rs.getString("user_last_name"),
                            rs.getString("user_email")
                    );

                    if (rs.getLong("trainer_id") == 0) {

                        note = new Note(
                                rs.getLong("note_id"),
                                rs.getString("note_title"),
                                rs.getString("note_content"),
                                rs.getString("note_purpose"),
                                rs.getDate("note_date"),
                                user
                        );

                    } else {
                        User trainer = new User(
                                rs.getLong("trainer_id"),
                                rs.getString("trainer_first_name"),
                                rs.getString("trainer_last_name"),
                                rs.getString("trainer_email")
                        );

                        note = new Note(
                                rs.getLong("note_id"),
                                rs.getString("note_title"),
                                rs.getString("note_content"),
                                rs.getString("note_purpose"),
                                rs.getDate("note_date"),
                                user,
                                trainer
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error buscando nota", e);
        }

        return note;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String sql = "DELETE FROM training_system.note WHERE id = ?";

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
            throw new DAOException("Error eliminando la nota", e);
        }
    }

    @Override
    public Note createNoteForClient(Note note) throws DAOException {
        return null;
    }

    @Override
    public Set<Note> listUserNotes(User user) throws DAOException {
        return Set.of();
    }
}
