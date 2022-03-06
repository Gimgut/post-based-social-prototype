package gimgut.postbasedsocial.security.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoDto;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.AuthenticationType;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.postbasedsocial.security.Tokens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LogManager.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final UserInfoMapper userInfoMapper;
    private final Validator validator;

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                             ObjectMapper objectMapper,
                                             JwtService jwtService,
                                             UserInfoMapper userInfoMapper, Validator validator) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String forwardedIp = request.getHeader("X-FORWARDED-FOR");
        String userRequestInfo = "addr: " + (forwardedIp == null ? request.getRemoteAddr() : forwardedIp);

        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.warn("Authe failed. Can't parse JSON to LoginRequestDto." + userRequestInfo);
            throw new UsernameNotFoundException("Bad request body");
        }

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(loginRequestDto);
        if (!violations.isEmpty()) {
            log.warn("Authe failed. Violated fields." + userRequestInfo);
            throw new UsernameNotFoundException("Bad request body");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String forwardedIp = request.getHeader("X-FORWARDED-FOR");
        String userRequestInfo = "addr: " + (forwardedIp == null ? request.getRemoteAddr() : forwardedIp);

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        log.info("Authe success. UIID: " + userDetails.getId() + "." + userRequestInfo);

        Tokens tokens = jwtService.getAccessAndRefreshTokens(userDetails, AuthenticationType.EMAIL);

        UserInfoDto responseUserInfo = userInfoMapper.toDto(userDetails.getUserInfo());
        LoginResponseDto loginResponse = new LoginResponseDto(
                tokens.getAccessToken(),
                tokens.getRefreshToken(),
                responseUserInfo);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String forwardedIp = request.getHeader("X-FORWARDED-FOR");
        String userRequestInfo = "addr: " + (forwardedIp == null ? request.getRemoteAddr() : forwardedIp);
        log.info("Authe failed. Wrong credentials." + userRequestInfo);

        response.setStatus(400);
        response.getOutputStream().write("WRONG_CREDENTIALS".getBytes());
    }
}
