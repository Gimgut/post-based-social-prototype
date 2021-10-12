package gimgut.postbasedsocial.security.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println("successful auth for " + authentication.getName() + "\n principal: " + authentication.getPrincipal() + "\n credentials: " + authentication.getCredentials());
        System.out.println("httpServletRequest.getRequestURI() : " + httpServletRequest.getRequestURI());
        System.out.println("httpServletRequest.getRequestURL() : " + httpServletRequest.getRequestURL());
        httpServletResponse.setStatus(302);
        httpServletResponse.setHeader("Location", "http://localhost:4200");
    }
}
