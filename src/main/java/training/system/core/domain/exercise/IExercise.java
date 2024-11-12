package training.system.core.domain.exercise;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IExercise {
    Exercise createExerciseForClient(Exercise exercise) throws Exception;
    Set<Exercise> listUserExercises(User user) throws Exception;
}
