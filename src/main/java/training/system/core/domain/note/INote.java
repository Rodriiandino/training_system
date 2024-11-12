package training.system.core.domain.note;

import training.system.core.domain.user.User;

import java.util.Set;

public interface INote {
    Note createNoteForClient(Note note) throws Exception;
    Set<Note> listUserNotes(User user) throws Exception;
}
