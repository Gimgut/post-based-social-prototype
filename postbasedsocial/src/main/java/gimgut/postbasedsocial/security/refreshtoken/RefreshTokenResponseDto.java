package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoDto;

public class RefreshTokenResponseDto {
    private RefreshTokenStatus status;
    private UserInfoDto userInfo;
    private String accessToken;
    private String refreshToken;

    public RefreshTokenResponseDto(RefreshTokenStatus status) {
        this.status = status;
    }

    public RefreshTokenResponseDto(RefreshTokenStatus status, UserInfoDto userInfo, String accessToken, String refreshToken) {
        this.status = status;
        this.userInfo = userInfo;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public RefreshTokenStatus getStatus() {
        return status;
    }

    public void setStatus(RefreshTokenStatus status) {
        this.status = status;
    }

    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDto userInfo) {
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
}
