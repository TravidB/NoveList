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
    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String tags;
    @jakarta.persistence.Column(columnDefinition = "INTEGER DEFAULT 0")
    private int rating;
    private Long userId;

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
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
