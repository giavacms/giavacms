package org.giavacms.expo.service.rs;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.expo.management.AppConstants;
import org.giavacms.expo.model.Exhibition;
import org.giavacms.expo.model.pojo.Discipline;
import org.giavacms.expo.repository.ExhibitionRepository;
import org.giavacms.expo.repository.ParticipationRepository;

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

}