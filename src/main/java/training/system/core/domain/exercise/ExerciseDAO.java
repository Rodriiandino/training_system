package training.system.core.domain.exercise;

import training.system.core.generic.GenericDao;

import java.util.Set;

public class ExerciseDAO implements GenericDao<Exercise, Long> {
    @Override
    public Exercise create(Exercise entity) {
        return null;
    }

    @Override
    public Exercise update(Exercise entity) {
        return null;
    }

    @Override
    public Set<Exercise> list() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
