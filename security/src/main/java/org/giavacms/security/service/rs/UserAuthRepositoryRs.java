package org.giavacms.security.service.rs;

import java.util.List;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.security.management.AppConstants;
import org.giavacms.security.model.UserAuth;
import org.giavacms.security.model.UserRole;
import org.giavacms.security.repository.UserAuthRepository;
import org.giavacms.security.repository.UserRoleRepository;
import org.giavacms.security.util.PasswordUtils;

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

   @Inject
   UserRoleRepository userRoleRepository;

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

   // @Override
   protected void _postPersist(UserAuth object) throws Exception
   {
      for (String roleName : object.getRoles())
      {
         UserRole userRole = new UserRole();
         userRole.setRoleName(roleName);
         userRole.setUserAuth(object);
         userRoleRepository.persist(userRole);
      }
      super.postPersist(object);
   }

   // @Override
   protected void _postUpdate(UserAuth object) throws Exception
   {
      Search<UserRole> sr = new Search<UserRole>(UserRole.class);
      sr.getObj().setUserAuth(object);
      List<UserRole> formerRoles = userRoleRepository.getList(sr, 0, 0);
      for (UserRole formerRole : formerRoles)
      {
         if (!object.getRoles().contains(formerRole.getRoleName()))
         {
            userRoleRepository.delete(formerRole.getId());
         }
         else
         {
            object.getRoles().remove(formerRole.getRoleName());
         }
      }
      for (String roleName : object.getRoles())
      {
         UserRole userRole = new UserRole();
         userRole.setRoleName(roleName);
         userRole.setUserAuth(object);
         userRoleRepository.persist(userRole);
      }
      super.postUpdate(object);
   }

   @Override
   protected void prePersist(UserAuth object) throws Exception
   {
      object.setPassword(PasswordUtils.createPassword(object.getNewPassword()));
      super.prePersist(object);
   }

   @Override
   protected void preUpdate(UserAuth object) throws Exception
   {
      if (object.getNewPassword() == null || object.getNewPassword().trim().isEmpty())
      {
         object.setPassword(getRepository().find(object.getId()).getPassword());
      }
      else
      {
         object.setPassword(PasswordUtils.createPassword(object.getNewPassword()));
      }
      super.preUpdate(object);
   }
}
