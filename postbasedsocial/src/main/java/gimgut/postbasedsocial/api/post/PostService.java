package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.services.TimeService;
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
        Post post = new Post(newPostDto.getTitle(), newPostDto.getContent(),0, timeService.getUtcNowLDT(), author);
        entityManager.persist(post);
        return post.getId();
    }
}