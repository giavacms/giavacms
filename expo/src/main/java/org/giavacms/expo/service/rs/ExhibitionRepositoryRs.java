package org.giavacms.expo.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.expo.management.AppConstants;
import org.giavacms.expo.model.Exhibition;
import org.giavacms.expo.model.Participation;
import org.giavacms.expo.model.pojo.Discipline;
import org.giavacms.expo.repository.ExhibitionRepository;
import org.giavacms.expo.repository.ParticipationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.EXHIBITIONS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExhibitionRepositoryRs extends RsRepositoryService<Exhibition>
{

   private static final long serialVersionUID = 1L;

   public ExhibitionRepositoryRs()
   {
   }

   @Inject
   ParticipationRepository participationRepository;

   @Inject
   public ExhibitionRepositoryRs(ExhibitionRepository repository)
   {
      super(repository);
   }

   @GET
   @Path("/{exhibitionId}/disciplines")
   public Response disciplines(@PathParam("exhibitionId") String exhibitionId)
   {
      logger.info("@GET /" + exhibitionId + "/disciplines");
      try
      {
         List<Discipline> disciplines = participationRepository.getDisciplinesByExhibitionId(exhibitionId);
         // PaginatedListWrapper<T> wrapper = new PaginatedListWrapper<>();
         // wrapper.setList(list);
         // wrapper.setListSize(listSize);
         // wrapper.setStartRow(startRow);
         return Response.status(Status.OK).entity(disciplines)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error reading disciplines list").build();
      }
   }

   @GET
   @Path("/{exhibitionId}/participations")
   public Response participations(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @PathParam("exhibitionId") String exhibitionId,
            @QueryParam("artistName") String artistName,
            @QueryParam("discipline") String discipline)
   {
      logger.info("@GET /" + exhibitionId + "/participations");
      try
      {
         Search<Participation> search = new Search<Participation>(Participation.class);
         if (artistName != null && !artistName.trim().isEmpty())
         {
            search.getLike().getArtist().setName(artistName);
         }
         if (discipline != null && !discipline.trim().isEmpty())
         {
            search.getObj().setDiscipline(discipline);
         }
         search.getObj().getExhibition().setId(exhibitionId);
         int listSize = participationRepository.getListSize(search);
         List<Participation> participations = participationRepository.getList(search, startRow, pageSize);
         // PaginatedListWrapper<T> wrapper = new PaginatedListWrapper<>();
         // wrapper.setList(list);
         // wrapper.setListSize(listSize);
         // wrapper.setStartRow(startRow);
         return Response.status(Status.OK).entity(participations)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                  .header("startRow", startRow)
                  .header("pageSize", pageSize)
                  .header("listSize", listSize)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error reading participations list").build();
      }
   }

}