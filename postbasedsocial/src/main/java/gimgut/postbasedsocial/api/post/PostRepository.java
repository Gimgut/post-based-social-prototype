package gimgut.postbasedsocial.api.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAll();

    List<Post> findAllByOrderByDatetimeDesc();

    Slice<Post> findAllByOrderByDatetimeDesc(Pageable pageable);
}
