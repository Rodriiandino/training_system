package training.system.core.domain.exercise;

import training.system.core.domain.category.Category;
import training.system.core.domain.user.User;

import java.util.Set;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private String explanation;
    private String videoUrl;
    private boolean isPredefined;
    private Set<Category> categories;
    private User user;
    private User trainer;

    public Exercise(String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories) {
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
    }

    public Exercise(String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories, User user) {
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
        this.user = user;
    }

    public Exercise(String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories, User user, User trainer) {
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
        this.user = user;
        this.trainer = trainer;
    }

    public Exercise(int id, String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
    }

    public Exercise(int id, String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
        this.user = user;
    }

    public Exercise(int id, String name, String description, String explanation, String videoUrl, boolean isPredefined, Set<Category> categories, User user, User trainer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.videoUrl = videoUrl;
        this.isPredefined = isPredefined;
        this.categories = categories;
        this.user = user;
        this.trainer = trainer;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean getIsPredefined() {
        return isPredefined;
    }

    public void setIsPredefined() {
        this.isPredefined = !isPredefined;
    }

    public User getUser() {
        return user;
    }

    public User getTrainer() {
        return trainer;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", explanation='" + explanation + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", isPredefined='" + isPredefined + '\'' +
                ", user=" + user +
                ", trainer=" + trainer +
                '}';
    }
}
