package gimgut.postbasedsocial.api.emailregistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface UserCredentialsEmailRepository extends JpaRepository<UserCredentialsEmailRegistration, Long> {

    /** Email is unique in the DB table */
    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHE_MODE, value = "GET")
    })
    UserCredentialsEmailRegistration findByEmail(String email);


    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHE_MODE, value = "GET")
    })
    @Query("SELECT u FROM UserCredentialsEmail u JOIN FETCH u.userInfo WHERE u.email=:email")
    UserCredentialsEmailRegistration findByEmail_Eager(String email);


    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.CACHE_MODE, value = "GET")
    })
    @Query("SELECT u FROM UserCredentialsEmail u JOIN FETCH u.userInfo WHERE u.userInfo.id=:uiid")
    UserCredentialsEmailRegistration findByUserInfoId_Eager(Long uiid);
}
