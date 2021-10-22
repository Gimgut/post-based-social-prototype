package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;

import javax.persistence.*;
import java.time.LocalDateTime;

public class PostDto {
    private long id;
    private String title;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
    private Long authorId;
    private String authorName;
}
