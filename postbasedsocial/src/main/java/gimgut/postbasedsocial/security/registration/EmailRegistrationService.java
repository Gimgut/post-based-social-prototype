package gimgut.postbasedsocial.security.registration;

import gimgut.postbasedsocial.api.user.Role;
import gimgut.postbasedsocial.api.user.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.services.TimeService;
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

    public EmailRegistrationService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserCredentialsEmailRepository userCredentialsEmailRepository, UserInfoRepository userInfoRepository, TimeService timeService) {
        this.userCredentialsEmailRepository = userCredentialsEmailRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.timeService = timeService;
    }

    @Transactional
    public RegistrationResponseStatus registerNewUser(UserCredentialsEmailRegistration user) {
        //TODO: validate userCredentials fields

        if (userCredentialsEmailRepository.findByEmail(user.getEmail()) != null)
            return RegistrationResponseStatus.EMAIL_EXISTS;

        if (userInfoRepository.findByUsername(user.getUserInfo().getUsername()) != null)
            return RegistrationResponseStatus.USERNAME_EXISTS;

        //set UserCredentialsEmailRegistration parameters
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set UserInfo parameters
        UserInfo userInfo = user.getUserInfo();
        Role role = roleRepository.findByName(Roles.USER.name());
        if (role == null)
            return RegistrationResponseStatus.FAILED;
        userInfo.setRole(role);
        userInfo.setLocked(false);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowLDT());

        try {
            userInfoRepository.save(userInfo);
            userCredentialsEmailRepository.save(user);
        } catch (Exception e) {
            return RegistrationResponseStatus.FAILED;
        }
        return RegistrationResponseStatus.SUCCESS;
    }
}