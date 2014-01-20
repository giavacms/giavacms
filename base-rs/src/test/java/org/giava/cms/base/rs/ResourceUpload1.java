package org.giava.cms.base.rs;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.apache.http.impl.client.DefaultHttpClient;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.service.rs.ResourceRest;
import org.giavacms.common.util.FileUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class ResourceUpload1
{

   @SuppressWarnings("rawtypes")
   public static void main(String[] args) throws Exception
   {
      String testFolder = "src/test/resources";
      String imageName = "twilio.jpg";
      byte[] imageBytes = FileUtils.getBytesFromFile(new File(testFolder, imageName));

      ResourceRest imageRest = new ResourceRest();
      imageRest.setBytes(imageBytes);
      imageRest.setName(imageName);
      imageRest.setResourceType(ResourceType.IMAGE.name());

      String uploadUrl = "http://localhost:8080/rest/v1/resource/upload";
      String overwriteUrl = "http://localhost:8080/rest/v1/resource/overwrite";
      String downloadUrl = "http://localhost:8080/rest/v1/resource/download";

      ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor();
      DefaultHttpClient client = (DefaultHttpClient) executor.getHttpClient();
      client.addRequestInterceptor(new AdminAdminAuthInterceptor());

      ClientRequest request = new ClientRequest(uploadUrl, executor);
      ClientResponse response = null;
      String id = null;

      request = new ClientRequest(uploadUrl);
      request.setHttpMethod("PUT");
      request.body(MediaType.APPLICATION_XML, imageRest);
      response = request.put(String.class);
      if (response.getStatus() == 200)
      {
         System.out.println("upload new = " + response.getEntity());
         id = (String) response.getEntity();
      }
      else
      {
         System.out.println("upload new = " + response.getStatus());
      }

      request = new ClientRequest(uploadUrl);
      request.setHttpMethod("PUT");
      request.body(MediaType.APPLICATION_XML, imageRest);
      response = request.put(String.class);
      if (response.getStatus() == 200)
      {
         System.out.println("upload existing = " + response.getEntity());
         id = (String) response.getEntity();
      }
      else
      {
         System.out.println("upload existing = " + response.getStatus());
      }

      request = new ClientRequest(overwriteUrl);
      request.setHttpMethod("PUT");
      request.body(MediaType.APPLICATION_XML, imageRest);
      response = request.put(String.class);
      if (response.getStatus() == 200)
      {
         System.out.println("overwrite existing = " + response.getEntity());
         id = (String) response.getEntity();
      }
      else
      {
         System.out.println("overwrite existing = " + response.getStatus());
      }

      request = new ClientRequest(overwriteUrl);
      request.setHttpMethod("PUT");
      imageRest.setName("another_" + imageRest.getName());
      request.body(MediaType.APPLICATION_XML, imageRest);
      response = request.put(String.class);
      if (response.getStatus() == 200)
      {
         System.out.println("overwrite new = " + response.getEntity());
         id = (String) response.getEntity();
      }
      else
      {
         System.out.println("overwrite new = " + response.getStatus());
      }

      request = new ClientRequest(downloadUrl);
      request.setHttpMethod("GET");
      request.queryParameter("type", ResourceType.IMAGE.name());
      request.queryParameter("id", id);
      response = request.get(byte[].class);
      if (response.getStatus() == 200)
      {
         System.out.println("download = " + ((byte[]) response.getEntity()).length + " bytes");
      }
      else
      {
         System.out.println("download = " + response.getStatus());
      }

   }

}
