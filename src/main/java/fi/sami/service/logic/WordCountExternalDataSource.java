/*
 * 2018 Sami.
 */
package fi.sami.service.logic;

import fi.sami.service.model.WordCount;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Sami
 *
 * Utility to fetch data from websites. count total amount of words and words
 * containing desired regex pattern
 */
@Stateless
public class WordCountExternalDataSource {

    private static final int INIT_ZERO = 0;
    private static final int BUFFER_SIZE = 40960;
    private static final String OK = "ok";
    private static final String FAILURE = "Failed, pleas try again";
    private static final String URL_FAILED = "Failed, Please check the URL";
    private static final String CONN_FAILED = "Connection to URL Failed.";
    private static final String ASYNC_FAILED = "Word computing failed.";
    private static final Logger logger = Logger.getLogger(WordCountExternalDataSource.class.getName());

    @EJB
    WordCounter awc;

    /**
     * Connects to the URL and fetches data in smaller slices and does the word
     * calculation
     *
     * @param url to connect.
     * @param averaged if true slices depending of buffer size is skipped and
     * result is averaged
     * @param lostPattern regex pattern to search from the input data
     * @return WordCount object containing the result
     */
    public WordCount fetchData(String url, boolean averaged, String lostPattern) {
        try {
            URL uri = new URL(url);
            URLConnection conn = uri.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            int slices = conn.getContentLength() / BUFFER_SIZE;
            return getWordCount(in, slices, averaged, lostPattern);

        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, URL_FAILED, ex);
            return new WordCount(INIT_ZERO, INIT_ZERO, URL_FAILED);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, CONN_FAILED, ex);
            return new WordCount(INIT_ZERO, INIT_ZERO, CONN_FAILED);
        }
    }

    /**
     * reads the Stream and calls wordCounter to calculate
     *
     * @param in reader for input stream
     * @param slices internal parameter to see how many slices of data is coming
     * in
     * @param averaged if to skip parts of inpur stream
     * @param lostPattern regex pattern to be found from the data
     * @return WordCount object containing result of the analysis
     */
    public WordCount getWordCount(BufferedReader in, int slices, boolean averaged, String lostPattern) {
        long time = System.currentTimeMillis();

        try {
            StringWriter out = new StringWriter();
            int charsRead = INIT_ZERO;
            int found = INIT_ZERO;
            int total = INIT_ZERO;
            int rounds = 0;
            char[] buf = new char[BUFFER_SIZE];

            while ((charsRead = in.read(buf, 0, BUFFER_SIZE)) != -1) {
                out.write(buf, 0, charsRead);
                WordCount part = awc.getWordCount(out.toString(), lostPattern);
                found += part.getFoundWords() >= 0 ? part.getFoundWords() : 0;
                total += part.getTotalWords() >= 0 ? part.getTotalWords() : 0;

                if (averaged && slices > 4) {
                    in.skip(BUFFER_SIZE);
                }
                rounds++;
            }

            in.close();

            if (averaged && slices > 4) {
                found = found / rounds;
                total = total / rounds;
            }
            long totalTime = System.currentTimeMillis() - time;
            logger.log(Level.INFO, "Processing time: {0} in {1} rounds", new Object[]{totalTime, rounds});
            return new WordCount(found, total, OK);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, CONN_FAILED, ex);
            return new WordCount(INIT_ZERO, INIT_ZERO, CONN_FAILED);
        }
    }
}
