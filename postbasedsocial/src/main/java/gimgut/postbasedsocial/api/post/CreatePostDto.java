package gimgut.postbasedsocial.api.post;

public class CreatePostDto {
    private String title;
    private String content;

    public CreatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

