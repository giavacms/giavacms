package org.giavacms.resource.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.base.model.pojo.Resource;
import org.giavacms.base.util.ResourceUtils;
import org.giavacms.resource.management.AppConstants;

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
   public Response getResources(@QueryParam("onlyFolder") String onlyFolder)
   {
      return getFilesFromFolder("/", onlyFolder);
   }

   public Response getFoldersFromFolder(String folder)
   {
      logger.info(" getFoldersFromFolder folder: " + folder);

      try
      {
         List<Resource> resources = ResourceUtils.getAllFolders(folder, null);
         return Response.status(Response.Status.OK).entity(resources)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize, startRow")
                  .header("startRow", 0)
                  .header("pageSize", resources != null ? resources.size() : 0)
                  .header("listSize", resources != null ? resources.size() : 0)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @GET
   @Path("/{folder:.*}")
   public Response getFilesFromFolder(@PathParam("folder") String folder, @QueryParam("onlyFolder") String onlyFolder)
   {
      logger.info(" getFilesFromFolder folder: " + folder);
      try
      {
         if (onlyFolder != null && onlyFolder.equals("true"))
            return getFoldersFromFolder(folder);
         List<Resource> resources = ResourceUtils.getAllFiles(folder);
         return Response.status(Response.Status.OK).entity(resources)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize, startRow")
                  .header("startRow", 0)
                  .header("pageSize", resources != null ? resources.size() : 0)
                  .header("listSize", resources != null ? resources.size() : 0)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @POST
   @Path("/{folder:.*}")
   //TODO
   public Response createFolder(@PathParam("folder") String folder, Resource resource)
   {
      try
      {
         resource = ResourceUtils.createSubFolder(folder, resource.getName());
         return Response.status(Response.Status.OK).entity(resource).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @GET
   @Path("/{folder:.*}/files/{fileName}")
   //TODO
   public Response getFile(@PathParam("folder") String folder,
            @PathParam("fileName") String fileName)
   {
      try
      {
         logger.info("getFile:" + folder + " - " + fileName);
         Resource resource = ResourceUtils.getFileContent(folder, fileName);
         return Response.status(Response.Status.OK).entity(resource).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @POST
   @Path("/{folder:.*}/files")
   //TODO
   public Response createFile(@PathParam("folder") String folder, Resource resource)
   {
      try
      {
         resource = ResourceUtils.createFileContent(folder, resource.getName(), resource.getFileContent());
         return Response.status(Response.Status.OK).entity(resource).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @PUT
   @Path("/{folder:.*}/files/{fileName}")
   //TODO
   public Response updateFile(@PathParam("folder") String folder,
            @PathParam("fileName") String fileName, Resource resource)
   {
      try
      {
         resource = ResourceUtils.setFileContent(folder, fileName, resource.getFileContent());
         return Response.status(Response.Status.OK).entity(resource).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @DELETE
   @Path("/{folder:.*}/files/{fileName}")
   //TODO
   public Response deleteFile(@PathParam("folder") String folder,
            @PathParam("fileName") String fileName)
   {
      try
      {
         ResourceUtils.deleteFileContent(folder, fileName);
         return Response.status(Response.Status.NO_CONTENT)
                  .entity("Resource deleted for fileName: " + fileName).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
   }

}
