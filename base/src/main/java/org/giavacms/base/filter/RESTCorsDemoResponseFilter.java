package org.giavacms.base.filter;

import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class RESTCorsDemoResponseFilter implements ContainerResponseFilter
{

   private final static Logger log = Logger.getLogger(RESTCorsDemoResponseFilter.class.getName());

   @Override
   public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException
   {
      log.info("Executing REST response filter getMethod: " + requestCtx.getMethod() + " - " + requestCtx.getUriInfo()
               .getPath());
      responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
      responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
      responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
      responseCtx.getHeaders()
               .add("Access-Control-Allow-Headers",
                        "Origin, X-Requested-With, Content-Type, Accept, Authorization, accept, authorization");
      // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
      //      if (requestCtx.getRequest().getMethod().equals("OPTIONS"))
      //      {
      //         log.info("HTTP Method (OPTIONS) - Detected!");
      //
      //         // Just send a OK signal back to the browser
      //         responseCtx.setEntity(Response.Status.OK).build());
      //         return;
      //      }

   }
}
