package gimgut.postbasedsocial.api.feed;

import gimgut.postbasedsocial.api.post.Post;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final FeedRecentRepository feedRecentRepository;

    public FeedController(FeedRecentRepository feedRecentRepository) {
        this.feedRecentRepository = feedRecentRepository;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Post>> getRecentPosts(@RequestParam(required = false) Long lastPostId) {
        List<Post> result = null;
        if (lastPostId == null)
            result = feedRecentRepository.findNewestPosts(20);
        else
            result = feedRecentRepository.findNewestPostsAfterId(lastPostId, 20);
        return result == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(result, HttpStatus.OK);
    }
}
