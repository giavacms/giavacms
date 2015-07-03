package org.giavacms.chalet.service.rs;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Parade;
import org.giavacms.chalet.repository.ParadeRepository;

@Path(AppConstants.BASE_PATH + AppConstants.PARADE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParadeRepositoryRs extends RsRepositoryService<Parade>
{

   private static final long serialVersionUID = 471883957307995404L;

   public ParadeRepositoryRs()
   {
   }

   @Inject
   public ParadeRepositoryRs(ParadeRepository repository)
   {
      super(repository);
   }

}
