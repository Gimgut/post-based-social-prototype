package gimgut.postbasedsocial.security.oauth2;

import gimgut.postbasedsocial.api.user.Role;
import gimgut.postbasedsocial.api.user.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.services.TimeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoogleRegistrationService {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final UserInfoRepository userInfoRepository;
    private final UserCredentialsGoogleRepository userCredentialsGoogleRepository;
    private final RoleRepository roleRepository;
    private final TimeService timeService;

    public GoogleRegistrationService(UserInfoRepository userInfoRepository,
                                     UserCredentialsGoogleRepository userCredentialsGoogleRepository,
                                     RoleRepository roleRepository,
                                     TimeService timeService) {
        this.userInfoRepository = userInfoRepository;
        this.userCredentialsGoogleRepository = userCredentialsGoogleRepository;
        this.roleRepository = roleRepository;
        this.timeService = timeService;
    }

    @Transactional
    public UserCredentialsGoogleRegistration getUserByEmailOrRegisterAsNew(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration user = userCredentialsGoogleRepository.findByEmail_Eager(oidcUser.getEmail());
        if (user != null) {
            return user;
        } else {
            //create google user credentials
            UserCredentialsGoogleRegistration newGoogleUser = new UserCredentialsGoogleRegistration();
            newGoogleUser.setEmail(oidcUser.getEmail());
            newGoogleUser.setPassword(Integer.toString(("g"+oidcUser.getEmail()).hashCode()));

            //create user info
            UserInfo userInfo = new UserInfo();
            Role role = roleRepository.findByName(Roles.USER.name());
            if (role == null)
                return null;
            userInfo.setRole(role);
            userInfo.setLocked(false);
            userInfo.setActivated(true);
            userInfo.setRegistrationTime(timeService.getUtcNowLDT());
            userInfo.setPicture(oidcUser.getPicture());
            String generatedUsername;
            do {
                generatedUsername =
                        "User"
                        + Math.abs(("g" + oidcUser.getEmail()).hashCode())
                        + ("g" + oidcUser.getName()).hashCode();
            } while (userInfoRepository.findIdByUsername(generatedUsername) != null);
            userInfo.setUsername(generatedUsername);

            newGoogleUser.setUserInfo(userInfo);

            //save
            try {
                userInfoRepository.save(userInfo);
                userCredentialsGoogleRepository.save(newGoogleUser);
            } catch (Exception e) {
                logger.error("Failed userInfoRepository.save(userInfo)");
                return null;
            }
            return newGoogleUser;
        }
    }

    /*
    @Transactional
    public RegistrationResponseStatus registerNewUser(UserCredentialsEmailRegistration user) {
        //TODO: validate userCredentials fields

        if (userCredentialsGoogleRepository.findByEmail(user.getEmail()) != null)
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

     */
}
