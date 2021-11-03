package gimgut.postbasedsocial.security.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoDto;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.AuthenticationType;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.postbasedsocial.security.Tokens;
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
    private final UserInfoMapper userInfoMapper;

    public JwtEmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                ObjectMapper objectMapper,
                                                JwtService jwtService,
                                                UserInfoMapper userInfoMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
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
        logger.info("Attempting authentication for email: " + loginRequestDto.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.info("Authentication is successful for user: " + authResult.getName());

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        Tokens tokens = jwtService.getAccessAndRefreshTokens(userDetails, AuthenticationType.EMAIL);

        UserInfoDto responseUserInfo = userInfoMapper.toDto(userDetails.getUserInfo());
        LoginResponseDto loginResponse = new LoginResponseDto(
                LoginResponseStatus.SUCCESS,
                tokens.getAccessToken(),
                tokens.getRefreshToken(),
                responseUserInfo);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.info("Authentication unsuccessful");
        response.setStatus(200);
        response.setContentType("application/json");
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setStatus(LoginResponseStatus.FAILED);
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }
}
