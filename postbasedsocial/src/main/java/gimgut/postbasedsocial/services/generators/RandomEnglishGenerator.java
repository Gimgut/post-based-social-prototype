package gimgut.postbasedsocial.services.generators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@Qualifier("RandomEnglishGenerator")
public class RandomEnglishGenerator extends StringGenerator {

    private ArrayList<String> words;

    @Value("classpath:data/words.txt")
    private Resource resourceWords;

    public RandomEnglishGenerator() {

        File resource = null;
        try {
            resource = new ClassPathResource(
                    "words.txt").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> words=null;
        try {
            words = Files.readAllLines(resource.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (words!=null) {
            this.words = new ArrayList<String>(words);
        }
    }

    public double f() {
        double alpha = 3.0;
        double x = alpha * random.nextDouble();
        return Math.exp(-alpha * x);
    }

    public int generateRandomWordIndex() {
        double y = f();
        return (int) (y*(double)words.size());
    }

    public String getRandomEnglishWord() {
        return words.get(generateRandomWordIndex());
    }

    @Override
    public String generateWord(int minLength, int maxLength, boolean capital) {
        String word = getRandomEnglishWord();
        return capital ? StringUtils.capitalize(word) : word;
    }

    @Override
    public String generateWord(int minLength, int maxLength) {
        return getRandomEnglishWord();
    }
}
