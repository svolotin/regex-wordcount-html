/*
 * 2018 Sami.
 */
package fi.sami.service.logic;

import fi.sami.service.model.WordCount;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;

/**
 * Counts words. All words found in input string as well as words containing
 * desired regex pattern. Word is defined as sequence of word characters
 *
 * @author sami
 */
@Stateless
public class WordCounter {

    private static final int INIT = 0;
    private static final int ONE = 1;
    private static final String NULL_ERROR = "Input was null";
    private static final String OK = "ok";

    /**
     * @param input data to be examined
     * @param lostPattern regular expression pattern to be found from data
     * @return WordCount object for results
     */
    public WordCount getWordCount(String input, String lostPattern) {

        if (input == null) {
            return new WordCount(INIT, INIT, NULL_ERROR);

        }
        Pattern metachars = Pattern.compile("\\W+");
        Matcher metamatch = metachars.matcher(input);

        int found = INIT;
        int total = INIT;
        int prevMatchEnd = INIT;

        while (metamatch.find()) {
            if ((metamatch.start() - prevMatchEnd) >= 2 && metamatch.start() > 0) {
                total++;
                if (isWordWithCharacter(input, prevMatchEnd, metamatch.start(), lostPattern)) {
                    found++;
                }
            }
            prevMatchEnd = metamatch.end();
        }
        if ((input.length() - prevMatchEnd) >= 2) {
            total++;
            if (isWordWithCharacter(input, prevMatchEnd, input.length(), lostPattern)) {
                found++;
            }
        }
        return new WordCount(found, total, OK);
    }

    public boolean isWordWithCharacter(String in, int left, int right, String lostPattern) {
        Pattern ewordchars = Pattern.compile(lostPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = ewordchars.matcher(in);
        return matcher.region(left, right).find();
    }
}
