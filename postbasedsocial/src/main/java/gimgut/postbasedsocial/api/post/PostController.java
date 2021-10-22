package gimgut.postbasedsocial.api.post;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final PostRepository postRepository;
    private final PostService postService;

    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id, Principal principal) { //@RequestParam Long id
        logger.info("principal id = " + principal.getName());
        Optional<Post> optPost = postRepository.findById(id);
        return optPost.isPresent() ?
                new ResponseEntity<>(optPost.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity createNewPost(@RequestBody CreatePostDto newPostDto, Principal principal) {
        logger.info("principal id = " + principal.getName());
        postService.createNewPost(newPostDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
