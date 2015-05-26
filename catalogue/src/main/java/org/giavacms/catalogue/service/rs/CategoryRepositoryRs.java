package org.giavacms.catalogue.service.rs;

import org.giavacms.catalogue.management.AppConstants;
import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.repository.CategoryRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.CATEGORIES_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryRepositoryRs extends RsRepositoryService<Category>
{

   private static final long serialVersionUID = 1L;

   public CategoryRepositoryRs()
   {
   }

   @Inject
   public CategoryRepositoryRs(CategoryRepository categoryRepository)
   {
      super(categoryRepository);
   }

   @GET
   @Path("/names")
   public Response getTipiRichContent()
   {
      try
      {
         Search<Category> srct = new Search<Category>(Category.class);
         List<Category> lrct = getRepository().getList(srct, 0, 0);
         if (lrct == null || lrct.size() == 0)
         {
            return Response.status(Status.NO_CONTENT).build();
         }
         List<String> names = new ArrayList<String>();
         for (Category rct : lrct)
         {
            names.add(rct.getName());
         }
         return Response.status(Status.OK).entity(names)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize, startRow")
                  .header("startRow", 0)
                  .header("pageSize", names.size())
                  .header("listSize", names.size())
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
   }

}
