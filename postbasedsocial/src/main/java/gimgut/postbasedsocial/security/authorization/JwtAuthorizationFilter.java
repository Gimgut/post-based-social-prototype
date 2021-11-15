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
        String forwardedIp = request.getHeader("X-FORWARDED-FOR");
        String userRequestInfo =
                "url: "
                + request.getRequestURL()
                + " addr: " + (forwardedIp == null ? request.getRemoteAddr() : forwardedIp);
        if (!request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            response.setStatus(403);
            logger.info("KICKED OUT." + userRequestInfo);
            return;
        }
        String servletPath = request.getServletPath();
        if (!servletPath.startsWith("/api")
                || servletPath.startsWith(AUTH_LOGIN_URL)
                || servletPath.startsWith(REFRESH_TOKEN_URL)) {
            logger.info("Skip autho." + userRequestInfo);
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.info("Unautho access." + userRequestInfo);
            filterChain.doFilter(request, response);
            return;
        }

        DecodedJWT decodedJWT;
        try {
            String accessToken = authorizationHeader.substring(7);
            decodedJWT = jwtService.verifyAccessToken(accessToken);
        } catch (Exception e) {
            logger.info("Autho failed." + userRequestInfo);
            response.setHeader("error", "Autho failed");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Long userInfoId = decodedJWT.getClaim("uiid").asLong();
        String userRole = decodedJWT.getClaim("role").asString();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userInfoId,
                        null,
                        Arrays.asList(Roles.valueOf(userRole)));

        // Suggestion from spring security doc because of
        // a race condition with the following implementation:
        // SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(context);
        logger.info("Autho success." + " UIID: " + userInfoId + "." + userRequestInfo );
        filterChain.doFilter(request, response);
    }
}
