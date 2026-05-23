package tb25.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String progressType; // "CHAPTER" or "PAGE"
    private int progress;
    private String coverUrl;

    public Novel() {}

    public Novel(String title, String author, String progressType, int progress) {
        this.title = title;
        this.author = author;
        this.progressType = progressType;
        this.progress = progress;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getProgressType() { return progressType; }
    public void setProgressType(String progressType) { this.progressType = progressType; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
