package org.giavacms.chalet.service.rs;

import org.apache.commons.io.IOUtils;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.ImageRepository;
import org.giavacms.base.util.FileUtils;
import org.giavacms.base.util.HttpUtils;
import org.giavacms.base.util.MimeUtils;
import org.giavacms.base.util.ResourceUtils;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Chalet;
import org.giavacms.chalet.repository.ChaletRepository;
import org.giavacms.chalet.repository.TagRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path(AppConstants.BASE_PATH + AppConstants.CHALET_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChaletRepositoryRs extends RsRepositoryService<Chalet>
{

   private static final long serialVersionUID = 1L;

   public ChaletRepositoryRs()
   {
   }

   @Inject
   public ChaletRepositoryRs(ChaletRepository repository)
   {
      super(repository);
   }

   @Inject
   TagRepository tagRepository;
   @Inject
   ImageRepository imageRepository;

   @POST
   @Path("/{chaletId}/images")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   //TODO _ RESIZE
   public Response addImage(@PathParam("chaletId") String chaletId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> fileParts = formParts.get("file");
         for (InputPart filePart : fileParts)
         {
            Image img = new Image();
            try
            {
               saveImage(chaletId, input, filePart, img);
               String output = "File saved to server location : " + img.getFilename();
               return Response.status(Status.OK).entity(img).build();
            }
            catch (Exception e)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file : " + img.getFilename()).build();
            }
         }
         return Response.status(Status.BAD_REQUEST)
                  .entity("File part not found in form data").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error creating doc").build();
      }
   }

   @PUT
   @Path("/{chaletId}/images/{imageId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   //TODO _ RESIZE
   public Response updateImage(@PathParam("chaletId") String chaletId,
            @PathParam("imageId") Long imageId,
            MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> fileParts = formParts.get("file");
         for (InputPart filePart : fileParts)
         {
            Image img = new Image();
            try
            {
               img.setId(imageId);
               saveImage(chaletId, input, filePart, img);
               String output = "File saved to server location : " + img.getFilename();
               return Response.status(Status.OK).entity(img).build();
            }
            catch (Exception e)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file : " + img.getFilename()).build();
            }
         }
         return Response.status(Status.BAD_REQUEST)
                  .entity("File part not found in form data").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error creating doc").build();
      }
   }

   @DELETE
   @Path("/{chaletId}/images/{imageId}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response deleteImage(@PathParam("chaletId") String chaletId,
            @PathParam("imageId") Long imageId)
            throws Exception
   {
      try
      {
         ((ChaletRepository) getRepository()).removeImage(chaletId, imageId);
         return Response.status(200).entity("ok").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error updating image").build();
      }
   }

   private void saveImage(String chaletId, MultipartFormDataInput input, InputPart filePart, Image img)
            throws Exception
   {
      // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
      MultivaluedMap<String, String> headers = filePart.getHeaders();
      String fileName = FileUtils.getLastPartOf(HttpUtils.parseFileName(headers));
      // Handle the body of that part with an InputStream
      InputStream istream = filePart.getBody(InputStream.class, null);
      byte[] byteArray = IOUtils.toByteArray(istream);
      img.setData(byteArray);
      img.setType(MimeUtils.getContentType(FileUtils.getLastPartOf(fileName)));
      String filename = ResourceUtils.createFile_(AppConstants.IMG_FOLDER, fileName, byteArray);
      img.setFilename(filename);
      if (input.getFormDataMap().containsKey("name"))
      {
         String name = input.getFormDataMap().get("name").get(0).getBodyAsString();
         img.setName(name);
      }
      if (input.getFormDataMap().containsKey("description"))
      {
         String description = input.getFormDataMap().get("description").get(0).getBodyAsString();
         img.setDescription(description);
      }
      if (img.getId() == null)
      {
         img = imageRepository.persist(img);
         ((ChaletRepository) getRepository()).addImage(chaletId, img.getId());
      }
      else
      {
         imageRepository.update(img);
      }
   }

   @GET
   @Path("/{chaletId}/images")
   public Response getImages(@PathParam("chaletId") String chaletId)
   {
      try
      {
         List<Image> list = ((ChaletRepository) getRepository()).getImages(chaletId);
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

   @Override
   protected void postPersist(Chalet object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   @Override
   protected void postUpdate(Chalet object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   private void postPersistOrUpdate(Chalet object)
   {
      tagRepository.set(object.getId(), object.getTagList(),
               new Date());
   }

   @Override
   protected void postDelete(Object key) throws Exception
   {
      tagRepository.set(key.toString(), new ArrayList<String>(),
               new Date());
   }

}