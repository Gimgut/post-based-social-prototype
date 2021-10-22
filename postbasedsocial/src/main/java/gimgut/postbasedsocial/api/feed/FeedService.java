package gimgut.postbasedsocial.api.feed;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {

    private final FeedRecentRepository feedRecentRepository;

    public FeedService(FeedRecentRepository feedRecentRepository) {
        this.feedRecentRepository = feedRecentRepository;
    }

    public List<Post> getRecentPosts(Long lastViewedPostId) {
        Pageable page = PageRequest.of(0,20);
        if (lastViewedPostId == null)
            return feedRecentRepository.findNewestPosts(page);
        else
            return feedRecentRepository.findNewestPostsAfterId(page, lastViewedPostId);
    }
}
