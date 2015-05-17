package org.giavacms.banner.service.rs;

import org.giavacms.banner.management.AppConstants;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.banner.model.Banner;
import org.giavacms.banner.repository.BannerRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.BANNER_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BannerRepositoryRs extends RsRepositoryService<Banner>
{

   private static final long serialVersionUID = 1L;

   @Inject
   public BannerRepositoryRs(BannerRepository bannerRepository)
   {
      super(bannerRepository);
   }

   public BannerRepositoryRs()
   {
   }

   @GET
   @Path("/random")
   public Response getRandomByTypologyAndLimit(@QueryParam("typology") String typology,
            @QueryParam("limit") int limit)
   {
      try
      {
         List<Banner> list = ((BannerRepository) getRepository()).getRandomByTypology(typology,
                  limit);
         if (list == null || list.size() == 0)
         {
            return Response.status(Status.NO_CONTENT).build();
         }
         return Response.status(Status.OK).entity(list)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize, startRow")
                  .header("startRow", 0)
                  .header("pageSize", list.size())
                  .header("listSize", list.size())
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
   }
}
