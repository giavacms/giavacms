package org.giavacms.chalet.service.jms;

import org.giavacms.base.management.AppProperties;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.contest.model.pojo.User;
import org.giavacms.contest.util.PlivoSmsUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by fiorenzo on 01/07/15.
 */
@MessageDriven(name = "FreeTicketsNotificationSenderMDB", activationConfig = {
         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
         @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConstants.QUEUE_NOTIFICATION_SENDER),
         @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
         @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
         @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "3600"),
         @ActivationConfigProperty(propertyName = "dLQMaxResent", propertyValue = "0") })
@TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
public class FreeTicketsNotificationSenderMDB implements MessageListener
{

   Logger logger = Logger.getLogger(getClass().getName());

   @TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
   public void onMessage(Message message)
   {
      String name = null;
      String surname = null;
      String phone = null;
      String preference1 = null;
      String preferenceName = null;
      String voteUid = null;
      String position = null;
      try
      {
         logger.info("*** " + this.getClass().getSimpleName() + " *******************************");
         MapMessage mapMessage = (MapMessage) message;
         name = mapMessage.getString(AppKeys.USER_name.name());
         surname = mapMessage.getString(AppKeys.USER_surname.name());
         preference1 = mapMessage.getString(AppKeys.USER_preference1.name());
         preferenceName = mapMessage.getString(AppKeys.USER_preferenceName.name());
         voteUid = mapMessage.getString(AppKeys.USER_voteUid.name());
         position = mapMessage.getString(AppKeys.USER_position.name());
         User user = new User(name, phone, preference1, surname, preferenceName);
         user.setVoteUid(voteUid);
         user.setPosition(Integer.valueOf(position));
         PlivoSmsUtils.sendSms(user, AppProperties.authId.value(), AppProperties.authToken.value(),
                  AppProperties.number.value(), AppProperties.url.value());
      }
      catch (Throwable re)
      {

      }
   }

}
