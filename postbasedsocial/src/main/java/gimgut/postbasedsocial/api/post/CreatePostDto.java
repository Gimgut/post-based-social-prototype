package gimgut.postbasedsocial.api.post;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.validation.constraints.Size;

public class CreatePostDto {

    @Size(min = 3, max = 256)
    private String title;

    @Size(min = 3, max = 10000)
    private String content;

    public CreatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
        Jsoup.clean(title, Safelist.none());
        Jsoup.clean(content, Safelist.basicWithImages());
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

