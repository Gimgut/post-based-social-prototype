package gimgut.postbasedsocial.api.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserInfoDto {
    private Long id;
    private String username;
    private String role;
    private boolean activated;
    private boolean locked;
    private LocalDateTime registrationTime;
    private String picture;

    public UserInfoDto() {
    }

    public UserInfoDto(Long id, String username, String role, boolean activated, boolean locked, LocalDateTime registrationTime, String picture) {
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
