package gimgut.postbasedsocial.api.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> optPost = postRepository.findById(id);
        return optPost.isPresent() ? new ResponseEntity<>(optPost.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity createNewPost(@RequestBody CreatePostDto newPostDto) {
        postService.createNewPost(newPostDto);
        return new ResponseEntity(HttpStatus.OK);
    }
    /* variant 2
    @GetMapping("")
    public Post getPostById(@RequestParam Long id) {
        return postRepository.findById(id).orElse(null);
    }
    */
}
