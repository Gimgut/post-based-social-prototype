package gimgut.postbasedsocial.api.feed;

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
@RequestMapping("/api/v1/feed")
public class FeedController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final FeedService feedService;
    private final PostMapper postMapper;

    public FeedController(FeedService feedService, PostMapper postMapper) {
        this.feedService = feedService;
        this.postMapper = postMapper;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostDto>> getRecentPosts(@RequestParam(required = false) Long lastPostId) {
        List<Post> posts;
        if (lastPostId == null) {
            posts = feedService.getRecentPosts();
        } else {
            posts = feedService.getRecentPosts(lastPostId);
        }
        return new ResponseEntity<>(postMapper.toListPostDto(posts), HttpStatus.OK);
    }

    @GetMapping("/recent/user/{id}")
    public ResponseEntity<List<PostDto>> getUserRecentPosts(
            @PathVariable @NotNull Long id,
            @RequestParam(required = false) Long lastPostId) {
        List<Post> posts;
        if (lastPostId == null) {
            posts = feedService.getRecentPostsOfUser(id);
        } else {
            posts = feedService.getRecentPostsOfUser(id, lastPostId);
        }
        return new ResponseEntity<>(postMapper.toListPostDto(posts), HttpStatus.OK);
    }
}
