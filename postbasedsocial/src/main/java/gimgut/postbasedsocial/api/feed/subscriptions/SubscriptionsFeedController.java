package gimgut.postbasedsocial.api.feed.subscriptions;

import gimgut.postbasedsocial.api.post.Post;
import gimgut.postbasedsocial.api.post.PostDto;
import gimgut.postbasedsocial.api.post.PostMapper;
import gimgut.postbasedsocial.api.subscription.Subscription;
import gimgut.postbasedsocial.api.subscription.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/feed/subscriptions")
public class SubscriptionsFeedController {

    private final SubscriptionsFeedService subscriptionsFeedService;
    private final SubscriptionService subscriptionService;
    private final PostMapper postMapper;

    public SubscriptionsFeedController(SubscriptionsFeedService subscriptionsFeedService, SubscriptionService subscriptionService, PostMapper postMapper) {
        this.subscriptionsFeedService = subscriptionsFeedService;
        this.subscriptionService = subscriptionService;
        this.postMapper = postMapper;
    }

    @GetMapping("recent")
    public ResponseEntity<List<PostDto>> getSubscriptionsPosts(
            @RequestParam(required = false) Long lastPostId,
            Authentication authentication) {

        Long idSubscriber = Long.valueOf(authentication.getName());
        Optional<Subscription> subscription = subscriptionService.getSubscription(idSubscriber);
        if (subscription.isEmpty() || subscription.get().getSubscriptions().isEmpty()) {
            return new ResponseEntity(new ArrayList(), HttpStatus.OK);
        }

        List<Post> posts;
        if (lastPostId == null) {
            posts = subscriptionsFeedService.getRecentPosts(subscription.get().getSubscriptions());
        } else {
            posts = subscriptionsFeedService.getRecentPostsAfterId(subscription.get().getSubscriptions(), lastPostId);
        }
        return new ResponseEntity(postMapper.toListPostDto(posts), HttpStatus.OK);
    }
}
