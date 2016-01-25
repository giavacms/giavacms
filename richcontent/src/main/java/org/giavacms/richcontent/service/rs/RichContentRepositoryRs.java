package org.giavacms.richcontent.service.rs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.DocumentRepository;
import org.giavacms.base.repository.ImageRepository;
import org.giavacms.base.repository.TagRepository;
import org.giavacms.base.util.FileUtils;
import org.giavacms.base.util.HttpUtils;
import org.giavacms.base.util.MimeUtils;
import org.giavacms.base.util.ResourceUtils;
import org.giavacms.richcontent.management.AppConstants;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
   @Path("/{richContentId}/images")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   // TODO _ RESIZE
   public Response addImage(@PathParam("richContentId") String richContentId, MultipartFormDataInput input)
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
               saveImage(richContentId, input, filePart, img);
               String output = "File saved to server location : " + img.getFilename();
               logger.info(output);
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

   @POST
   @Path("/{richContentId}/images/{path:.*}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   // TODO _ RESIZE
   public Response addImage(@PathParam("richContentId") String richContentId, @PathParam("path") String path)
            throws Exception
   {
      try
      {
         Image img = new Image();
         img.setName(FileUtils.getLastPartOf(path));
         img.setFilename(path);
         img.setType(MimeUtils.getContentType(img.getName()));
         img = imageRepository.persist(img);
         ((RichContentRepository) getRepository()).addImage(richContentId, img.getId());
         return Response.status(Status.OK).entity(img).build();
      }
      catch (Exception e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error adding image: " + path).build();
      }
   }

   @POST
   @Path("/{richContentId}/documents/{path:.*}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response addDocument(@PathParam("richContentId") String richContentId, @PathParam("path") String path)
            throws Exception
   {
      try
      {
         Document doc = new Document();
         doc.setFilename(path);
         doc.setName(FileUtils.getLastPartOf(path));
         doc.setType(MimeUtils.getContentType(doc.getName()));
         doc = documentRepository.persist(doc);
         ((RichContentRepository) getRepository()).addDocument(richContentId, doc.getId());
         return Response.status(Status.OK).entity(doc).build();
      }
      catch (Exception e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR)
                  .entity("Error adding image: " + path).build();
      }
   }

   @PUT
   @Path("/{richContentId}/images/{imageId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   // TODO _ RESIZE
   public Response updateImage(@PathParam("richContentId") String richContentId,
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
               saveImage(richContentId, input, filePart, img);
               String output = "File saved to server location : " + img.getFilename();
               logger.info(output);
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
   @Path("/{richContentId}/images/{imageId}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response deleteImage(@PathParam("richContentId") String richContentId,
            @PathParam("imageId") Long imageId)
            throws Exception
   {
      try
      {
         ((RichContentRepository) getRepository()).removeImage(richContentId, imageId);
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
   @Path("/{richContentId}/documents")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response addDocument(@PathParam("richContentId") String richContentId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> fileParts = formParts.get("file");
         for (InputPart filePart : fileParts)
         {
            Document doc = new Document();
            try
            {
               saveDocument(richContentId, input, filePart, doc);
               String output = "File saved to server location : " + doc.getFilename();
               logger.info(output);
               return Response.status(200).entity(doc).build();
            }
            catch (Exception e)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file : " + doc.getFilename()).build();
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
   @Path("/{richContentId}/documents/{documentId}")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response updateDocument(@PathParam("richContentId") String richContentId,
            @PathParam("documentId") Long documentId, MultipartFormDataInput input)
            throws Exception
   {
      try
      {
         Map<String, List<InputPart>> formParts = input.getFormDataMap();
         List<InputPart> fileParts = formParts.get("file");
         for (InputPart filePart : fileParts)
         {
            Document doc = new Document();
            try
            {
               doc.setId(documentId);
               saveDocument(richContentId, input, filePart, doc);
               String output = "File saved to server location : " + doc.getFilename();
               logger.info(output);
               return Response.status(200).entity(doc).build();
            }
            catch (Exception e)
            {
               return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("Error writing file : " + doc.getFilename()).build();
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

   private void saveDocument(String richContentId, MultipartFormDataInput input, InputPart filePart, Document doc)
            throws Exception
   {
      // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
      MultivaluedMap<String, String> headers = filePart.getHeaders();
      String fileName = FileUtils.getLastPartOf(HttpUtils.parseFileName(headers));
      // Handle the body of that part with an InputStream
      InputStream istream = filePart.getBody(InputStream.class, null);
      byte[] byteArray = IOUtils.toByteArray(istream);
      doc.setData(byteArray);
      doc.setType(MimeUtils.getContentType(FileUtils.getLastPartOf(fileName)));
      String filename = ResourceUtils.createFile_(AppConstants.DOC_FOLDER, fileName, byteArray);
      doc.setFilename(filename);
      if (input.getFormDataMap().containsKey("name"))
      {
         String name = input.getFormDataMap().get("name").get(0).getBodyAsString();
         doc.setName(name);
      }
      if (input.getFormDataMap().containsKey("description"))
      {
         String description = input.getFormDataMap().get("description").get(0).getBodyAsString();
         doc.setDescription(description);
      }
      if (doc.getId() == null)
      {
         doc = documentRepository.persist(doc);
         ((RichContentRepository) getRepository()).addDocument(richContentId, doc.getId());
      }
      else
      {
         documentRepository.update(doc);
      }
   }

   private void saveImage(String richContentId, MultipartFormDataInput input, InputPart filePart, Image img)
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
         ((RichContentRepository) getRepository()).addImage(richContentId, img.getId());
      }
      else
      {
         imageRepository.update(img);
      }
   }

   @DELETE
   @Path("/{richContentId}/documents/{documentId}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response deleteDocument(@PathParam("richContentId") String richContentId,
            @PathParam("documentId") Long documentId)
            throws Exception
   {
      try
      {
         ((RichContentRepository) getRepository()).removeDocument(richContentId, documentId);
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
   public Response getDocuments(@PathParam("richContentId") String richContentId)
   {
      try
      {
         List<Document> list = ((RichContentRepository) getRepository()).getDocuments(richContentId);
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
      tagRepository.set(RichContent.class.getName(), object.getId(), object.getTagList(),
               object.getDate());
      if (object.isHighlight())
      {
         ((RichContentRepository) getRepository()).refreshHighlight(object.getId(), object.getRichContentType());
      }
   }

   @Override
   protected void postDelete(Object key) throws Exception
   {
      tagRepository.set(RichContent.class.getName(), key.toString(), new ArrayList<String>(),
               new Date());
   }

}