package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.services.TimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class PostService {

    private final EntityManager entityManager;
    private final TimeService timeService;

    public PostService(EntityManager entityManager, TimeService timeService) {
        this.entityManager = entityManager;
        this.timeService = timeService;
    }

    @Transactional
    public Post createNewPost(CreatePostDto newPostDto) {
        Post post = new Post(newPostDto.getTitle(), newPostDto.getContent(), timeService.getUtcNowTS());
        entityManager.persist(post);
        return post;
    }
}