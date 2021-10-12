package gimgut.postbasedsocial.api.post;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Post {

    @Id
    @SequenceGenerator(name = "publication_sequence", sequenceName = "publication_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(length = 128)
    private String title;

    @Column(length = 10000)
    private String content;

    private int rating;
    private Timestamp datetime;

    public Post() {
    }

    public Post(long id, String title, String content, int rating, Timestamp datetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.datetime = datetime;
    }

    public Post(String title, String content, int rating, Timestamp datetime) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.datetime = datetime;
    }

    public Post(String title, String content, Timestamp datetime) {
        this.title = title;
        this.content = content;
        this.datetime = datetime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", datetime=" + datetime +
                '}';
    }
}

