package gimgut.postbasedsocial.security.authentication;

import gimgut.postbasedsocial.api.user.UserInfo;

public class LoginResponseDto {
    private LoginResponseStatus status;
    private UserInfo userInfo;

    public LoginResponseDto(LoginResponseStatus status, UserInfo userInfo) {
        this.status = status;
        this.userInfo = userInfo;
    }

    public LoginResponseStatus getStatus() {
        return status;
    }

    public void setStatus(LoginResponseStatus status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
