package training.system.core.domain.gym;

import training.system.core.generic.GenericController;

import java.util.Set;

public class GymController implements GenericController<Gym, Long> {
    @Override
    public Gym create(Gym entity) {
        return null;
    }

    @Override
    public Gym update(Gym entity) {
        return null;
    }

    @Override
    public Set<Gym> list() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
