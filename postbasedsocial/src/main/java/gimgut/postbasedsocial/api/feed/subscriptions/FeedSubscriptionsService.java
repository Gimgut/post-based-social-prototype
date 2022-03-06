package gimgut.postbasedsocial.api.feed.subscriptions;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FeedSubscriptionsService {

    private final FeedSubscriptionsRepository feedSubscriptionsRepository;
    private final Pageable defaultPage = PageRequest.of(0, 20);

    public FeedSubscriptionsService(FeedSubscriptionsRepository feedSubscriptionsRepository) {
        this.feedSubscriptionsRepository = feedSubscriptionsRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPosts(Collection idsPublishers) {
        if (idsPublishers.size() < 1) {
            return new ArrayList<>();
        }
        return this.feedSubscriptionsRepository.findNewestPostsOfUsers(defaultPage, idsPublishers);
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPostsAfterId(Collection idsPublishers, Long lastViewedPostId) {
        if (idsPublishers.size() < 1) {
            return new ArrayList<>();
        }
        return this.feedSubscriptionsRepository.findNewestPostsOfUsersAfterId(defaultPage, lastViewedPostId, idsPublishers);
    }
}
