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

    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean activated;

    private boolean locked;

    @Column(name = "dt_registration")
    private LocalDateTime registrationTime;

    /**
     * Contains picture url
     * TODO: add a service that shortens picture url
     */
    private String picture;

    public UserInfo() {
    }

    public UserInfo(Long id, String username, Role role, boolean activated, boolean locked, LocalDateTime registrationTime, String picture) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.activated = activated;
        this.locked = locked;
        this.registrationTime = registrationTime;
        this.picture = picture;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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
                ", locked=" + locked +
                ", registrationTime=" + registrationTime +
                '}';
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
