package gimgut.postbasedsocial.security.oauth2;

import gimgut.postbasedsocial.api.user.Role;
import gimgut.postbasedsocial.api.user.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class GoogleRegistrationService {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final UserInfoRepository userInfoRepository;
    private final UserCredentialsGoogleRepository userCredentialsGoogleRepository;
    private final RoleRepository roleRepository;
    private final TimeService timeService;
    private final EntityManager entityManager;

    public GoogleRegistrationService(UserInfoRepository userInfoRepository,
                                     UserCredentialsGoogleRepository userCredentialsGoogleRepository,
                                     RoleRepository roleRepository,
                                     TimeService timeService,
                                     EntityManager entityManager) {
        this.userInfoRepository = userInfoRepository;
        this.userCredentialsGoogleRepository = userCredentialsGoogleRepository;
        this.roleRepository = roleRepository;
        this.timeService = timeService;
        this.entityManager = entityManager;
    }

    @Transactional
    public UserCredentialsGoogleRegistration getUserByEmailOrRegisterAsNew(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration user = userCredentialsGoogleRepository.findByEmail_Eager(oidcUser.getEmail());
        if (user != null) {
            return user;
        } else {
            return registerNewUser(oidcUser);
        }
    }

    @Transactional
    public UserCredentialsGoogleRegistration registerNewUser(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration newGoogleUser = this.createUserCredentials(oidcUser);
        UserInfo userInfo = this.createDefaultUserInfo(oidcUser, newGoogleUser);
        newGoogleUser.setUserInfo(userInfo);
        return newGoogleUser;
    }

    @Transactional
    private UserCredentialsGoogleRegistration createUserCredentials(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration newGoogleUser = new UserCredentialsGoogleRegistration();
        newGoogleUser.setEmail(oidcUser.getEmail());
        newGoogleUser.setPassword(this.generateRandomPassword(oidcUser));
        return userCredentialsGoogleRepository.save(newGoogleUser);
    }

    private String generateRandomPassword(DefaultOidcUser oidcUser) {
        return Integer.toString(("g"+oidcUser.getEmail()).hashCode());
    }

    @Transactional
    private UserInfo createDefaultUserInfo(DefaultOidcUser oidcUser, UserCredentialsGoogleRegistration userCredentials) {
        UserInfo userInfo = new UserInfo();
        String defaultRole = Roles.USER.name();
        Role role = roleRepository.findByName(defaultRole);
        if (role == null) {
            throw new GoogleRegistrationException("Role " + defaultRole + " was not found in the database");
        }
        userInfo.setRole(role);
        userInfo.setUnlocked(true);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowLDT());
        userInfo.setPictureUrl(oidcUser.getPicture());
        userInfo.setUsername("User"+userCredentials.getId()+"g");
        return userInfoRepository.save(userInfo);
    }
}
