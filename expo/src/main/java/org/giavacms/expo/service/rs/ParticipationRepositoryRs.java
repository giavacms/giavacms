package org.giavacms.expo.service.rs;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.expo.management.AppConstants;
import org.giavacms.expo.model.Participation;
import org.giavacms.expo.repository.ParticipationRepository;

@Path(AppConstants.BASE_PATH + AppConstants.PARTICIPATIONS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParticipationRepositoryRs extends RsRepositoryService<Participation>
{

   private static final long serialVersionUID = 1L;

   public ParticipationRepositoryRs()
   {
   }

   @Inject
   public ParticipationRepositoryRs(ParticipationRepository repository)
   {
      super(repository);
   }

}