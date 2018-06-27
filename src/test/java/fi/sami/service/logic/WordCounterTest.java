/*
 * 2018 Sami.
 */
package fi.sami.service.logic;

import fi.sami.service.model.WordCount;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

/**
 * @author Sami
 */
@RunWith(JMockit.class)
public class WordCounterTest {

    private final static String LOST_PATTERN = "e";
    private final static String BOUNDARY1 = "TEST>";
    private final static String BOUNDARY2 = " WORD";
    private final static String BOUNDARY3 = "WORD";
    private final static String BOUNDARY4 = "<>??%";
    private final static String BOUNDARY5 = "<>>>TE";
    private final static String BOUNDARY6 = "<>TE>et";
    private final static String BOUNDARY7 = "<>TE>et<WORD>WORD2";
    private final static String EMPTY = "";
    private final static String NULL = null;
    private final static String STATUS_OK = "ok";
    private static final String NULL_ERROR = "Input was null";

    @Tested
    private WordCounter awc;

    @Test
    public void invokeFutureResult_ok(@Mocked WordCount wc) throws Exception {

        WordCount ab = new WordCount(1, 1, STATUS_OK);
        new Expectations() {
            {
                new WordCount(1, 1, STATUS_OK);
                times = 1;
                result = ab;
            }
        };
        awc.getWordCount(BOUNDARY1, LOST_PATTERN);
    }

    // rest of tests are checking input boundaries
    @Test
    public void countWords_boundary1() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY1, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 1);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 1);
        assertTrue("Invalid Porportion", result.getProportion() == 1);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary2() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY2, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 1);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 0);
        assertTrue("Invalid Porportion", result.getProportion() == 0);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary3() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY3, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 1);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 0);
        assertTrue("Invalid Porportion", result.getProportion() == 0);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary4() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY4, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 0);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 0);
        assertTrue("Invalid Porportion", result.getProportion() == 0);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary5() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY5, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 1);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 1);
        assertTrue("Invalid Porportion", result.getProportion() == 1);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary6() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY6, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 2);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 2);
        assertTrue("Invalid Porportion", result.getProportion() == 1);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary7() throws Exception {

        WordCount result = awc.getWordCount(BOUNDARY7, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 4);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 2);
        assertTrue("Invalid Porportion", result.getProportion() == 0.5);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

    @Test
    public void countWords_boundary8() throws Exception {

        WordCount result = awc.getWordCount(NULL, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 0);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 0);
        assertTrue("Invalid Porportion", result.getProportion() == 0);
        assertTrue("Invalid Status", result.getStatus().equals(NULL_ERROR));
    }

    @Test
    public void countWords_empty() throws Exception {

        WordCount result = awc.getWordCount(EMPTY, LOST_PATTERN);

        assertTrue("Invalid amount of Total Words", result.getTotalWords() == 0);
        assertTrue("Invalid amount of found e words", result.getFoundWords() == 0);
        assertTrue("Invalid Porportion", result.getProportion() == 0);
        assertTrue("Invalid Status", result.getStatus().equals(STATUS_OK));
    }

}
