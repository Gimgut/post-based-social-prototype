package gimgut.postbasedsocial.api.emailregistration;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.security.SecuredUser;

import javax.persistence.*;

@Entity(name = "UserCredentialsEmail")
public class UserCredentialsEmailRegistration implements SecuredUser {

    @Id
    @SequenceGenerator(name = "user_cred_email_reg_sequence", sequenceName = "user_cred_email_reg_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_cred_email_reg_sequence")
    @Column(name = "id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    public UserCredentialsEmailRegistration() {
    }

    public UserCredentialsEmailRegistration(Long id, String email, String password, UserInfo userInfo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userInfo = userInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserCredentialsEmailRegistration{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
