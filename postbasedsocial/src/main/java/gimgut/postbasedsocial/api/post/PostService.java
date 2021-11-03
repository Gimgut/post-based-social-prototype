package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class PostService {

    private final EntityManager entityManager;
    private final PostRepository postRepository;
    private final TimeService timeService;

    public PostService(EntityManager entityManager, PostRepository postRepository, TimeService timeService) {
        this.entityManager = entityManager;
        this.postRepository = postRepository;
        this.timeService = timeService;
    }

    @Transactional
    public Long createNewPost(PostRequestDto newPostDto, Long authorId) {
        UserInfo author = entityManager.getReference(UserInfo.class, authorId);
        Post post = new Post(
                newPostDto.getTitle(),
                newPostDto.getContent(),
                0,
                timeService.getUtcNowLDT(),
                author,
                true);
        entityManager.persist(post);
        return post.getId();
    }

    @Transactional
    public EditPostResponseStatus editPost(Long postId,PostRequestDto postRequestDto, Authentication authentication) {
        Post post = postRepository.getById(postId);
        if (post == null) {
            return EditPostResponseStatus.POST_NOT_FOUND;
        }

        if (canEdit(post, authentication)) {
            post.setTitle(postRequestDto.getTitle());
            post.setContent(postRequestDto.getContent());
            return EditPostResponseStatus.SUCCESS;
        } else {
            return EditPostResponseStatus.NO_AUTHORITY;
        }
    }

    /**
     * 1.Admin can edit every post
     * 2.User can edit only his own posts
     * @param post
     * @param authentication
     * @return
     */
    private boolean canEdit(Post post, Authentication authentication) {
        Long userInfoId = Long.valueOf(authentication.getName());
        Roles role = (Roles) authentication.getAuthorities().iterator().next();
        if ((role == Roles.ADMIN) || (post.getAuthor().getId() == userInfoId)) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public EditPostResponseStatus deletePost(Long postId, Authentication authentication) {
        Post post = postRepository.getById(postId);
        if (post == null) {
            return EditPostResponseStatus.POST_NOT_FOUND;
        }

        if (canDelete(post, authentication)) {
            postRepository.softDelete(post.getId());
            return EditPostResponseStatus.SUCCESS;
        } else {
            return EditPostResponseStatus.NO_AUTHORITY;
        }
    }

    /**
     * 1.Admin can delete every post
     * 2.User can delete only his own posts
     * @param post
     * @param authentication
     * @return
     */
    private boolean canDelete(Post post, Authentication authentication) {
        Long userInfoId = Long.valueOf(authentication.getName());
        Roles role = (Roles) authentication.getAuthorities().iterator().next();
        if ((role == Roles.ADMIN) || (post.getAuthor().getId() == userInfoId)) {
            return true;
        } else {
            return false;
        }
    }
}