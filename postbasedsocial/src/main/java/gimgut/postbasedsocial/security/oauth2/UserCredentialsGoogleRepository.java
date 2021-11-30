package gimgut.postbasedsocial.security.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface UserCredentialsGoogleRepository extends JpaRepository<UserCredentialsGoogleRegistration, Long> {


    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHE_MODE, value = "GET")
    })
    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.email=:email")
    UserCredentialsGoogleRegistration findByEmail_JoinFetchInfo(String email);

    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHE_MODE, value = "GET")
    })
    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.userInfo.id=:uiid")
    UserCredentialsGoogleRegistration findByUserInfoId_JoinFetchInfo(Long uiid);
}
