package gimgut.postbasedsocial.security.oauth2;

import gimgut.postbasedsocial.api.user.role.Role;
import gimgut.postbasedsocial.api.user.role.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.security.SecureRandom;

@Service
public class GoogleRegistrationService {

    private final Logger log = LogManager.getLogger(this.getClass());
    private final UserInfoRepository userInfoRepository;
    private final UserCredentialsGoogleRepository userCredentialsGoogleRepository;
    private final RoleRepository roleRepository;
    private final TimeService timeService;
    private final EntityManager entityManager;
    private final SecureRandom random = new SecureRandom();

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
        UserCredentialsGoogleRegistration user = userCredentialsGoogleRepository.findByEmail_JoinFetchInfo(oidcUser.getEmail());
        if (user != null) {
            return user;
        } else {
            return registerNewUser(oidcUser);
        }
    }

    @Transactional
    public UserCredentialsGoogleRegistration registerNewUser(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration newGoogleUser = this.createUserCredentials(oidcUser);
        UserInfo userInfo = createDefaultUserInfo(oidcUser, newGoogleUser);
        newGoogleUser.setUserInfo(userInfo);
        return newGoogleUser;
    }

    @Transactional
    private UserCredentialsGoogleRegistration createUserCredentials(DefaultOidcUser oidcUser) {
        UserCredentialsGoogleRegistration newGoogleUser = new UserCredentialsGoogleRegistration();
        newGoogleUser.setEmail(oidcUser.getEmail());
        newGoogleUser.setPassword(generateRandomPassword());
        return userCredentialsGoogleRepository.save(newGoogleUser);
    }

    private String generateRandomPassword() {
        byte[] password = new byte[16];
        random.nextBytes(password);
        return String.valueOf(password);
    }

    @Transactional
    private UserInfo createDefaultUserInfo(DefaultOidcUser oidcUser, UserCredentialsGoogleRegistration userCredentials) {
        UserInfo userInfo = new UserInfo();
        String defaultRole = Roles.READER.name();
        Role role = roleRepository.findByName(defaultRole);
        if (role == null) {
            throw new GoogleRegistrationException("Role " + defaultRole + " was not found in the database");
        }
        userInfo.setRole(role);
        userInfo.setUnlocked(true);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowZDT());
        //TODO: load picture to some service to avoid google's "Rate-limit exceeded" error
        userInfo.setPictureUrl(oidcUser.getPicture());
        userInfo.setUsername("User" + userCredentials.getId() + "g");
        return userInfoRepository.save(userInfo);
    }
}
