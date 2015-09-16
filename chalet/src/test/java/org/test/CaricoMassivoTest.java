package org.test;

import org.giavacms.base.util.FileUtils;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.model.Chalet;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by fiorenzo on 09/07/15.
 */
public class CaricoMassivoTest
{

   static String TARGET_HOST = "http://votalatua.estate";
   private static CrudTests crudTests;

   @BeforeClass
   public static void before()
   {
      crudTests = new CrudTests();

   }

   @Test
   public void test()
   {
      if (true)
         return;
      List<String> righe = FileUtils.readLinesFromTextFile("src/test/resources/chalet_indirizzi.csv", "UTF-8");
      for (String riga : righe)
      {
         String[] campi = riga.split(";");
         if (campi != null && campi.length > 0)
         {
            System.out.println(riga.toString());
            //"BACIO DELL'ONDA";"1";"Viale Trieste, 1";"bacio-dell-onda.jpg";
            Chalet chalet = new Chalet();
            chalet.setName(campi[0].replace("\"", ""));
            chalet.setLicenseNumber(campi[1].replace("\"", ""));
            if (campi.length > 2)
               chalet.setAddress(campi[2].replace("\"", ""));
            chalet.setCity("San Benededetto del Tronto");
            chalet.setProvince("AP");
            chalet.setPostalNumber("63074");
            chalet = crudTests
                     .create(TARGET_HOST, AppConstants.API_PATH + AppConstants.BASE_PATH + AppConstants.CHALET_PATH,
                              chalet);
            System.out.println(chalet);
            if (chalet != null && campi.length > 3 && campi[3] != null && !campi[3].isEmpty())
            {
               uploadPhoto(chalet.getId(), campi[3].replace("\"", ""));
            }
         }
      }

   }

   private void uploadPhoto(String chaletId, String fileName)
   {
      try
      {

         Client client = ClientBuilder.newClient();
         WebTarget target = client
                  .target(TARGET_HOST + AppConstants.API_PATH + AppConstants.BASE_PATH + AppConstants.CHALET_PATH + "/"
                           + chaletId + "/images");
         MultipartFormDataOutput mdo = new MultipartFormDataOutput();

         mdo.addFormData("file",
                  new FileInputStream(new File("/Users/fiorenzo/Desktop/CHALET/chalet_sbt_2015/" + fileName)),
                  MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName);

         // mdo.addPart(new FileInputStream(new
         // File("src/test/java/es/getdat/files/test/"
         // + getClass().getSimpleName() + ".java")),
         // MediaType.APPLICATION_OCTET_STREAM_TYPE, getClass()
         // .getSimpleName() + ".java");

         GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(
                  mdo)
         {
         };

         // Upload File
         Response res = target.request().post(
                  Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));

         System.out.println("code: " + res.getStatus() + " - status: "
                  + Response.Status.fromStatusCode(res.getStatus()) + " - family: "
                  + Response.Status.Family.familyOf(res.getStatus()));
         if (res.getStatus() == Response.Status.OK.getStatusCode())
         {
            String pluginConfigurationId = res.readEntity(String.class);
            System.out.println(pluginConfigurationId);
            Assert.assertNotNull(pluginConfigurationId);
         }
      }
      catch (IOException ex)
      {
         System.err.println(ex);
         Assert.fail(ex.getClass().getCanonicalName());
      }
   }

}
