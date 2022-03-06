package gimgut.postbasedsocial.security.authentication;

import gimgut.postbasedsocial.api.emailregistration.UserCredentialsEmailRegistration;
import gimgut.postbasedsocial.api.emailregistration.UserCredentialsEmailRepository;
import gimgut.postbasedsocial.security.Roles;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsEmailRepository userCredentialsEmailRepository;

    public UserDetailsServiceImpl(UserCredentialsEmailRepository userCredentialsEmailRepository) {
        this.userCredentialsEmailRepository = userCredentialsEmailRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentialsEmailRegistration user = userCredentialsEmailRepository.findByEmail_Eager(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found:" + email);
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getId(),
                user.getUserInfo().getUsername(),
                user.getPassword(),
                true,
                user.getUserInfo().isUnlocked(),
                true,
                user.getUserInfo().isActivated(),
                Collections.singleton(Roles.valueOf(user.getUserInfo().getRole().getName())),
                user.getUserInfo()
        );
        return userDetails;
    }
}
