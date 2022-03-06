package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.role.RoleRepository;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoRepository;
import gimgut.postbasedsocial.shared.services.TimeService;
import gimgut.postbasedsocial.shared.services.generators.go1984.Generator1984;
import gimgut.postbasedsocial.shared.services.generators.randomstring.RandomStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RandomPostService {

    private final EntityManager entityManager;
    private final RandomStringGenerator randomStringGenerator;
    private final TimeService timeService;
    private final UserInfoRepository userInfoRepository;
    private final Generator1984 go1984;
    private final RoleRepository roleRepository;

    public RandomPostService(EntityManager entityManager, RandomStringGenerator randomStringGenerator, TimeService timeService, UserInfoRepository userInfoRepository, Generator1984 go1984, RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.randomStringGenerator = randomStringGenerator;
        this.timeService = timeService;
        this.userInfoRepository = userInfoRepository;
        this.go1984 = go1984;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void addRandomPublications(int count) {
        //create random user info
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Random01");
        userInfo.setUnlocked(true);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowZDT());
        userInfoRepository.save(userInfo);
        for (int iPost = 0; iPost < count; iPost++) {
            ZonedDateTime rndLdt = timeService.getUtcNowZDT().plusSeconds(randomStringGenerator.generateRandomInt(20 * iPost, 20 * (iPost + 1)));
            Post publication = new Post(
                    randomStringGenerator.generateSentence(1, 4),
                    randomStringGenerator.generateParagraph(1, 10),
                    iPost + 1,
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
        userInfo.setUnlocked(true);
        userInfo.setActivated(true);
        userInfo.setRegistrationTime(timeService.getUtcNowZDT());
        userInfo.setRole(roleRepository.findByName("WRITER"));
        userInfoRepository.save(userInfo);
        for (int page = realPages - 1; page > -1; page--) {
            ZonedDateTime rndLdt = timeService.getUtcNowZDT().minusSeconds(randomStringGenerator.generateRandomInt(20 * page, 20 * (page + 1)));
            Post publication = new Post(
                    "Pseudo page " + (page + 1),
                    pagedText.get(page),
                    100500,
                    rndLdt,
                    userInfo,
                    true);
            entityManager.persist(publication);
        }
    }
}

