package org.giavacms.richcontent.service.rs;

import org.giavacms.api.model.Group;
import org.giavacms.api.model.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.richcontent.management.AppConstants;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.RichContentType;
import org.giavacms.richcontent.model.Tag;
import org.giavacms.richcontent.repository.TagRepository;

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
public class TagRepositoryRs extends RsRepositoryService<Tag>
{

   private static final long serialVersionUID = 1L;

   public TagRepositoryRs()
   {
   }

   @Inject
   public TagRepositoryRs(TagRepository tagRepository)
   {
      super(tagRepository);
   }

   @GET
   @Path("/groups")
   public Response getRequestTags(@QueryParam("richContentType") String richContentType,
            @QueryParam("startRow") String startRow, @QueryParam("pageSize") String pageSize)
   {
      try
      {
         Search<Tag> st = new Search<Tag>(Tag.class);
         st.setGrouping("tagName");
         st.getObj().setRichContent(new RichContent());
         st.getObj().getRichContent().setRichContentType(new RichContentType(richContentType));

         List<Group<Tag>> list = ((TagRepository) getRepository())
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
