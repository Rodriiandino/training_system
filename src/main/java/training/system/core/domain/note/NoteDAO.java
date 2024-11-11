package training.system.core.domain.note;

import training.system.core.exception.DAOException;
import training.system.core.generic.GenericDao;

import java.sql.Connection;
import java.util.Set;

public class NoteDAO implements GenericDao<Note, Long> {

    Connection connection;

    public NoteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Note create(Note entity) throws DAOException {
        return null;
    }

    @Override
    public Note update(Note entity) throws DAOException {
        return null;
    }

    @Override
    public Set<Note> list() throws DAOException {
        return null;
    }

    @Override
    public Note search(Long aLong) throws DAOException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }
}
