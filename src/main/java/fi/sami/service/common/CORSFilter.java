/*
 * Copyright (c) 2018 Sami. All Rights Reserved. Company Confidential.
 */
package fi.sami.service.common;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * CORS filter for fitting Cross-Origin Resource Sharing (CORS) support to Java
 * web applications.
 *
 * CORSFilterQualifier added to solve wildfly swarm context loading problems
 * (unsatisfied dependency)
 *
 * @author Sami
 */
@CORSFilterQualifier
@Provider
public class CORSFilter implements ContainerResponseFilter {

    private static final String ANY_ORIGIN = "*";

    @Override
    public void filter(final ContainerRequestContext requestContext,
            final ContainerResponseContext cres) throws IOException {
        // resolve value for Access-Control-Allow-Origin
        String allowOrigin = ANY_ORIGIN;
        String requestOrigin = requestContext.getHeaderString("Origin");
        if (requestOrigin != null && requestOrigin.length() > 0) {
            allowOrigin = requestOrigin;
        }

        cres.getHeaders().putSingle("Access-Control-Allow-Origin", allowOrigin);
        cres.getHeaders().putSingle("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        cres.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cres.getHeaders().putSingle("Access-Control-Max-Age", "86400"); //time in seconds, 24 hours
        if (!allowOrigin.equals(ANY_ORIGIN)) {
            cres.getHeaders().putSingle("Vary", "Accept-Encoding, Origin");
        }
    }

}
