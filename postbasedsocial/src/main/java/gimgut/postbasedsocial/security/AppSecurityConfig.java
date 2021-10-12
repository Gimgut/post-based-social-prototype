package gimgut.postbasedsocial.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.security.authentication.JwtAuthenticationFilter;
import gimgut.postbasedsocial.security.oauth2.AuthenticationSuccess;
import gimgut.postbasedsocial.security.authorization.JwtAuthorizationFilter;
import gimgut.postbasedsocial.security.authentication.UserDetailsMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final ObjectMapper mapper;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsMapper userDetailsMapper;

    private final String AUTH_LOGIN = "/api/auth/signin";
    private final String AUTH_REGISTER = "/api/auth/signup";

    public AppSecurityConfig(ObjectMapper mapper, UserDetailsService userDetailsService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             UserDetailsMapper userDetailsMapper) {
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsMapper = userDetailsMapper;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        //auth.authenticationProvider(emailPasswordAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManagerBean(),
                userDetailsMapper);
        jwtAuthenticationFilter.setFilterProcessesUrl(AUTH_LOGIN);

        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //public endpoints
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/feed/**" , "/api/user/**", "/api/post/**", "/login/oauth2/**", "/oauth2/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, AUTH_LOGIN, AUTH_REGISTER).permitAll();
        //authenticated endpoints
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/post/create").hasAnyAuthority(Roles.WRITER.name(), Roles.ADMIN.name());
        http.authorizeRequests().antMatchers("/api/**").authenticated();
        http.authorizeRequests().antMatchers("/**").permitAll();

        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(new JwtAuthorizationFilter(AUTH_LOGIN), UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login().successHandler(new AuthenticationSuccess()).and().exceptionHandling().authenticationEntryPoint(this::authenticationEntryPoint);
        http.oauth2Login().authorizationEndpoint().authorizationRequestRepository(new InMemoryRequestRepository());
        //http.oauth2Login().loginPage("http://localhost:4200/auth").successHandler(new Oauth2AuthenticationSuccess());
        //http.oauth2Client().disable();
        //http.oauth2Login().disable();

    }

    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response,
                                          AuthenticationException authException ) throws IOException {
        logger.info("authenticationEntryPoint");
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.getWriter().write( mapper.writeValueAsString( Collections.singletonMap( "error", "Unauthenticated" ) ) );
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
        //config.setAllowCredentials(true);
        //config.addAllowedOrigin("http://localhost:4200");
        //config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
