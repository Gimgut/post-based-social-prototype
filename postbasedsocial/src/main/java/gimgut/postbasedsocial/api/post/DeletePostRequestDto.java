package gimgut.postbasedsocial.api.post;

import javax.validation.constraints.NotNull;

public class DeletePostRequestDto {

    @NotNull
    private Long postId;

    public DeletePostRequestDto() {
    }

    public DeletePostRequestDto(Long postId) {
        this.postId = postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
