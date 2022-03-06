package gimgut.postbasedsocial.api.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class UserInfoDto {
    private Long id;
    private String username;
    private String role;
    private boolean activated;
    private boolean unlocked;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss Z")
    private ZonedDateTime registrationTime;
    private String pictureUrl;

    public UserInfoDto() {
    }

    public UserInfoDto(Long id, String username, String role, boolean activated, boolean unlocked, ZonedDateTime registrationTime, String pictureUrl) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    public ZonedDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(ZonedDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
