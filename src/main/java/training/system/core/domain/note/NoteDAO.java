package training.system.core.domain.note;

import training.system.core.generic.GenericDao;

import java.util.Set;

public class NoteDAO implements GenericDao<Note, Long> {
    @Override
    public Note create(Note entity) {
        return null;
    }

    @Override
    public Note update(Note entity) {
        return null;
    }

    @Override
    public Set<Note> list() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
