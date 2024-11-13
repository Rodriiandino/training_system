package training.system.core.domain.routine;

import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;

import java.util.Set;

public class Routine {
    private Long id;
    private String name;
    private String description;
    private final User user;
    private User trainer;
    private Set<Exercise> exercises;

    public Routine(String name, String description, User user, Set<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.exercises = exercises;
    }

    public Routine(Long id, String name, String description, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Routine(String name, String description, User user, User trainer, Set<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.trainer = trainer;
        this.exercises = exercises;
    }

    public Routine(Long id, String name, String description, User user, Set<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.exercises = exercises;
    }

    public Routine(Long id, String name, String description, User user, User trainer, Set<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.trainer = trainer;
        this.exercises = exercises;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public User getTrainer() {
        return trainer;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void addExercises(Set<Exercise> exercises) {
        this.exercises.addAll(exercises);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void removeExercises(Set<Exercise> exercises) {
        this.exercises.removeAll(exercises);
    }

    @Override
    public String toString() {
        return "Routine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", trainer=" + trainer +
                ", exercises=" + exercises +
                '}';
    }
}
