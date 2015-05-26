package org.giavacms.banner.service.rs;

import org.apache.commons.io.IOUtils;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.banner.management.AppConstants;
import org.giavacms.banner.model.Banner;
import org.giavacms.banner.repository.BannerRepository;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.ImageRepository;
import org.giavacms.base.util.FileUtils;
import org.giavacms.base.util.HttpUtils;
import org.giavacms.base.util.MimeUtils;
import org.giavacms.base.util.ResourceUtils;
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
import java.util.List;
import java.util.Map;

@Path(AppConstants.BASE_PATH + AppConstants.BANNER_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BannerRepositoryRs extends RsRepositoryService<Banner>
{

   private static final long serialVersionUID = 1L;

   @Inject
   ImageRepository imageRepository;

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

   @GET
   @Path("/{bannerId}/image")
   public Response getImages(@PathParam("bannerId") String bannerId)
   {
      try
      {
         Image image = ((BannerRepository) getRepository()).getImage(bannerId);
         if (image == null)
         {
            return Response.status(Status.NO_CONTENT).build();
         }
         return Response.status(Status.OK).entity(image).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
   }

   @POST
   @Path("/{bannerId}/image")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response addImage(@PathParam("bannerId") String bannerId, MultipartFormDataInput input)
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
               saveImage(bannerId, input, filePart, img);
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
   @Path("/{bannerId}/image/{imageId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response updateImage(@PathParam("bannerId") String bannerId,
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
               saveImage(bannerId, input, filePart, img);
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

   private void saveImage(String bannerId, MultipartFormDataInput input, InputPart filePart, Image img)
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
         ((BannerRepository) getRepository()).updateImage(bannerId, img.getId());
      }
      else
      {
         imageRepository.update(img);
      }
   }
}
