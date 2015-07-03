package org.giavacms.contest.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.logging.Logger;
import org.plivo.ee.helper.api.MessageApi;
import org.plivo.ee.helper.api.configuration.GlobalConstant;
import org.plivo.ee.helper.api.pojo.ApiResponse;
import org.plivo.ee.helper.api.util.Authenticator;

/**
 * Created by fiorenzo on 01/07/15.
 */
public class PlivoSmsUtils
{

   static Logger logger = Logger.getLogger(PlivoSmsUtils.class.getName());

   public static void sendSms(String authId, String authToken, String srcNumber, String dstNumber, String text,
            String url, String type)
   {
      try
      {
         Client client = ClientBuilder.newClient().register(new Authenticator(authId, authToken));
         MessageApi messageApi = new MessageApi(client);
         messageApi.put(GlobalConstant.AUTH_ID, authId)
                  .put(GlobalConstant.SRC, srcNumber)
                  .put(GlobalConstant.DST, dstNumber)
                  .put(GlobalConstant.TEXT, text)
                  .put(GlobalConstant.TYPE, "sms")
                  .put(GlobalConstant.URL, url + "?type=" + type)
                  .put(GlobalConstant.METHOD, "POST")
                  .put(GlobalConstant.LOG, "true");
         ApiResponse result = messageApi.sendMessage();
         if (result != null && result.message_uuid != null && result.message_uuid.size() > 0)
         {
            String externalId = result.message_uuid.get(0);
            logger.info("invio: " + externalId);
         }
         else
         {
            logger.error("errore in fase di invio");
         }
      }
      catch (Exception re)
      {
         logger.error("eccezione in fase di invio: " + re.getClass().getCanonicalName() + ". "
                  + (re.getMessage() == null ? "" : re.getMessage()));
      }
   }

}
