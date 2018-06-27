/*
 * 2018 Sami.
 */
package fi.sami.service.model;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Sami
 */
public class WordCountTest {

    private final static String STATUS_OK = "ok";
    private final static String STATUS_FAILED = "failed";

    @Test
    public void wordObject_ok() throws Exception {

        WordCount wc = new WordCount(1, 1, STATUS_OK);
        assertTrue("Invalid amount of Total Words", wc.getTotalWords() == 1);
        assertTrue("Invalid amount of found e words", wc.getFoundWords() == 1);
        assertTrue("Invalid Porportion", wc.getProportion() == 1);
        assertTrue("Invalid Status", wc.getStatus().equals(STATUS_OK));
    }

    @Test
    public void wordObject_nok() throws Exception {

        WordCount wc = new WordCount(-1, -1, STATUS_FAILED);
        assertTrue("Invalid amount of Total Words", wc.getTotalWords() == -1);
        assertTrue("Invalid amount of found e words", wc.getFoundWords() == -1);
        assertTrue("Invalid Porportion", wc.getProportion() == 0);
        assertTrue("Invalid Status", wc.getStatus().equals(STATUS_FAILED));
    }
}
