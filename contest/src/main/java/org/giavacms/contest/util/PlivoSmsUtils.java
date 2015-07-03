package org.giavacms.contest.util;

import org.giavacms.contest.model.pojo.User;
import org.jboss.logging.Logger;
import org.plivo.ee.helper.api.MessageApi;
import org.plivo.ee.helper.api.configuration.GlobalConstant;
import org.plivo.ee.helper.api.pojo.ApiResponse;
import org.plivo.ee.helper.api.util.Authenticator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by fiorenzo on 01/07/15.
 */
public class PlivoSmsUtils
{

   static Logger logger = Logger.getLogger(PlivoSmsUtils.class.getName());

   public static void sendSms(User user, String authId, String authToken, String number, String url)
   {
      try
      {
         Client client = ClientBuilder.newClient().register(new Authenticator(authId, authToken));
         MessageApi messageApi = new MessageApi(client);
         messageApi.put(GlobalConstant.AUTH_ID, authId)
                  .put(GlobalConstant.SRC, number)
                  .put(GlobalConstant.DST, user.getPhone())
                  .put(GlobalConstant.TEXT,
                           " Gentile " + user.getName() + " " + user.getSurname() + " il tuo chalet " + user
                                    .getPreferenceName() + " conc. nro. " + user.getPreference1()
                                    + " e\' ora alla posizone nro" + user.getPosition()
                  )
                  .put(GlobalConstant.TYPE, "sms")
                  .put(GlobalConstant.URL, url + "?voteId=" + user.getVoteUid())
                  .put(GlobalConstant.METHOD, "POST")
                  .put(GlobalConstant.LOG, "true");
         ApiResponse result = messageApi.sendMessage();
         if (result != null && result.message_uuid != null && result.message_uuid.size() > 0)
         {
            String externalId = result.message_uuid.get(0);
         }
         else
         {
            logger.info("errore in fase di invio");
         }
      }
      catch (Exception re)
      {

      }
   }
}
