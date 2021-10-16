package gimgut.postbasedsocial.security.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.AuthenticationType;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.util.Pair;
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

public class JwtEmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;

    public JwtEmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, JwtService jwtService, UserInfoRepository userInfoRepository) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.userInfoRepository = userInfoRepository;
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

        Pair<String, String> tokens = jwtService.getAccessAndRefreshTokens(userDetails, AuthenticationType.EMAIL);

        response.setHeader("access_token", tokens.getFirst());
        response.setHeader("refresh_token", tokens.getSecond());

        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(),
                new LoginResponseDto(LoginResponseStatus.SUCCESS, userInfoRepository.findById(userDetails.getId()).orElse(null))
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
