package org.giava.cms.base.rs;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.giavacms.base.model.enums.ResourceType;
import org.giavacms.base.service.rs.ResourceRest;
import org.giavacms.base.service.rs.ResourceUploader;
import org.giavacms.common.util.FileUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class ResourceUpload2
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

      // Configure HttpClient to authenticate preemptively
      // by prepopulating the authentication data cache.

      // 1. Create AuthCache instance
      AuthCache authCache = new BasicAuthCache();

      // 2. Generate BASIC scheme object and add it to the local auth cache
      BasicScheme basicAuth = new BasicScheme();
      authCache.put(new HttpHost("localhost", 8080), basicAuth);

      // 3. Add AuthCache to the execution context
      BasicHttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.AUTH_CACHE, authCache);

      // 4. Create client executor and proxy
      DefaultHttpClient httpClient = new DefaultHttpClient();
      ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(httpClient, localContext);
      ResourceUploader client = ProxyFactory.create(ResourceUploader.class, uploadUrl, executor);

      String id = client.upload(imageRest);
      System.out.println("upload new = " + id);
   }
}
