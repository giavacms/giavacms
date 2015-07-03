package org.giavacms.chalet.service.rs;

import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.ChaletRanking;
import org.giavacms.chalet.service.NotificationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Path(AppConstants.BASE_PATH + AppConstants.NOTIFICATION_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationServiceRs implements Serializable
{

   @Inject
   NotificationService notificationService;

   @GET
   @Path("/parade")
   public Response sendParadeSms()
   {
      try
      {
         List<ChaletRanking> result = notificationService.sendParadeSms();
         return Response.status(Response.Status.OK).entity(result).build();
      }
      catch (Exception e)
      {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity(e.getMessage()).build();
      }
   }

   @GET
   @Path("/tickets")
   public Response sendTickets()
   {
      try
      {
         boolean result = notificationService.sendTickets();
         return Response.status(Response.Status.OK).entity(result).build();
      }
      catch (Exception e)
      {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity(e.getMessage()).build();
      }
   }

}
