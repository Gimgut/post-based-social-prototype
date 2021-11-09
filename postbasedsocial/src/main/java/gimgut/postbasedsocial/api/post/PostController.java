package gimgut.postbasedsocial.api.post;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final PostService postService;
    private final Validator validator;
    private final PostCleaner postCleaner;

    public PostController(PostMapper postMapper, PostRepository postRepository, PostService postService, Validator validator, PostCleaner postCleaner) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.postService = postService;
        this.validator = validator;
        this.postCleaner = postCleaner;
    }

    @GetMapping("{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findByIdVisible(id);
        if (!post.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PostDto postDto = postMapper.toPostDto(post.get());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * @param postDto
     * @param principal
     * @return created post id on success
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity createNewPost(@RequestBody PostRequestDto postDto, Principal principal) {
        postCleaner.clean(postDto);

        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postDto);
        if (!violations.isEmpty()) {
            return new ResponseEntity<>("BAD_PARAMETERS", HttpStatus.BAD_REQUEST);
        }

        Long userInfoId = Long.valueOf(principal.getName());
        Long postId = postService.createNewPost(postDto, userInfoId);
        return new ResponseEntity(postId, HttpStatus.CREATED);
    }


    @PatchMapping("{id}")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity editPost(
            @PathVariable @NotNull Long id,
            @RequestBody PostRequestDto postRequestDto,
            Authentication authentication) {
        postCleaner.clean(postRequestDto);

        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postRequestDto);
        if (!violations.isEmpty()) {
            return new ResponseEntity<>("BAD_PARAMETERS", HttpStatus.BAD_REQUEST);
        }

        EditPostResponseStatus status = postService.editPost(id, postRequestDto, authentication);
        if (status == EditPostResponseStatus.SUCCESS) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(status.name(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity deletePost(@PathVariable @NotNull Long id, Authentication authentication) {
        EditPostResponseStatus status = postService.deletePost(id, authentication);
        if (status == EditPostResponseStatus.SUCCESS) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(status.name(), HttpStatus.BAD_REQUEST);
        }
    }
}
