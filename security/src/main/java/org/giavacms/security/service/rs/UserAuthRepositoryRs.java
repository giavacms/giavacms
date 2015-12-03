package org.giavacms.security.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.security.management.AppConstants;
import org.giavacms.security.model.UserAuth;
import org.giavacms.security.repository.UserAuthRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path(AppConstants.BASE_PATH + AppConstants.USER_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAuthRepositoryRs extends RsRepositoryService<UserAuth>
{

   private static final long serialVersionUID = 1L;

   public UserAuthRepositoryRs()
   {
   }

   @Inject
   public UserAuthRepositoryRs(UserAuthRepository userAuthRepository)
   {
      super(userAuthRepository);
   }

   @GET
   @Path("/byUsername/{username}")
   public Response findByUsername(@PathParam("username") String username)
   {
      logger.info("@GET byUsername:" + username);
      try
      {
         UserAuth t = ((UserAuthRepository) getRepository()).findByUsername(username);
         if (t == null)
         {
            return jsonResponse(Status.NOT_FOUND, "msg", "User not found for username: " + username);
         }
         else
         {
            return Response.status(Status.OK).entity(t).build();
         }
      }
      catch (NoResultException e)
      {
         logger.error(e.getMessage());
         return jsonResponse(Status.NOT_FOUND, "msg", "User not found for username: " + username);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error reading user with username: " + username);
      }
   }
}
