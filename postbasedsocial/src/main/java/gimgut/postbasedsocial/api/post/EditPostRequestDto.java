package gimgut.postbasedsocial.api.post;

import javax.validation.constraints.NotNull;

public class EditPostRequestDto extends CreatePostRequestDto {

    @NotNull
    private Long postId;

    public EditPostRequestDto(Long postId,String title, String content) {
        super(title, content);
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
