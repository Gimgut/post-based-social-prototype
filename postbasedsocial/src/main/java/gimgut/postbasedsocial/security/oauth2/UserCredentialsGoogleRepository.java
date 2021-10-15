package gimgut.postbasedsocial.security.oauth2;

import gimgut.postbasedsocial.security.SecuredUser;
import gimgut.postbasedsocial.security.oauth2.UserCredentialsGoogleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsGoogleRepository extends JpaRepository<UserCredentialsGoogleRegistration, Long> {

    UserCredentialsGoogleRegistration findByEmail(String email);

    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.email=:email")
    UserCredentialsGoogleRegistration findByEmail_Eager(String email);

    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.userInfo=:uiid")
    UserCredentialsGoogleRegistration findByUserInfoId_Eager(Long uiid);
}
