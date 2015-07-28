package org.giavacms.chalet.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.utils.QrUtils;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;

/**
 * Created by fiorenzo on 26/07/15.
 */
@Path(AppConstants.BASE_PATH + AppConstants.QRCODE_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QrCodeRs
{
   protected final Logger logger = Logger.getLogger(getClass());

   static final int QRCODE_IMAGE_HEIGHT = 300;
   static final int QRCODE_IMAGE_WIDTH = 300;
   static final String IMAGE_URL = "http://votalatua.estate/img/100.png";

   @GET
   @Produces("image/png")
   public Response getQrCode(@PathParam("file") String file)
   {
      if (file == null || file.trim().isEmpty() || !file.contains("."))
      {
         logger.error(" file name is null or not usable ");
         return RsRepositoryService.jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg",
                  " file name is null or not usable " + file);
      }
      String[] nameAndFormat = file.split(".");
      try
      {
         byte[] data = QrUtils
                  .encode(nameAndFormat[0], new URL(IMAGE_URL), QRCODE_IMAGE_WIDTH, QRCODE_IMAGE_HEIGHT);
         Response.ResponseBuilder response = Response.ok(data);
         response.header("Content-Disposition",
                  "attachment; filename=xcode-" + file);
         return response.build();
      }
      catch (Exception e)
      {

         logger.error(e.getMessage(), e);
         return RsRepositoryService.jsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "msg",
                  " Error creating image " + file);
      }
   }

}
