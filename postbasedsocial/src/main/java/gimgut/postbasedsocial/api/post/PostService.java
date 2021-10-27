package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
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
    public Long createNewPost(CreatePostRequestDto newPostDto, Long authorId) {
        UserInfo author = entityManager.getReference(UserInfo.class, authorId);
        Post post = new Post(newPostDto.getTitle(), newPostDto.getContent(),0, timeService.getUtcNowLDT(), author, true);
        entityManager.persist(post);
        return post.getId();
    }

    @Transactional
    public EditPostResponseStatus editPost(EditPostRequestDto postDto, Long uiid, Roles role) {
        Post post = postRepository.getById(postDto.getPostId());
        if (post == null) return EditPostResponseStatus.POST_NOT_FOUND;

        //1.Admin can edit every post
        //2.User can edit only his own posts
        if (role == Roles.ADMIN || post.getAuthor().getId() == uiid) {
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
        } else { return EditPostResponseStatus.NO_AUTHORITY; }

        return EditPostResponseStatus.SUCCESS;
    }

    @Transactional
    public EditPostResponseStatus deletePost(DeletePostRequestDto dto, Long uiid, Roles role) {
        Post post = postRepository.getById(dto.getPostId());
        if (post == null) return EditPostResponseStatus.POST_NOT_FOUND;

        //1.Admin can delete any post
        //2.User can delete only his own posts
        if (role == Roles.ADMIN || post.getAuthor().getId() == uiid) {
            //TODO: Delete
            //postRepository.softDelete(post.getId());
            post.setVisible(false);
            System.out.println("DELETED!");
        } else { return EditPostResponseStatus.NO_AUTHORITY; }

        return EditPostResponseStatus.SUCCESS;
    }
}