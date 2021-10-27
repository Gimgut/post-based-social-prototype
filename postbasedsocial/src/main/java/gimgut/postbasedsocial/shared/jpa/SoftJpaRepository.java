package gimgut.postbasedsocial.shared.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@NoRepositoryBean
public interface SoftJpaRepository<T, ID> extends JpaRepository<T, ID> {

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.visible = true")
    List<T> findAllVisible();

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.visible = true")
    List<T> findVisibleById(Long id);


    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.visible=false where e.id = ?1")
    void softDelete(Long id);


}
