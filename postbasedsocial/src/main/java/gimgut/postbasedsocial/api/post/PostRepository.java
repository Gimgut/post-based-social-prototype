package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.shared.jpa.SoftJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends SoftJpaRepository<Post, Long> {



    //List<Post> findAllByOrderByCreatedAtDesc();
}
