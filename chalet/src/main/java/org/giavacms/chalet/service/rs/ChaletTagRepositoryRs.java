package org.giavacms.chalet.service.rs;

import org.giavacms.api.model.Group;
import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.model.ChaletTag;
import org.giavacms.chalet.repository.ChaletTagRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.TAG_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChaletTagRepositoryRs extends RsRepositoryService<ChaletTag>
{

   private static final long serialVersionUID = 1L;

   public ChaletTagRepositoryRs()
   {
   }

   @Inject
   public ChaletTagRepositoryRs(ChaletTagRepository tagRepository)
   {
      super(tagRepository);
   }

   @GET
   @Path("/groups")
   public Response getRequestTags(@QueryParam("startRow") String startRow, @QueryParam("pageSize") String pageSize)
   {
      try
      {
         Search<ChaletTag> st = new Search<ChaletTag>(ChaletTag.class);
         st.setGrouping("tagName");
         st.getObj().setChalet(new Chalet());

         List<Group<ChaletTag>> list = ((ChaletTagRepository) getRepository())
                  .getGroups(st, Integer.parseInt(startRow), Integer.parseInt(pageSize));
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
