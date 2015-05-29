package org.giavacms.resource.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.base.util.ResourceUtils;
import org.giavacms.resource.management.AppConstants;
import org.giavacms.resource.model.pojo.Resource;
import org.giavacms.resource.util.ResourceConcerter;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.RESOURCES_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResourceRepositoryRs extends RsRepositoryService<Resource>
{

   private static final long serialVersionUID = 1L;

   public ResourceRepositoryRs()
   {
   }

   @GET
   public Response getResources()
   {

      return getFileFromFolder("/");
   }

   @GET
   @Path("/{folder}")
   public Response getFileFromFolder(@PathParam("folder") String folder)
   {
      try
      {
         List<String> list = ResourceUtils.getAllFiles("/");
         List<Resource> resources = ResourceConcerter.fromNames("/", list);
         return Response.status(Response.Status.OK).entity(resources)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize, startRow")
                  .header("startRow", 0)
                  .header("pageSize", list.size())
                  .header("listSize", list.size())
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @GET
   @Path("/{fileName}")
   //TODO
   public Response getFile(@PathParam("fileName") String fileName)
   {
      try
      {
         Resource file = null;
         return Response.status(Response.Status.OK).entity(file).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @POST
   @Path("/{fileName}")
   //TODO
   public Response updateFile(@PathParam("fileName") String fileName)
   {
      try
      {
         Resource file = null;
         return Response.status(Response.Status.OK).entity(file).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @DELETE
   @Path("/{fileName}")
   //TODO
   public Response deleteFile(@PathParam("fileName") String fileName)
   {
      try
      {
         Resource file = null;
         return Response.status(Response.Status.OK).entity(file).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

}
