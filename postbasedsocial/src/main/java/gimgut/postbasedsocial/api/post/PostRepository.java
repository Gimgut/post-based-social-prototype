package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.shared.jpa.SoftJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends SoftJpaRepository<Post, Long> {


    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author WHERE p.id=?1 AND p.visible=true")
    Optional<Post> findByIdVisible(Long id);


    //List<Post> findAllByOrderByCreatedAtDesc();
}
