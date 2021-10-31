package gimgut.postbasedsocial.security.authorization;

import com.auth0.jwt.interfaces.DecodedJWT;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.postbasedsocial.security.Roles;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTH_LOGIN_URL;
    private final String REFRESH_TOKEN_URL;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(String auth_login_url, String refresh_token_url, JwtService jwtService) {
        this.AUTH_LOGIN_URL = auth_login_url;
        this.REFRESH_TOKEN_URL = refresh_token_url;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Enter JwtAuthorizationFilter...");
        String servletPath = request.getServletPath();
        if (servletPath.equals(AUTH_LOGIN_URL) || servletPath.equals(REFRESH_TOKEN_URL)) {
            logger.info("Skipping JwtAuthorizationFilter because path is " + AUTH_LOGIN_URL + "or" + REFRESH_TOKEN_URL);
            filterChain.doFilter(request, response);
        } else {
            logger.info("Try authorization...");
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String accessToken = authorizationHeader.substring(7);
                    DecodedJWT decodedJWT = jwtService.verifyAccessToken(accessToken);

                    Long userInfoId = decodedJWT.getClaim("uiid").asLong();
                    String userRole = decodedJWT.getClaim("role").asString();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userInfoId, null, Arrays.asList(Roles.valueOf(userRole)));

                    //Suggestion from spring security doc because of race condition with the following implementation: SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(usernamePasswordAuthenticationToken);
                    SecurityContextHolder.setContext(context);
                    logger.info("Authorization is successful for user uiid: " + userInfoId + " with role: " + userRole);

                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    logger.info("Authorization failed");
                    response.setHeader("error", "Authorization failed");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                logger.info("Unauthorized access");
                filterChain.doFilter(request, response);
            }
        }
    }
}
