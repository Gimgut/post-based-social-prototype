package gimgut.postbasedsocial.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsMapper userDetailsMapper;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsMapper userDetailsMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsMapper = userDetailsMapper;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("Attempt authentication...");
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new UsernameNotFoundException("Bad request body");
        }
        logger.info("Attempting authentication for email: " + loginRequestDto.email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.info("Authentication is successful for user: " + authResult.getName());

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String role = userDetails.getAuthorities().stream().toList().get(0).toString();
        String access_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 600))
                .withIssuer(request.getRequestURI().toString())
                .withClaim("role", role)
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) //7 days expiry
                .withIssuer(request.getRequestURI().toString())
                .withClaim("role", role)
                .sign(algorithm);


        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);

        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),
                new LoginResponseDto(LoginResponseStatus.SUCCESS, userDetailsMapper.toUserDetailsDto(userDetails))
        );
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.info("Authentication unsuccessful");
        response.setStatus(200);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),
                new LoginResponseDto(LoginResponseStatus.FAILED, null)
        );
    }
}
