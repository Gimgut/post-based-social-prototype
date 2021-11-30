package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.shared.jpa.SoftJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface PostRepository extends SoftJpaRepository<Post, Long> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author WHERE p.id = :id AND p.visible = true")
    Optional<Post> findVisibleById_JoinFetchAuthor(Long id);


    //List<Post> findAllByOrderByCreatedAtDesc();
}
