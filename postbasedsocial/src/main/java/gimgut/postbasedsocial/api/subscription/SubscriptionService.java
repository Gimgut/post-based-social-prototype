package gimgut.postbasedsocial.api.subscription;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/*
 * TODO: For a real big app Redis would be better to store
 * TODO: subscriptions and update immediate subscribers count.
 * For now, normalized table is introduced
 */
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserInfoRepository userInfoRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserInfoRepository userInfoRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    public void subscribe(Long idSubscriber, Long idPublisher) {
        Optional<Subscription> subscription = subscriptionRepository.findById(idSubscriber);
        if (subscription.isPresent()) {
            subscription.get().getSubscriptions().add(idPublisher);
            return;
        } else {
            Subscription newSubscription = new Subscription();
            newSubscription.setId(idSubscriber);
            newSubscription.setSubscriptions(new HashSet<>(Arrays.asList(idPublisher)));
            subscriptionRepository.save(newSubscription);
            return;
        }
    }

    @Transactional
    public void unsubscribe(Long idSubscriber, Long idPublisher) {
        Optional<Subscription> subscription = subscriptionRepository.findById(idSubscriber);
        if (subscription.isPresent()) {
            subscription.get().getSubscriptions().remove(idPublisher);
            return;
        }
    }

    @Transactional(readOnly = true)
    public List<UserInfo> getSubscriptionsUsersInfo(Long idSubscriber) {
        Optional<Subscription> subscription = getSubscription(idSubscriber);
        if (subscription.isEmpty()) {
            return List.of();
        } else {
            return userInfoRepository.findByIdsFromCollection(subscription.get().getSubscriptions());
        }
    }

    @Transactional(readOnly = true)
    public Optional<Subscription> getSubscription(Long idSubscriber) {
        return subscriptionRepository.findById(idSubscriber);
    }
}
