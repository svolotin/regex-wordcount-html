package fi.sami.service.common;

import fi.sami.service.logic.WordCounter;
import fi.sami.service.logic.WordCountExternalDataSource;
import fi.sami.service.model.WordCount;
import fi.sami.service.rest.*;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.undertow.server.host.AccessLogSetting;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.undertow.UndertowFraction;

/**
 * Wildfly swarm main class.
 *
 * @author Sami
 */
public class Main {

    public static void main(final String[] args) throws Exception {

        System.setProperty("swarm.http.port", "8080");
        final Swarm swarm = new Swarm(); //swarm instance has to be initialized first!
        final JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "wordcounter.war");
        // Add rest implementations and configuration
        deployment.setContextRoot("wordcounter/rest");
        //standalone.xml for configure ManagedExecutorService thread pools
        final ClassLoader cl = Main.class.getClassLoader();
        // Add the properties files to the deployment
        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", cl), "classes/META-INF/beans.xml");
        // Explicitly declare dependecies to libraries "internal" to module implementations
        // Add rest implementatons and configuration
        deployment.addClass(CORSFilter.class);
        deployment.addClass(WordResource.class);
        deployment.addClass(WordCount.class);
        deployment.addClass(WordCountExternalDataSource.class);
        deployment.addClass(WordCounter.class);
        deployment.addAllDependencies();

        // Enable request logging for REST endpoints
        final UndertowFraction accessLog = UndertowFraction.createDefaultFraction();
        accessLog.subresources()
                .server("default-server")
                .subresources()
                .host("default-host")
                .accessLogSetting(new AccessLogSetting()
                        .useServerLog(true)
                        .pattern("\"%r\" %s %b"));
        swarm.fraction(accessLog);

        swarm
                .start()
                .deploy(deployment);
    }
}
