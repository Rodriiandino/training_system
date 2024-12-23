package training.system.core.domain.routine;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IRoutine {
    Routine createRoutineForClient(Routine routine) throws Exception;
    Set<Routine> listUserRoutines(User user) throws Exception;
}
