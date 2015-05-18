package org.giavacms.richcontent.service.rs;

import org.apache.commons.io.IOUtils;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.DocumentRepository;
import org.giavacms.base.repository.ImageRepository;
import org.giavacms.base.util.FileUtils;
import org.giavacms.base.util.HttpUtils;
import org.giavacms.base.util.MimeUtils;
import org.giavacms.base.util.ResourceUtils;
import org.giavacms.richcontent.management.AppConstants;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.TagRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
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

@Path(AppConstants.BASE_PATH + AppConstants.RICHCONTENT_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RichContentRepositoryRs extends RsRepositoryService<RichContent>
{

   private static final long serialVersionUID = 1L;

   public RichContentRepositoryRs()
   {
   }

   @Inject
   public RichContentRepositoryRs(RichContentRepository repository)
   {
      super(repository);
   }

   @Inject
   TagRepository tagRepository;
   @Inject
   ImageRepository imageRepository;

   @Inject
   DocumentRepository documentRepository;

   @GET
   @Path("/last")
   public Response getLast(@QueryParam("category") String category)
   {
      try
      {
         RichContent lastContent = ((RichContentRepository) getRepository()).getLast(category);
         if (lastContent != null)
         {
            return Response.status(Status.OK).entity(lastContent)
                     .build();
         }
         else
         {
            return Response.status(Status.NO_CONTENT).build();
         }
      }
      catch (NoResultException e)
      {
         return Response.status(Status.NO_CONTENT).build();
      }
      catch (Exception e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .build();
      }
   }

   @GET
   @Path("/highlight")
   public Response getHighlight(@QueryParam("category") String category)
   {
      try
      {
         RichContent highlightContent = ((RichContentRepository) getRepository()).getHighlight(category);
         if (highlightContent != null)
         {
            return Response.status(Status.OK).entity(highlightContent)
                     .build();
         }
         else
         {
            return Response.status(Status.NO_CONTENT).build();
         }
      }
      catch (NoResultException e)
      {
         return Response.status(Status.NO_CONTENT).build();
      }
      catch (Exception e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .build();
      }
   }

   @POST
   @Path("/{richContentId}/image")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response addImage(@PathParam("richContentId") String richContentId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         String fileName = "";
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> inPart = formParts.get("file");
         for (InputPart inputPart : inPart)
         {
            // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            fileName = FileUtils.getLastPartOf(HttpUtils.parseFileName(headers));
            // Handle the body of that part with an InputStream
            InputStream istream = inputPart.getBody(InputStream.class, null);
            byte[] byteArray = IOUtils.toByteArray(istream);
            Image image = new Image();
            image.setFilename(FileUtils.getLastPartOf(fileName));
            image.setType(ResourceUtils.getType(fileName));
            fileName = ResourceUtils.createImage_(AppConstants.IMG_FOLDER, fileName, byteArray);
            image.setFilename(fileName);
            if (input.getFormDataMap().containsKey("name"))
            {
               String name = input.getFormDataMap().get("name").get(0).getBodyAsString();
               image.setName(name);
            }
            if (input.getFormDataMap().containsKey("description"))
            {
               String description = input.getFormDataMap().get("description").get(0).getBodyAsString();
               image.setDescription(description);
            }

            image.setData(byteArray);
            image = imageRepository.persist(image);
            if (image.getId() == null)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file: " + fileName).build();
            }
            ((RichContentRepository) getRepository()).addImage(richContentId, image.getId());
         }
         String output = "File saved to server location : " + fileName;
         return Response.status(200).entity(output).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error creating image").build();
      }
   }

   @PUT
   @Path("/{richContentId}/image/{imageId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   //TODO
   public Response updateImage(@PathParam("richContentId") String richContentId,
            @PathParam("imageId") String imageId,
            MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         return Response.status(200).entity("ok").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error updating image").build();
      }
   }

   @DELETE
   @Path("/{richContentId}/image/{imageId}")
   @Consumes(MediaType.APPLICATION_JSON)
   //TODO
   public Response deleteImage(@PathParam("richContentId") String richContentId,
            @PathParam("imageId") String imageId)
            throws Exception
   {
      try
      {
         return Response.status(200).entity("ok").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error updating image").build();
      }
   }

   @POST
   @Path("/{richContentId}/document")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response addDocument(@PathParam("richContentId") String richContentId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         String fileName = "";
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> inPart = formParts.get("file");
         for (InputPart inputPart : inPart)
         {
            // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            fileName = FileUtils.getLastPartOf(HttpUtils.parseFileName(headers));
            // Handle the body of that part with an InputStream
            InputStream istream = inputPart.getBody(InputStream.class, null);
            byte[] byteArray = IOUtils.toByteArray(istream);
            Document doc = new Document();
            doc.setData(byteArray);
            doc.setType(MimeUtils.getContentType(FileUtils.getLastPartOf(fileName)));
            String filename = ResourceUtils.createFile_(AppConstants.DOC_FOLDER, fileName, byteArray);
            doc.setFilename(filename);
            if (input.getFormDataMap().containsKey("name"))
            {
               String name = input.getFormDataMap().get("name").get(0).getBodyAsString();
               doc.setName("");
            }
            if (input.getFormDataMap().containsKey("description"))
            {
               String description = input.getFormDataMap().get("description").get(0).getBodyAsString();
               doc.setDescription("");
            }
            doc = documentRepository.persist(doc);

            if (doc.getId() == null)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file: " + fileName).build();
            }
            ((RichContentRepository) getRepository()).addDocument(richContentId, doc.getId());
         }
         String output = "File saved to server location : " + fileName;
         return Response.status(200).entity(output).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error creating doc").build();
      }
   }

   @PUT
   @Path("/{richContentId}/document/{documentId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   //TODO
   public Response updateDocument(@PathParam("richContentId") String richContentId,
            @PathParam("documentId") String documentId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         return Response.status(200).entity("ok").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error updating doc").build();
      }
   }

   @DELETE
   @Path("/{richContentId}/document/{documentId}")
   @Consumes(MediaType.APPLICATION_JSON)
   //TODO
   public Response deleteDocument(@PathParam("richContentId") String richContentId,
            @PathParam("documentId") String documentId)
            throws Exception
   {
      try
      {
         return Response.status(200).entity("ok").build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error updating doc").build();
      }
   }

   @GET
   @Path("/{richContentId}/images")
   public Response getImages(@PathParam("richContentId") String richContentId)
   {
      try
      {
         List<Image> list = ((RichContentRepository) getRepository()).getImages(richContentId);
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
   @Path("/{richContentId}/documents")
   public Response getDocument(@PathParam("richContentId") String richContentId)
   {
      try
      {
         List<Image> list = ((RichContentRepository) getRepository()).getDocuments(richContentId);
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
   protected void postPersist(RichContent object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   @Override
   protected void postUpdate(RichContent object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   private void postPersistOrUpdate(RichContent object)
   {
      tagRepository.set(object.getId(), object.getTagList(),
               object.getDate());
      if (object.isHighlight())
      {
         ((RichContentRepository) getRepository()).refreshHighlight(object.getId(), object.getRichContentType());
      }
   }

   @Override
   protected void postDelete(Object key) throws Exception
   {
      tagRepository.set(key.toString(), new ArrayList<String>(),
               new Date());
   }

}