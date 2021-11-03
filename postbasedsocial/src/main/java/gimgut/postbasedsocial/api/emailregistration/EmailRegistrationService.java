package gimgut.postbasedsocial.api.emailregistration;

import gimgut.postbasedsocial.api.user.Role;
import gimgut.postbasedsocial.api.user.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailRegistrationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsEmailRepository userCredentialsEmailRepository;
    private final UserInfoRepository userInfoRepository;
    private final TimeService timeService;

    public EmailRegistrationService(RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder,
                                    UserCredentialsEmailRepository userCredentialsEmailRepository,
                                    UserInfoRepository userInfoRepository,
                                    TimeService timeService) {
        this.userCredentialsEmailRepository = userCredentialsEmailRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.timeService = timeService;
    }

    @Transactional
    public RegistrationResponseStatus registerNewUser(UserCredentialsEmailRegistration userCredentials) {

        if (userCredentialsEmailRepository.findByEmail(userCredentials.getEmail()) != null) {
            return RegistrationResponseStatus.EMAIL_EXISTS;
        }

        if (userInfoRepository.findByUsername(userCredentials.getUserInfo().getUsername()) != null) {
            return RegistrationResponseStatus.USERNAME_EXISTS;
        }

        userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));

        UserInfo userInfo = userCredentials.getUserInfo();
        Role role = roleRepository.findByName(Roles.READER.name());
        if (role == null) {
            return RegistrationResponseStatus.FAILED;
        }
        userInfo.setRole(role);
        userInfo.setUnlocked(true);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowLDT());

        try {
            userInfoRepository.save(userInfo);
            userCredentialsEmailRepository.save(userCredentials);
            return RegistrationResponseStatus.SUCCESS;
        } catch (Exception e) {
            return RegistrationResponseStatus.FAILED;
        }
    }
}