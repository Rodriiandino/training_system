package training.system.core.domain.routine;

import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;

import java.util.List;

public class Routine {
    private int id;
    private String name;
    private String description;
    private User user;
    private User trainer;
    private List<Exercise> exercises;

    public Routine(String name, String description, User user, List<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.exercises = exercises;
    }

    public Routine(String name, String description, User user, User trainer, List<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.trainer = trainer;
        this.exercises = exercises;
    }

    public Routine(int id, String name, String description, User user, List<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.exercises = exercises;
    }

    public Routine(int id, String name, String description, User user, User trainer, List<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.trainer = trainer;
        this.exercises = exercises;
    }

    public int getId() {
        return id;
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

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }
}
