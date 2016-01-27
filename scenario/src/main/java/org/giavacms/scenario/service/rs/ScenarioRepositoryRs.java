package org.giavacms.scenario.service.rs;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.scenario.management.AppConstants;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.repository.ScenarioRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path(AppConstants.BASE_PATH + AppConstants.SCENARIO_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScenarioRepositoryRs extends RsRepositoryService<Scenario>
{

   private static final long serialVersionUID = 1L;
   @Inject
   private TagRepository tagRepository;
   @Inject
   DocumentRepository documentRepository;
   @Inject
   ImageRepository imageRepository;

   @Inject
   public ScenarioRepositoryRs(ScenarioRepository scenarioRepository)
   {
      super(scenarioRepository);
   }

   public ScenarioRepositoryRs()
   {
   }

   @Override
   protected void postPersist(Scenario object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   @Override
   protected void postUpdate(Scenario object) throws Exception
   {
      postPersistOrUpdate(object);
   }

   private void postPersistOrUpdate(Scenario object)
   {
      tagRepository.set(Scenario.class.getSimpleName(), object.getId(), object.getTagList(),
               new Date());
   }

   private void saveDocument(String scenarioId, MultipartFormDataInput input, InputPart filePart, Document doc)
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
         ((ScenarioRepository) getRepository()).addDocument(scenarioId, doc.getId());
      }
      else
      {
         documentRepository.update(doc);
      }
   }

   private void saveImage(String scenarioId, MultipartFormDataInput input, InputPart filePart, Image img)
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
         ((ScenarioRepository) getRepository()).addImage(scenarioId, img.getId());
      }
      else
      {
         imageRepository.update(img);
      }
   }

   @DELETE
   @Path("/{scenarioId}/images/{imageId}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response deleteImage(@PathParam("scenarioId") String scenarioId,
            @PathParam("imageId") Long imageId)
            throws Exception
   {
      try
      {
         ((ScenarioRepository) getRepository()).removeImage(scenarioId, imageId);
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
   @Path("/{scenarioId}/documents/{documentId}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response deleteDocument(@PathParam("scenarioId") String scenarioId,
            @PathParam("documentId") Long documentId)
            throws Exception
   {
      try
      {
         ((ScenarioRepository) getRepository()).removeDocument(scenarioId, documentId);
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
   @Path("/{scenarioId}/images")
   public Response getImages(@PathParam("scenarioId") String scenarioId)
   {
      try
      {
         List<Image> list = ((ScenarioRepository) getRepository()).getImages(scenarioId);
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
   @Path("/{scenarioId}/documents")
   public Response getDocuments(@PathParam("scenarioId") String scenarioId)
   {
      try
      {
         List<Document> list = ((ScenarioRepository) getRepository()).getDocuments(scenarioId);
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
