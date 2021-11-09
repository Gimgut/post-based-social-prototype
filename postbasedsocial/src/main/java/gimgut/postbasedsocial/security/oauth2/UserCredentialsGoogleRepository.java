package gimgut.postbasedsocial.security.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsGoogleRepository extends JpaRepository<UserCredentialsGoogleRegistration, Long> {

    UserCredentialsGoogleRegistration findByEmail(String email);

    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.email=:email")
    UserCredentialsGoogleRegistration findByEmail_JoinFetchInfo(String email);

    @Query("SELECT u FROM UserCredentialsGoogle u JOIN FETCH u.userInfo WHERE u.userInfo.id=:uiid")
    UserCredentialsGoogleRegistration findByUserInfoId_JoinFetchInfo(Long uiid);
}
