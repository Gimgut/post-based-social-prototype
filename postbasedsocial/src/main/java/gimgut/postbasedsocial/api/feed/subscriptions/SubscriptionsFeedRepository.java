package gimgut.postbasedsocial.api.feed.subscriptions;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubscriptionsFeedRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p LEFT JOIN FETCH p.author where p.id<:lastId and p.author.id IN :authorsIds and p.visible=true order by p.id desc")
    List<Post> findNewestPostsOfUsersAfterId(Pageable pageable, long lastId, Collection authorsIds);

    @Query("select p from Post p LEFT JOIN FETCH p.author where p.author.id IN :authorsIds and p.visible=true order by p.id desc")
    List<Post> findNewestPostsOfUsers(Pageable pageable, Collection authorsIds);

}
