package gimgut.postbasedsocial.api.post;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.validation.constraints.Size;

public class CreatePostRequestDto {

    @Size(min = 1, max = 256)
    private String title;

    @Size(min = 1, max = 10000)
    private String content;

    public CreatePostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

