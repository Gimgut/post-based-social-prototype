package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.shared.jpa.Hideable;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "UPDATE post SET visible = false WHERE id = ?")
@FilterDef(name = "visiblePostFilter", parameters = @ParamDef(name = "isVisible", type = "boolean"))
@Filter(name = "visiblePostFilter", condition = "visible :isVisible")
public class Post implements Hideable {

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
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_info_id")
    private UserInfo author;

    private boolean visible;

    public Post() {
    }

    public Post(long id, String title, String content, int rating, LocalDateTime createdAt, UserInfo author, boolean visible) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.author = author;
        this.visible = visible;
    }

    public Post(String title, String content, int rating, LocalDateTime createdAt, UserInfo author, boolean visible) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.author = author;
        this.visible = visible;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}

