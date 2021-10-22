package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.services.TimeService;
import gimgut.postbasedsocial.services.generators.StringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Service
public class RandomPostService {

    private final EntityManager entityManager;
    private final StringGenerator stringGenerator;
    private final TimeService timeService;
    private final UserInfoRepository userInfoRepository;

    public RandomPostService(EntityManager entityManager, StringGenerator stringGenerator, TimeService timeService, UserInfoRepository userInfoRepository) {
        this.entityManager = entityManager;
        this.stringGenerator = stringGenerator;
        this.timeService = timeService;
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    public void addRandomPublications(int count) {
        //create random user info
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Random01");
        userInfo.setLocked(false);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowLDT());
        userInfoRepository.save(userInfo);
        for (int i = 0; i < count; i++) {
            LocalDateTime rndLdt = timeService.getUtcNowLDT().plusSeconds(stringGenerator.generateRandomInt(20*i,20*(i+1)));
            Post publication = new Post(
                    stringGenerator.generateSentence(1,4),
                    stringGenerator.generateParagraph(1,10),
                    i+1,
                    rndLdt,
                    userInfo);
            entityManager.persist(publication);
        }
    }
}

