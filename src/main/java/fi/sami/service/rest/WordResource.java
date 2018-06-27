/*
 * 2018 Sami.
 */
package fi.sami.service.rest;

import fi.sami.service.logic.WordCountExternalDataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Sami
 */
@Path("/word")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WordResource {

    private static final String URL = "url";
    private static final String PATTERN = "pattern";
    private static final Logger logger = Logger.getLogger(WordResource.class.getName());

    @Inject
    WordCountExternalDataSource es;

    @Resource
    ManagedExecutorService mes;

    @POST
    public void getProportion(@Suspended AsyncResponse response, JsonObject url, @QueryParam("averaged") @DefaultValue("false") boolean averaged
    ) throws Exception {

        if (!url.containsKey(PATTERN) || !url.containsKey(URL)) {
            logger.log(Level.WARNING, "Bad json in payload");
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            throw new BadRequestException();
        }
        response.resume(Response.ok((mes.submit(() -> {
            return es.fetchData(url.getString(URL), averaged, url.getString(PATTERN));
        }).get())).status(Response.Status.OK).build());
    }

}
