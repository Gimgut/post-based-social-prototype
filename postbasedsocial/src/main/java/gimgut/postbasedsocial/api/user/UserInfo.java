package gimgut.postbasedsocial.api.user;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserInfo {

    @Id
    @SequenceGenerator(name = "user_info_sequence", sequenceName = "user_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_info_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean activated;

    private boolean unlocked;

    @Column(name = "registration_dt")
    private LocalDateTime registrationTime;

    //TODO: shorten url before saving / use cdn / use cloud storage / etc.
    @Column(name = "picture_url")
    private String pictureUrl;

    public UserInfo() {
    }

    public UserInfo(Long id, String username, Role role, boolean activated, boolean unlocked, LocalDateTime registrationTime, String pictureUrl) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.activated = activated;
        this.unlocked = unlocked;
        this.registrationTime = registrationTime;
        this.pictureUrl = pictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", activated=" + activated +
                ", unlocked=" + unlocked +
                ", registrationTime=" + registrationTime +
                '}';
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
