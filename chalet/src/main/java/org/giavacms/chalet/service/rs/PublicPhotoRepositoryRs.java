package org.giavacms.chalet.service.rs;

import org.giavacms.api.model.Search;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Photo;
import org.giavacms.chalet.repository.PhotoRepository;
import org.giavacms.chalet.utils.PhotoUtils;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.PUBLIC_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublicPhotoRepositoryRs
{

   private static final long serialVersionUID = 471883957307995404L;

   @Inject
   PhotoRepository photoRepository;

   protected final Logger logger = Logger.getLogger(getClass());

   public PublicPhotoRepositoryRs()
   {
   }

   @GET
   public Response get(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize)
   {
      try
      {
         Boolean approved = true;
         Boolean evaluated = true;
         String chaletId = null;
         String accountId = null;
         String accountUuid = null;
         Search<Photo> search = PhotoUtils.makeSearch(chaletId, accountId, accountUuid, approved, evaluated);
         int listSize = photoRepository.getListSize(search);
         List<Photo> list = photoRepository.getList(search, startRow, pageSize);
         // rimuovo numero di telefono
         for (Photo photo : list)
         {
            photoRepository.getEm().detach(photo);
            //ELIMINO INFORMAZIONI SENSIBILI
            photo.setAccountId("");
            photo.setAccountNameSurname("");
         }
         return Response.status(Status.OK).entity(list)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                  .header("startRow", startRow)
                  .header("pageSize", pageSize)
                  .header("listSize", listSize)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @GET
   @Path("/{accountUuid}/photos")
   public Response getPhotos(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @PathParam("accountUuid") String accountUuid)
   {
      try
      {
         Boolean approved = true;
         Boolean evaluated = true;
         String chaletId = null;
         String accountId = null;
         Search<Photo> search = PhotoUtils.makeSearch(chaletId, accountId, accountUuid, approved, evaluated);
         int listSize = photoRepository.getListSize(search);
         List<Photo> list = photoRepository.getList(search, startRow, pageSize);
         // rimuovo numero di telefono
         for (Photo photo : list)
         {
            photoRepository.getEm().detach(photo);
            //ELIMINO INFORMAZIONI SENSIBILI
            photo.setAccountId("");
            photo.setAccountNameSurname("");
         }
         return Response.status(Status.OK).entity(list)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                  .header("startRow", startRow)
                  .header("pageSize", pageSize)
                  .header("listSize", listSize)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @OPTIONS
   public Response options()
   {
      logger.info("@OPTIONS");
      return Response.ok().build();
   }

   @OPTIONS
   @Path("{path:.*}")
   public Response allOptions()
   {
      logger.info("@OPTIONS ALL");
      return Response.ok().build();
   }
}
