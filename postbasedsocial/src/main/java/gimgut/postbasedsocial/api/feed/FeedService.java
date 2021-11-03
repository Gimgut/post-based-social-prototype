package gimgut.postbasedsocial.api.feed;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * TODO: If the project expands, replace query creation
 * TODO: into builder/chain/decorator with
 * TODO: query modification register/unregister
 * TODO: to follow open/closed principle on project expansion
 */
@Service
public class FeedService {

    private final FeedRecentRepository feedRecentRepository;
    private final Pageable defaultPage = PageRequest.of(0,20);

    public FeedService(FeedRecentRepository feedRecentRepository) {
        this.feedRecentRepository = feedRecentRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPosts(Long lastViewedPostId) {
        return feedRecentRepository.findNewestPostsAfterId(defaultPage, lastViewedPostId);
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPosts() {
        return feedRecentRepository.findNewestPosts(defaultPage);
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPostsOfUser(Long userId) {
        return feedRecentRepository.findNewestPostsOfUser(defaultPage, userId);
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPostsOfUser(Long userId, Long lastViewedPostId) {
        return feedRecentRepository.findNewestPostsOfUserAfterId(defaultPage, lastViewedPostId, userId);
    }
}
