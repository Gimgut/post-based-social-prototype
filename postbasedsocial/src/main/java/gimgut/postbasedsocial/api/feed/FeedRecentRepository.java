package gimgut.postbasedsocial.api.feed;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface FeedRecentRepository extends JpaRepository<Post, Long> {

    /**
     * Assumed that higher id means newer post
     *
     * @param lastId id of the last post in the shown feed
     * @param limit how many posts to be returned
     * @return
     */
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM post p WHERE p.id<?1 ORDER BY p.id DESC LIMIT ?2", nativeQuery = true)
    List<Post> findNewestPostsAfterId(long lastId, long limit);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM post p ORDER BY p.id DESC LIMIT ?1", nativeQuery = true)
    List<Post> findNewestPosts(long limit);
}