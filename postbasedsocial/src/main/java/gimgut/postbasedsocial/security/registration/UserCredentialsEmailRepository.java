package gimgut.postbasedsocial.security.registration;

import gimgut.postbasedsocial.security.registration.UserCredentialsEmailRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsEmailRepository extends JpaRepository<UserCredentialsEmailRegistration, Long> {

    /**
     * @param email @Column(unique = true)
     * @return
     */
    UserCredentialsEmailRegistration findByEmail(String email);

    @Query("SELECT u FROM UserCredentialsEmail u JOIN FETCH u.userInfo WHERE u.email=:email")
    UserCredentialsEmailRegistration findByEmail_Eager(String email);
}
