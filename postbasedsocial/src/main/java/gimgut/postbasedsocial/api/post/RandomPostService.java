package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.shared.services.TimeService;
import gimgut.postbasedsocial.shared.services.generators.go1984.Generator1984;
import gimgut.postbasedsocial.shared.services.generators.randomstring.RandomStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RandomPostService {

    private final EntityManager entityManager;
    private final RandomStringGenerator randomStringGenerator;
    private final TimeService timeService;
    private final UserInfoRepository userInfoRepository;
    private final Generator1984 go1984;

    public RandomPostService(EntityManager entityManager, RandomStringGenerator randomStringGenerator, TimeService timeService, UserInfoRepository userInfoRepository, Generator1984 go1984) {
        this.entityManager = entityManager;
        this.randomStringGenerator = randomStringGenerator;
        this.timeService = timeService;
        this.userInfoRepository = userInfoRepository;
        this.go1984 = go1984;
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
            LocalDateTime rndLdt = timeService.getUtcNowLDT().plusSeconds(randomStringGenerator.generateRandomInt(20*i,20*(i+1)));
            Post publication = new Post(
                    randomStringGenerator.generateSentence(1,4),
                    randomStringGenerator.generateParagraph(1,10),
                    i+1,
                    rndLdt,
                    userInfo,
                    true);
            entityManager.persist(publication);
        }
    }

    @Transactional
    public void add1984(int pages) {
        List<String> pagedText = go1984.getPseudoPages();
        int realPages = Math.min(pagedText.size(), pages);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Random02");
        userInfo.setLocked(false);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowLDT());
        userInfoRepository.save(userInfo);
        for (int i = realPages-1; i > -1; i--) {
            LocalDateTime rndLdt = timeService.getUtcNowLDT().plusSeconds(randomStringGenerator.generateRandomInt(20 * i, 20 * (i + 1)));
            Post publication = new Post(
                    "Pseudo page " + (i + 1),
                    pagedText.get(i),
                    100500,
                    rndLdt,
                    userInfo,
                    true);
            entityManager.persist(publication);
        }
    }
}

