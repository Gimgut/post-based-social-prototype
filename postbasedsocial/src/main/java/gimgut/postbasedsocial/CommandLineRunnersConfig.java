package gimgut.postbasedsocial;

import gimgut.postbasedsocial.api.emailregistration.UserCredentialsEmailRegistration;
import gimgut.postbasedsocial.api.emailregistration.UserCredentialsEmailRepository;
import gimgut.postbasedsocial.api.post.Post;
import gimgut.postbasedsocial.api.post.PostRepository;
import gimgut.postbasedsocial.api.post.RandomPostService;
import gimgut.postbasedsocial.api.user.role.Role;
import gimgut.postbasedsocial.api.user.role.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.security.Roles;
import gimgut.postbasedsocial.shared.services.TimeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Profile("DbRandomPopulation")
public class CommandLineRunnersConfig {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Bean("createAppRoles")
    public CommandLineRunner createAppRoles(RoleRepository roleRepository) {
        return args -> {
            Role role = new Role();
            role.setName(Roles.GUEST.name());
            roleRepository.save(role);

            role = new Role();
            role.setName(Roles.READER.name());
            roleRepository.save(role);

            role = new Role();
            role.setName(Roles.WRITER.name());
            roleRepository.save(role);

            role = new Role();
            role.setName(Roles.ADMIN.name());
            roleRepository.save(role);

            //roleRepository.findAll().forEach((x) -> logger.info(x.toString()));
        };
    }

    @Bean
    public CommandLineRunner run(RandomPostService randomPublicationService, PostRepository publicationRepository) {
        return args -> {
            //randomPublicationService.addRandomPublications(100);
            randomPublicationService.add1984(100);
            List<Post> allPublications = publicationRepository.findAll();
            allPublications.forEach((x) -> logger.info(x.toString()));
            //allPublications = publicationRepository.findAllByOrderByCreatedAtDesc();
            //allPublications.forEach((x) -> logger.info(x.toString()));
        };
    }

    @Bean
    public CommandLineRunner createDefaultUsers(UserInfoRepository userInfoRepository,
                                                UserCredentialsEmailRepository userCredentialsEmailRepository,
                                                PasswordEncoder passwordEncoder,
                                                RoleRepository roleRepository,
                                                TimeService timeService) {
        return args -> {

            UserCredentialsEmailRegistration user = new UserCredentialsEmailRegistration();
            user.setEmail("admin@admin");
            user.setPassword("admin");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            UserInfo userInfo = new UserInfo();
            userInfo.setUsername("admin");
            userInfo.setActivated(true);
            userInfo.setUnlocked(true);
            Role role = roleRepository.findByName(Roles.ADMIN.name());
            userInfo.setRole(role);
            userInfo.setRegistrationTime(timeService.getUtcNowZDT());
            userInfoRepository.save(userInfo);

            user.setUserInfo(userInfo);
            userCredentialsEmailRepository.save(user);

            logger.info("Creates admin user " + user);
        };
    }
}

