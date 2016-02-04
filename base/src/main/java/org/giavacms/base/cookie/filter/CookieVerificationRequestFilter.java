package org.giavacms.base.cookie.filter;

import org.giavacms.base.cookie.annotation.AccountCookieVerification;
import org.jboss.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

//@Provider
//@AccountCookieVerification
public class CookieVerificationRequestFilter implements ContainerRequestFilter
{

   @Context
   HttpServletRequest httpServletRequest;

   private final static Logger logger = Logger.getLogger(CookieVerificationRequestFilter.class.getName());

   @Override
   public void filter(ContainerRequestContext requestCtx) throws IOException
   {
      logger.info("Executing REST CookieVerificationRequestFilter filter");
      if (requestCtx.getMethod().equals("OPTIONS"))
      {
         logger.info("bypasso options");
         return;
      }

      try
      {
         if (httpServletRequest.getUserPrincipal() == null)
         {
            logger.error("Error CookieVerificationRequestFilter: user principal null ");
            requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
         }

      }
      catch (Exception e)
      {
         logger.error("Error CookieVerificationRequestFilter " + e.getMessage());
         requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
         return;
      }
   }
}
