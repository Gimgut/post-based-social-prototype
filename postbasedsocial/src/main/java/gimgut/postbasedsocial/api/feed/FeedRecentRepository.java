package gimgut.postbasedsocial.api.feed;

import gimgut.postbasedsocial.api.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*
 * Assumed that higher id means newer post
 */
@Repository
public interface FeedRecentRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p LEFT JOIN FETCH p.author where p.id<:lastId and p.visible=true order by p.id desc")
    List<Post> findNewestPostsAfterId(Pageable pageable, long lastId);

    @Query("select p from Post p LEFT JOIN FETCH p.author where p.visible=true order by p.id desc")
    List<Post> findNewestPosts(Pageable pageable);


    @Query("select p from Post p LEFT JOIN FETCH p.author where p.id<:lastId and p.author.id=:authorId and p.visible=true order by p.id desc")
    List<Post> findNewestPostsOfUserAfterId(Pageable pageable, long lastId, long authorId);

    @Query("select p from Post p LEFT JOIN FETCH p.author where p.author.id=:authorId and p.visible=true order by p.id desc")
    List<Post> findNewestPostsOfUser(Pageable pageable, long authorId);
}