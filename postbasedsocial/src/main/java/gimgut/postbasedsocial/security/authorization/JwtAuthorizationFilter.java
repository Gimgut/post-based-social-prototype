package gimgut.postbasedsocial.security.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTH_LOGIN;

    public JwtAuthorizationFilter(String auth_login) {
        AUTH_LOGIN = auth_login;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Enter JwtAuthorizationFilter...");
        if (request.getServletPath().equals(AUTH_LOGIN)) {
            logger.info("Skipping JwtAuthorizationFilter because path is " + AUTH_LOGIN);
            filterChain.doFilter(request, response);
        } else {
            logger.info("Try authorization...");
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring(7);
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String username = decodedJWT.getSubject();
                    String role = decodedJWT.getClaim("role").asString();
                    Collection<SimpleGrantedAuthority> authorityCollection = new ArrayList<>();
                    authorityCollection.add(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorityCollection);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(usernamePasswordAuthenticationToken);
                    SecurityContextHolder.setContext(context);
                    //SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    logger.info("Authorization is successful for user: " + username + " with role: " + role);

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
