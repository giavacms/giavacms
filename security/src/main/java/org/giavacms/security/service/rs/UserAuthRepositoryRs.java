package org.giavacms.security.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.security.management.AppConstants;
import org.giavacms.security.model.UserAuth;
import org.giavacms.security.repository.UserAuthRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

}
