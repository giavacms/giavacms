package org.giavacms.base.filter;

import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class HeaderRequestFilter implements ContainerRequestFilter
{

   Logger logger = Logger.getLogger(this.getClass());

   public void filter(ContainerRequestContext requestContext)
            throws IOException
   {
      logger.info("method:" + requestContext.getMethod());
      MultivaluedMap<String, String> headers = requestContext.getHeaders();
      for (String key : headers.keySet())
      {
         String value = headers.getFirst(key);
         logger.info(key + ":" + value);
      }
   }
}