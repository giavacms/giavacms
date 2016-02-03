package org.giavacms.base.cookie.service.rs;

import org.jboss.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

//@Path("/login")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractCookieIdentityServiceRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @Context
   HttpServletRequest httpServletRequest;

   @GET
   public Response identity()
   {
      try
      {
         // TODO - GESTIONE RUOLI??
         String username = httpServletRequest.getUserPrincipal().getName();
         if (username == null)
         {
            return Response
                     .status(Response.Status.FORBIDDEN)
                     .entity("No user found for alias: " + username
                     ).build();
         }
         Object user = findByPrincipalName(username);
         @SuppressWarnings("rawtypes")
         List roles = getRoles(user);
         if (user != null && roles != null && !roles.isEmpty())
         {
            return Response.status(Response.Status.OK).entity(user).build();
         }
         return Response
                  .status(Response.Status.FORBIDDEN)
                  .entity("No user found for alias: " + username
                  ).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
         return Response
                  .status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Error attempting login: " + e.getMessage())
                  .build();
      }
   }

   @SuppressWarnings("rawtypes")
   protected abstract List getRoles(Object user);

   protected abstract Object findByPrincipalName(String username);

   @OPTIONS
   public Response options()
   {
      logger.info("@OPTIONS");
      return Response.ok().build();
   }

   @OPTIONS
   @Path("{path:.*}")
   public Response allOptions()
   {
      logger.info("@OPTIONS ALL");
      return Response.ok().build();
   }
}
