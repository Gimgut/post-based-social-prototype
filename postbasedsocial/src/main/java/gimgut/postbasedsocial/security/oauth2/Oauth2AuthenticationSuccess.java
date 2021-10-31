package gimgut.postbasedsocial.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoDto;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.postbasedsocial.security.Tokens;
import gimgut.postbasedsocial.security.authentication.LoginResponseDto;
import gimgut.postbasedsocial.security.authentication.LoginResponseStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Oauth2AuthenticationSuccess implements AuthenticationSuccessHandler {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final ObjectMapper objectMapper;
    private final GoogleRegistrationService googleRegistrationService;
    private final JwtService jwtService;
    private final UserInfoMapper userInfoMapper;

    public Oauth2AuthenticationSuccess(ObjectMapper objectMapper, GoogleRegistrationService googleRegistrationService, JwtService jwtService, UserInfoMapper userInfoMapper) {
        this.objectMapper = objectMapper;
        this.googleRegistrationService = googleRegistrationService;
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        if (oidcUser.getEmail() == null) {
            httpServletResponse.setStatus(400);
            httpServletResponse.getOutputStream().write("NO_EMAIL_PROVIDED".getBytes());
            return;
        }
        UserCredentialsGoogleRegistration registeredUser = googleRegistrationService.getUserByEmailOrRegisterAsNew(oidcUser);
        Tokens tokens = jwtService.getAccessAndRefreshTokens(registeredUser);
        UserInfoDto userInfoDto = userInfoMapper.toUserInfoDto(registeredUser.getUserInfo());
        LoginResponseDto loginResponse = new LoginResponseDto(
                LoginResponseStatus.SUCCESS,
                tokens.getAccessToken(),
                tokens.getRefreshToken(),
                userInfoDto);

        httpServletResponse.setContentType("application/json");
        objectMapper.writeValue(httpServletResponse.getOutputStream(), loginResponse);
    }
}
