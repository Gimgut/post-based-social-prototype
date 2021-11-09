package gimgut.postbasedsocial.security.authentication;

import gimgut.postbasedsocial.api.user.UserInfoDto;

public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserInfoDto userInfo;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String accessToken, String refreshToken, UserInfoDto userInfo) {
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

    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDto userInfo) {
        this.userInfo = userInfo;
    }
}
