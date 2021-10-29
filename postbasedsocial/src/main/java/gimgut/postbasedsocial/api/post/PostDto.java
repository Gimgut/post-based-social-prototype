package gimgut.postbasedsocial.api.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import gimgut.postbasedsocial.api.user.UserInfoDto;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private int rating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private UserInfoDto author;

    public PostDto() {
    }

    public PostDto(Long id, String title, String content, int rating, LocalDateTime createdAt, UserInfoDto userInfoDto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.author = userInfoDto;
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

    public UserInfoDto getAuthor() {
        return author;
    }

    public void setAuthor(UserInfoDto author) {
        this.author = author;
    }
}
