package gimgut.postbasedsocial.api.subscription;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

/**
 * Normalized UserInfo
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subscription {

    /** Essentially UserInfo.id */
    @Id
    private Long id;

    @Convert(converter = SetOfLongsConverter.class)
    private Set<Long> subscriptions;

    public Subscription() {
    }

    public Subscription(Long id, Set<Long> subscriptions) {
        this.id = id;
        this.subscriptions = subscriptions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Long> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
