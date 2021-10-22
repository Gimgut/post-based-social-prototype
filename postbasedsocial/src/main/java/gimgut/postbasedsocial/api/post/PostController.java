package gimgut.postbasedsocial.api.post;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final PostRepository postRepository;
    private final PostService postService;

    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) { //@RequestParam Long id

        Optional<Post> optPost = postRepository.findById(id);
        return optPost.isPresent() ?
                new ResponseEntity<>(optPost.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity createNewPost(@RequestBody CreatePostDto newPostDto, Principal principal) {
        logger.info("principal id = " + principal.getName());
        Long uiid = Long.valueOf(principal.getName());
        postService.createNewPost(newPostDto, uiid);
        return new ResponseEntity(HttpStatus.OK);
    }
}
