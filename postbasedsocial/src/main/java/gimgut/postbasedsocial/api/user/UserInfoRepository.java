package gimgut.postbasedsocial.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    UserInfo findByUsername(String username);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("SELECT u FROM UserInfo u WHERE u.id IN :ids")
    List<UserInfo> findByIdsFromCollection(Collection ids);

    @Override
    UserInfo save(UserInfo entity);
}

