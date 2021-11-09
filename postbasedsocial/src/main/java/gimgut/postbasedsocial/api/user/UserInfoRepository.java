package gimgut.postbasedsocial.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUsername(String username);

    Long findIdByUsername(String username);

    @Query("SELECT u FROM UserInfo u WHERE u.id IN :ids")
    List<UserInfo> findByIdsFromCollection(Collection ids);
}

