package gimgut.postbasedsocial.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.authentication.EmailPasswordAuthenticationFilter;
import gimgut.postbasedsocial.security.authorization.JwtAuthorizationFilter;
import gimgut.postbasedsocial.security.oauth2.GoogleRegistrationService;
import gimgut.postbasedsocial.security.oauth2.HollowOauth2AuthorizedClientService;
import gimgut.postbasedsocial.security.oauth2.InMemoryRequestRepository;
import gimgut.postbasedsocial.security.oauth2.Oauth2AuthenticationSuccess;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger log = LogManager.getLogger(this.getClass());
    private final ObjectMapper mapper;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final UserInfoMapper userInfoMapper;
    private final Validator validator;
    private final GoogleRegistrationService googleRegistrationService;

    //TODO: move to configuration file
    private final String AUTH_LOGIN = "/api/v1/auth/signin";
    private final String AUTH_REGISTER = "/api/v1/auth/signup";
    private final String AUTH_REFRESH_TOKEN = "/api/v1/auth/refresh_token";

    public AppSecurityConfig(ObjectMapper mapper,
                             UserDetailsService userDetailsService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             JwtService jwtService,
                             UserInfoMapper userInfoMapper,
                             Validator validator,
                             GoogleRegistrationService googleRegistrationService) {
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
        this.validator = validator;
        this.googleRegistrationService = googleRegistrationService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter =
                new EmailPasswordAuthenticationFilter(
                        authenticationManagerBean(),
                        mapper,
                        jwtService,
                        userInfoMapper,
                        validator);
        emailPasswordAuthenticationFilter.setFilterProcessesUrl(AUTH_LOGIN);

        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Public endpoints
        http.authorizeRequests().antMatchers(
                HttpMethod.GET,
                "/api/*/feed/recent/**",
                "/api/*/users/**",
                "/api/*/posts/**",
                "api/oauth2/**").permitAll();
        http.authorizeRequests().antMatchers(
                HttpMethod.POST,
                AUTH_LOGIN,
                AUTH_REGISTER,
                AUTH_REFRESH_TOKEN).permitAll();

        // Dev endpoints, full exposure to view
        http.authorizeRequests().antMatchers(
                HttpMethod.GET,
                "/api/docs/**",
                "/api/swagger-ui/**",
                "/api/actuator/**").permitAll();

        //authenticated endpoints
        http.authorizeRequests().antMatchers(
                HttpMethod.POST,
                "/api/*/post/create")
                .hasAnyAuthority(Roles.WRITER.name(), Roles.ADMIN.name());
        http.authorizeRequests().antMatchers("/api/**").authenticated();


        http.addFilter(emailPasswordAuthenticationFilter);
        http.addFilterBefore(
                new JwtAuthorizationFilter(AUTH_LOGIN, AUTH_REFRESH_TOKEN, jwtService),
                UsernamePasswordAuthenticationFilter.class);

        // Called after auth fail on ".authenticated()" matcher with no mapping
        // for this request url. f.e. /api/r (/api/r doesn't exist)
        // Or if authenticated but doesn't have a necessary authority
        // Default spring implementation: redirect to oauth login page
        http.exceptionHandling().authenticationEntryPoint(this::authenticationExceptionEntryPoint);

        http.oauth2Login()
                .authorizationEndpoint()
                // TODO: Resolve deprecated
                .authorizationRequestRepository(new InMemoryRequestRepository());
        http.oauth2Login()
                .authorizedClientService(new HollowOauth2AuthorizedClientService());
        http.oauth2Login()
                .successHandler(
                        new Oauth2AuthenticationSuccess(
                            mapper,
                            googleRegistrationService,
                            jwtService,
                            userInfoMapper));
        // Old: "ip:port/oauth2/authorization/google"
        // New: "ip:port/api/oauth2/authorization/google"
        http.oauth2Login().authorizationEndpoint().baseUri("/api/oauth2/authorization");
        // Old: "ip:port/login/oauth2/code/google"
        // New: "ip:port/api/oauth2/code/google
        http.oauth2Login().redirectionEndpoint().baseUri("/api/oauth2/code/*");
    }

    private void authenticationExceptionEntryPoint(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
