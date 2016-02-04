package org.giavacms.base.filter;

import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class RESTCorsDemoResponseFilter implements ContainerResponseFilter {

    private final static Logger logger = Logger.getLogger(RESTCorsDemoResponseFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException {
        try {
            logger.info("method:" + requestCtx.getMethod() + "- origin: " + requestCtx.getHeaderString("Origin"));
            MultivaluedMap<String, String> headers = requestCtx.getHeaders();
            for (String key : headers.keySet()) {
                String value = headers.getFirst(key);
                logger.info(key + ":" + value);
            }

            logger.info("Executing REST response filter getMethod: " + requestCtx.getMethod() + " - " + requestCtx.getUriInfo()
                    .getPath());
            //responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
            if (responseCtx != null && responseCtx.getHeaders() != null) {
                if (requestCtx.getHeaderString("Origin") != null) {
                    logger.info("Origin: " + requestCtx.getHeaderString("Origin"));
                    responseCtx.getHeaders().add("Access-Control-Allow-Origin", requestCtx.getHeaderString("Origin"));
                } else {
                    logger.info("Origin NULLO ");
                    responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
                }
                responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
                responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
                responseCtx.getHeaders()
                        .add("Access-Control-Allow-Headers",
                                "Origin, X-Requested-With, Content-Type, Accept, Authorization, accept, authorization");
            } else {
                logger.info("qualcosa di nullo");
            }
        } catch (Throwable e) {
            logger.info(e.getMessage());
        }

    }
}
