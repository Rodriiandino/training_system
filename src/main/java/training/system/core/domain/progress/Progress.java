package training.system.core.domain.progress;

import training.system.core.domain.exercise.Exercise;
import training.system.core.domain.user.User;

import java.util.Date;

public class Progress {
    private Long id;
    private Date progressDate;
    private int repetitions;
    private int weight;
    private int time;
    private final User user;
    private User trainer;
    private Exercise exercise;

    public Progress(Date progressDate, int repetitions, int weight, int time, User user, Exercise exercise) {
        this.progressDate = progressDate;
        this.repetitions = repetitions;
        this.weight = weight;
        this.time = time;
        this.user = user;
        this.exercise = exercise;
    }

    public Progress(Date progressDate, int repetitions, int weight, int time, User user, User trainer, Exercise exercise) {
        this.progressDate = progressDate;
        this.repetitions = repetitions;
        this.weight = weight;
        this.time = time;
        this.user = user;
        this.trainer = trainer;
        this.exercise = exercise;
    }

    public Progress(Long id, Date progressDate, int repetitions, int weight, int time, User user, Exercise exercise) {
        this.id = id;
        this.progressDate = progressDate;
        this.repetitions = repetitions;
        this.weight = weight;
        this.time = time;
        this.user = user;
        this.exercise = exercise;
    }

    public Progress(Long id, Date progressDate, int repetitions, int weight, int time, User user, User trainer, Exercise exercise) {
        this.id = id;
        this.progressDate = progressDate;
        this.repetitions = repetitions;
        this.weight = weight;
        this.time = time;
        this.user = user;
        this.trainer = trainer;
        this.exercise = exercise;
    }

    public Long getId() {
        return id;
    }

    public Date getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(Date progressDate) {
        this.progressDate = progressDate;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public User getTrainer() {
        return trainer;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "id=" + id +
                ", progressDate=" + progressDate +
                ", repetitions=" + repetitions +
                ", weight=" + weight +
                ", time=" + time +
                ", user=" + user +
                ", trainer=" + trainer +
                ", exercise=" + exercise +
                '}';
    }
}
