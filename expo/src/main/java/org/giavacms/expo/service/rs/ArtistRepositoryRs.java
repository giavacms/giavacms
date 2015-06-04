package org.giavacms.expo.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.expo.management.AppConstants;
import org.giavacms.expo.model.Artist;
import org.giavacms.expo.repository.ArtistRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.BASE_PATH + AppConstants.ARTISTS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArtistRepositoryRs extends RsRepositoryService<Artist>
{

   private static final long serialVersionUID = 1L;

   public ArtistRepositoryRs()
   {
   }

   @Inject
   public ArtistRepositoryRs(ArtistRepository repository)
   {
      super(repository);
   }

}