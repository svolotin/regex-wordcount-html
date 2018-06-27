/*
 * 2018 Sami.
 */
package fi.sami.service.logic;

import fi.sami.service.model.WordCount;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

/**
 * @author Sami
 */
@RunWith(JMockit.class)
public class WordCountExternalDataSourceTest {

    private final static String LOST_PATTERN = "e";
    private final static String TEST = "<>TE>et<WORD>WORD2";
    private final static String STATUS_OK = "ok";

    @Tested
    private WordCountExternalDataSource cut;

    @Injectable
    WordCounter awc;

    // Check that WordCounter bean is invoked and protect the code against unintentional changes-
    // Verify the result of wordcount
    @Test
    public void invokeWordCountBean(@Mocked URL uri, @Mocked URLConnection conn, @Mocked WordCounter wc) throws Exception {

        InputStream is = new ByteArrayInputStream(TEST.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        WordCount ab = new WordCount(1, 3, STATUS_OK);
        String pattern = "e";

        new Expectations() {
            {
                awc.getWordCount(TEST, LOST_PATTERN);
                times = 1;
                result = ab;
            }
        };
        cut.getWordCount(br, 0, false, pattern);
    }

}
