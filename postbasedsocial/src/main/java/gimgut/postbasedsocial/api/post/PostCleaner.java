package gimgut.postbasedsocial.api.post;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class PostCleaner {

    //TODO: Tune JSoup.clean to be more restrictive to content, and make it synch-ed with front-end post editor
    public void clean(CreatePostRequestDto postDto) {
        postDto.setTitle(this.cleanTitle(postDto.getTitle()));
        postDto.setContent(this.cleanContent(postDto.getContent()));
    }

    private String cleanTitle(String title) {
        title = title.trim();
        return Jsoup.clean(title, Safelist.none());
    }

    private String cleanContent(String content) {
        content = content.trim();
        return Jsoup.clean(content, Safelist.basicWithImages());
    }
}
