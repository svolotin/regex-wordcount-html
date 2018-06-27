/*
 * 2018 Sami.
 */
package fi.sami.service.rest;

import fi.sami.service.logic.WordCountExternalDataSource;
import fi.sami.service.model.WordCount;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.Test;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.runner.RunWith;

/**
 * @author Sami
 */
@RunWith(JMockit.class)
public class WordResourceTest {

    private final static String STATUS_OK = "ok";
    private final static String PATTERN = "e";

    @Tested
    private WordResource cut;

    @Injectable
    WordCountExternalDataSource es;

    @Injectable
    ManagedExecutorService mes;

    // Check that async action is invoked when endpoint is called with correct payload
    @Test
    public void callWordCounter(@Mocked AsyncResponse as, @Mocked Future<WordCount> fut) throws Exception {

        final JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("url", "test");
        json.add("pattern", PATTERN);
        JsonObject js = json.build();
        WordCount ws = new WordCount(1, 1, STATUS_OK);

        new Expectations() {
            {
                mes.submit((Callable<WordCount>) any);
                times = 1;
                result = fut;
                fut.get();
                times = 1;
                result = ws;
            }
        };
        cut.getProportion(as, js, false);
    }

    // Check the return with bad input payload
    @Test(expected = BadRequestException.class)
    public void callWordCounter_nok(@Mocked AsyncResponse as) throws Exception {

        final JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("test", "test");
        json.add("test", PATTERN);
        JsonObject js = json.build();
        WordCount ws = new WordCount(0, 0, STATUS_OK);

        new Expectations() {
            {
                Response.status(Response.Status.BAD_REQUEST).build();
                times = 1;
            }
        };
        cut.getProportion(as, js, false);
    }

}
