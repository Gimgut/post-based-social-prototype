package gimgut.postbasedsocial.api.feed.recent;

import gimgut.postbasedsocial.api.post.Post;
import gimgut.postbasedsocial.api.post.PostDto;
import gimgut.postbasedsocial.api.post.PostMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feed/recent")
public class FeedRecentController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final FeedRecentService feedRecentService;
    private final PostMapper postMapper;

    public FeedRecentController(FeedRecentService feedRecentService, PostMapper postMapper) {
        this.feedRecentService = feedRecentService;
        this.postMapper = postMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<PostDto>> getRecentPosts(
            @RequestParam(required = false) Long lastPostId) {
        List<Post> posts;
        if (lastPostId == null) {
            posts = feedRecentService.getRecentPosts();
        } else {
            posts = feedRecentService.getRecentPosts(lastPostId);
        }
        return new ResponseEntity<>(postMapper.toListPostDto(posts), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostDto>> getUserRecentPosts(
            @PathVariable @NotNull Long id,
            @RequestParam(required = false) Long lastPostId) {
        List<Post> posts;
        if (lastPostId == null) {
            posts = feedRecentService.getRecentPostsOfUser(id);
        } else {
            posts = feedRecentService.getRecentPostsOfUser(id, lastPostId);
        }
        return new ResponseEntity<>(postMapper.toListPostDto(posts), HttpStatus.OK);
    }
}
