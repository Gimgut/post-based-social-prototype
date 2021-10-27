package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.security.Roles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
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
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {

        Optional<Post> post = postRepository.findById(id);
        logger.info("post visible = " + post.get().isVisible());
        return post.isPresent() ?
                new ResponseEntity<>(post.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * On success returns created Post.id
     * @param newPostDto
     * @param principal
     * @return
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity<Long> createNewPost(@RequestBody @Valid CreatePostRequestDto newPostDto, Principal principal) {
        Long uiid = Long.valueOf(principal.getName());
        Long postId = postService.createNewPost(newPostDto, uiid);
        return new ResponseEntity(postId,HttpStatus.OK);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity editPost(@RequestBody @Valid EditPostRequestDto postDto, Authentication authentication) {
        Long uiid = Long.valueOf(authentication.getName());
        Roles role = (Roles) authentication.getAuthorities().iterator().next();
        EditPostResponseStatus status = postService.editPost(postDto, uiid, role);
        return status == EditPostResponseStatus.SUCCESS ?
                new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(status.name(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/delete")
    //@PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity editPost(@RequestBody @Valid DeletePostRequestDto dto, Authentication authentication) {
        Long uiid = Long.valueOf(authentication.getName());
        Roles role = (Roles) authentication.getAuthorities().iterator().next();
        EditPostResponseStatus status = postService.deletePost(dto, uiid, role);
        return status == EditPostResponseStatus.SUCCESS ?
                new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(status.name(), HttpStatus.BAD_REQUEST);
    }
}
