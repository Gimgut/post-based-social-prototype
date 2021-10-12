package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.services.TimeService;
import gimgut.postbasedsocial.services.generators.StringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class RandomPostService {

    private final EntityManager entityManager;
    private final StringGenerator stringGenerator;
    private final TimeService timeService;

    public RandomPostService(EntityManager entityManager, StringGenerator stringGenerator, TimeService timeService) {
        this.entityManager = entityManager;
        this.stringGenerator = stringGenerator;
        this.timeService = timeService;
    }

    @Transactional
    public void addRandomPublications(int count) {
        for (int i = 0; i < count; i++) {
            LocalDateTime rndLdt = timeService.getUtcNowLDT().plusSeconds(stringGenerator.generateRandomInt(20*i,20*(i+1)));
            Post publication = new Post(
                    stringGenerator.generateSentence(1,4),
                    stringGenerator.generateParagraph(1,10),
                    i+1,
                    Timestamp.valueOf(rndLdt)
            );
            entityManager.persist(publication);
        }
    }
}

