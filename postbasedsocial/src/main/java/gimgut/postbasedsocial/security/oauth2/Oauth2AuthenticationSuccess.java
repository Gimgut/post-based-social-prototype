package gimgut.postbasedsocial.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.postbasedsocial.security.authentication.LoginResponseDto;
import gimgut.postbasedsocial.security.authentication.LoginResponseStatus;
import gimgut.util.Pair;
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
        logger.info("successful auth for " + authentication.getName()
                + "\n principal: " + authentication.getPrincipal()
                + "\n credentials: " + authentication.getCredentials());
        logger.info("principal class: " +authentication.getPrincipal().getClass());

        //1. get email
        //2. if email is missing then httpservletresponse authentication error
        //3. check if email exists in the UserCredentialsGoogleRepositoty
        //4. if does not exist, then register new user. UserInfo.username = "Username"+(10000+id)
        //5. httpservletresponse return access token and refresh token

        DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        boolean failed = false;
        if (user.getEmail() != null) {
            UserCredentialsGoogleRegistration registeredUser = googleRegistrationService.getUserByEmailOrRegisterAsNew(user);
            if (registeredUser != null) {

                Pair<String, String> tokens = jwtService.getAccessAndRefreshTokens(registeredUser);

                //httpServletResponse.setHeader("access_token", tokens.getFirst());
                //httpServletResponse.setHeader("refresh_token", tokens.getSecond());

                httpServletResponse.setContentType("application/json");
                objectMapper.writeValue(httpServletResponse.getOutputStream(),
                        new LoginResponseDto(LoginResponseStatus.SUCCESS,
                                tokens.getFirst(), tokens.getSecond(),
                                userInfoMapper.toUserInfoDto(registeredUser.getUserInfo()))
                );
            } else {
                failed = true;
            }
        } else {
            failed = true;
        }

        if (failed) {
            httpServletResponse.setStatus(200);
            objectMapper.writeValue(httpServletResponse.getOutputStream(),
                    new LoginResponseDto(LoginResponseStatus.FAILED, null, null, null));
        }
    }
}
