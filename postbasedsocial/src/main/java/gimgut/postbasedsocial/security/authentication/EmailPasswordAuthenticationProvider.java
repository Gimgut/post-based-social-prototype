package gimgut.postbasedsocial.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationService userAuthenticationService;

    public EmailPasswordAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = (User) userService.loadUserByUsername(username);

        if(user != null && (user.getUsername().equals(username) || user.getName().equals(username)))
        {
            if(!passwordEncoder.matches(password, user.getPassword()))
            {
                throw new BadCredentialsException("Wrong password");
            }

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        }
        else
            throw new BadCredentialsException("Username not found");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
*/