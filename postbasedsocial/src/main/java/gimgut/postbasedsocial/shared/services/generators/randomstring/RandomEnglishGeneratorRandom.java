package gimgut.postbasedsocial.shared.services.generators.randomstring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@Qualifier("RandomEnglishGeneratorRandom")
public class RandomEnglishGeneratorRandom extends RandomStringGenerator {

    private ArrayList<String> words;

    public RandomEnglishGeneratorRandom() {

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

    //TODO: Build a distribution chart to look at
    public double f() {
        double alpha = 3.0;
        double x = alpha * random.nextDouble();
        return Math.exp(-alpha * x);
    }

    //TODO: Build a distribution chart to look at
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
