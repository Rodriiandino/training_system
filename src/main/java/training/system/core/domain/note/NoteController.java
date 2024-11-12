package training.system.core.domain.note;

import training.system.core.domain.user.User;
import training.system.core.exception.ControllerException;
import training.system.core.exception.DAOException;
import training.system.core.exception.DatabaseConnectionException;
import training.system.core.factory.ConnectionFactory;
import training.system.core.generic.GenericController;

import java.util.Set;

public class NoteController implements GenericController<Note, Long>, INote {

    private final NoteDAO noteDAO;

    public NoteController() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            this.noteDAO = new NoteDAO(connectionFactory.getConnection());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador de nota", e);
        }
    }

    @Override
    public Note create(Note entity) throws ControllerException {
        try {
            return noteDAO.create(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear la nota", e);
        }
    }

    @Override
    public Set<Note> list() throws ControllerException {
        try {
            return noteDAO.list();
        } catch (DAOException e) {
            throw new ControllerException("Error al listar las notas", e);
        }
    }

    @Override
    public Note search(Long aLong) throws ControllerException {
        try {
            return noteDAO.search(aLong);
        } catch (DAOException e) {
            throw new ControllerException("Error al buscar la nota", e);
        }
    }

    @Override
    public Note update(Note entity) throws ControllerException {
        try {
            return noteDAO.update(entity);
        } catch (DAOException e) {
            throw new ControllerException("Error al actualizar la nota", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ControllerException {
        try {
            return noteDAO.delete(id);
        } catch (DAOException e) {
            throw new ControllerException("Error al eliminar la nota", e);
        }
    }

    @Override
    public Note createNoteForClient(Note note) throws ControllerException {
        try {
            return noteDAO.createNoteForClient(note);
        } catch (DAOException e) {
            throw new ControllerException("Error al crear la nota", e);
        }
    }

    @Override
    public Set<Note> listUserNotes(User user) throws ControllerException {
        try {
            return noteDAO.listUserNotes(user);
        } catch (DAOException e) {
            throw new ControllerException("Error al listar las notas del usuario", e);
        }
    }
}

