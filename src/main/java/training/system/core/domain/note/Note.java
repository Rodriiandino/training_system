package training.system.core.domain.note;

import training.system.core.domain.user.User;

import java.util.Date;

public class Note {

    private Long id;
    private String title;
    private String content;
    private String purpose;
    private Date noteDate;
    private final User user;
    private User trainer;

    public Note(String title, String content, String purpose, Date noteDate, User user) {
        this.title = title;
        this.content = content;
        this.purpose = purpose;
        this.noteDate = noteDate;
        this.user = user;
    }

    public Note(String title, String content, String purpose, Date noteDate, User user, User trainer) {
        this.title = title;
        this.content = content;
        this.purpose = purpose;
        this.noteDate = noteDate;
        this.user = user;
        this.trainer = trainer;
    }

    public Note(Long id, String title, String content, String purpose, Date noteDate, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.purpose = purpose;
        this.noteDate = noteDate;
        this.user = user;
    }

    public Note(Long id, String title, String content, String purpose, Date noteDate, User user, User trainer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.purpose = purpose;
        this.noteDate = noteDate;
        this.user = user;
        this.trainer = trainer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public User getUser() {
        return user;
    }

    public User getTrainer() {
        return trainer;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", purpose='" + purpose + '\'' +
                ", noteDate=" + noteDate +
                ", user=" + user +
                ", trainer=" + trainer +
                '}';
    }
}
