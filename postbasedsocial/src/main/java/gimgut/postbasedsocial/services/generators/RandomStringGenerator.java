package gimgut.postbasedsocial.services.generators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("RandomStringGenerator")
public class RandomStringGenerator extends StringGenerator {

    public final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public final char[] alphabetExtended = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    /**
     * Chars are taken from char[] alphabet
     * @return random char
     */
    private char generateRandomChar() {
        return alphabet[random.nextInt(alphabet.length)];
    }

    /**
     * Chars are taken from char[] alphabetExtended
     * @return random char
     */
    private char generateRandomExtendedChar() {
        return alphabetExtended[random.nextInt(alphabetExtended.length)];
    }

    /**
     *
     * @param minLength inclusive
     * @param maxLength inclusive
     * @return random name
     */
    public String generateRandomName(int minLength, int maxLength) {
        int sLen = generateRandomInt(minLength, maxLength);
        if (sLen<1)
            return "";

        char[] s = new char[sLen];
        s[0] = Character.toUpperCase(generateRandomChar());
        for (int i=1; i<sLen; i++) {
            s[i] = generateRandomChar();
        }
        return new String(s);
    }

    /**
     *
     * @param minLength inclusive
     * @param maxLength inclusive
     * @return random password
     */
    public String generateRandomPassword(int minLength, int maxLength) {
        int pLen = generateRandomInt(minLength, maxLength);
        if (pLen<1)
            return "";

        char[] p = new char[pLen];
        for (int i=0; i<pLen; i++) {
            p[i] = generateRandomExtendedChar();
        }
        return new String(p);
    }

    /**
     *
     * @param minLength inclusive
     * @param maxLength inclusive
     * @return random email
     */
    public String generateRandomEmail(int minLength, int maxLength) {
        int pLen = generateRandomInt(minLength, maxLength);
        if (pLen<1)
            return "";

        char[] p = new char[pLen];
        for (int i=0; i<pLen; i++) {
            p[i] = generateRandomExtendedChar();
        }

        p[p.length/2]='@';
        return new String(p);
    }

    /**
     *
     * @param minLength inclusive
     * @param maxLength inclusive
     * @param capital first character flag
     * @return random word
     */
    public String generateWord(int minLength, int maxLength, boolean capital) {
        int sLen = generateRandomInt(minLength, maxLength);
        if (sLen<1)
            return "";

        char[] s = new char[sLen];
        char firstChar = generateRandomChar();
        if (capital)
            firstChar = Character.toUpperCase(firstChar);
        s[0] = firstChar;
        for (int i=1; i<sLen; i++) {
            s[i] = generateRandomChar();
        }
        return new String(s);
    }

    /**
     *
     * @param minLength inclusive
     * @param maxLength inclusive
     * @return non capital random word
     */
    public String generateWord(int minLength, int maxLength) {
        return generateWord(minLength, maxLength, false);
    }
}
