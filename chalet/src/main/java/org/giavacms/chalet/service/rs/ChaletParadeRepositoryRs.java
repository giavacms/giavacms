package org.giavacms.chalet.service.rs;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.ChaletParade;
import org.giavacms.chalet.repository.ChaletParadeRepository;

@Path(AppConstants.BASE_PATH + AppConstants.PARADE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChaletParadeRepositoryRs extends RsRepositoryService<ChaletParade>
{

   private static final long serialVersionUID = 471883957307995404L;

   public ChaletParadeRepositoryRs()
   {
   }

   @Inject
   public ChaletParadeRepositoryRs(ChaletParadeRepository repository)
   {
      super(repository);
   }

}
