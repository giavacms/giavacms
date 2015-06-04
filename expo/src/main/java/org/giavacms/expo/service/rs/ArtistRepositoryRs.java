package org.giavacms.expo.service.rs;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.expo.management.AppConstants;
import org.giavacms.expo.model.Artist;
import org.giavacms.expo.model.Participation;
import org.giavacms.expo.repository.ArtistRepository;
import org.giavacms.expo.repository.ParticipationRepository;

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
   ParticipationRepository participationRepository;

   @Inject
   public ArtistRepositoryRs(ArtistRepository repository)
   {
      super(repository);
   }

   @GET
   @Path("/import")
   public void import2015(@QueryParam("year") String year)
   {
      try
      {
         ((ArtistRepository) getRepository()).importYear(year);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
   }

   @GET
   @Path("/{artistId}/participations")
   public Response participations(@PathParam("artistId") String artistId)
   {
      logger.info("@GET /" + artistId + "/participations");
      try
      {
         Search<Participation> sp = new Search<Participation>(Participation.class);
         sp.getObj().setArtist(new Artist());
         sp.getObj().getArtist().setId(artistId);
         List<Participation> participations = participationRepository.getList(sp, 0, 0);
         return Response.status(Status.OK).entity(participations)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error reading disciplines list").build();
      }
   }

}