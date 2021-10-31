package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfo;

public class RefreshToken {
    private RefreshTokenStatus status;
    private String accessToken;
    private String refreshToken;
    private UserInfo userInfo;

    public RefreshToken() {
    }

    public RefreshToken(RefreshTokenStatus status) {
        this.status = status;
    }

    public RefreshToken(String accessToken, String refreshToken, UserInfo userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public RefreshTokenStatus getStatus() {
        return status;
    }

    public void setStatus(RefreshTokenStatus status) {
        this.status = status;
    }
}
