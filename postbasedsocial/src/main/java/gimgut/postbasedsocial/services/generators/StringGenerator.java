package gimgut.postbasedsocial.services.generators;

import java.util.Random;

public abstract class StringGenerator {

    protected Random random = new Random();

    /**
     * @param min inclusive
     * @param max inclusive
     * @return random int in uniform distribution
     */
    public int generateRandomInt(int min, int max) { return min + random.nextInt(max - min + 1); }

    abstract public String generateWord(int minLength, int maxLength, boolean capital);
    abstract public String generateWord(int minLength, int maxLength);

    /**
     * @param minWordCount inclusive
     * @param maxWordCount inclusive
     * @param minWordLen inclusive
     * @param maxWordLen inclusive
     * @return random sentence
     */
    public String generateSentence(int minWordCount, int maxWordCount, int minWordLen, int maxWordLen) {
        int sLen = generateRandomInt(minWordCount, maxWordCount);
        if (sLen<1)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(generateWord(minWordLen,maxWordLen,true));
        for (int i=1; i<sLen; i++) {
            stringBuilder.append(" ");
            stringBuilder.append(generateWord(minWordLen, maxWordLen));
        }
        stringBuilder.append(".");
        return stringBuilder.toString();
    }

    /**
     * @param minWordCount inclusive
     * @param maxWordCount inclusive
     * @return random sentence with words len [3,10]
     */
    public String generateSentence(int minWordCount, int maxWordCount) {
        return generateSentence(minWordCount, maxWordCount, 3, 10);
    }

    /**
     * @param minSentencesCount inclusive
     * @param maxSentencesCount inclusive
     * @return paragraph with sentences word count [5,20]
     */
    public String generateParagraph(int minSentencesCount, int maxSentencesCount) {
        int sLen = generateRandomInt(minSentencesCount, maxSentencesCount);
        if (sLen < 1)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            if (i != 0)
                stringBuilder.append(" ");
            stringBuilder.append(generateSentence(5,20));
        }
        return stringBuilder.toString();
    }
}