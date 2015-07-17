package org.giavacms.chalet.service.jms;

import org.giavacms.base.management.AppProperties;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.contest.util.SkebbySmsUtils;
import org.giavacms.contest.util.SkebbyType;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by pisi79 on 03/07/15.
 */
@MessageDriven(name = "SmsSenderMDB", activationConfig = {
         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
         @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConstants.QUEUE_NOTIFICATION_SENDER),
         @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
         @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
         @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "3600"),
         @ActivationConfigProperty(propertyName = "dLQMaxResent", propertyValue = "0") })
@TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
public class SmsSenderMDB implements MessageListener
{

   Logger logger = Logger.getLogger(getClass().getName());

   @TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
   public void onMessage(Message message)
   {
      String dstNumber = null;
      String text = null;
      String type = null;
      try
      {
         logger.info("*** " + this.getClass().getSimpleName() + " *******************************");
         MapMessage mapMessage = (MapMessage) message;
         dstNumber = mapMessage.getString(AppKeys.MESSAGE_number.name());
         text = mapMessage.getString(AppKeys.MESSAGE_text.name());
         //         type = mapMessage.getString(AppKeys.MESSAGE_type.name());
         //         PlivoSmsUtils.sendSms(AppProperties.authId.value(), AppProperties.authToken.value(),
         //                  AppProperties.number.value(), dstNumber, text, type, AppProperties.url.value());

         String result = SkebbySmsUtils.skebbyGatewaySendSMS(AppProperties.skebbyUsername.value(),
                  AppProperties.skebbyPassword.value(), new String[] { dstNumber }, text,
                  SkebbyType.send_sms_basic, AppProperties.skebbyNumber.value(),
                  null, "UTF-8");
         logger.info("result (" + dstNumber + "): " + result);
      }
      catch (Throwable re)
      {
         logger.error("eccezione " + getClass().getSimpleName() + ".onMessage(dstNumber = " + dstNumber + ", text = "
                  + text + ", type = "
                  + type + "): " + re.getClass().getCanonicalName() + ". "
                  + (re.getMessage() == null ? "" : re.getMessage()));
      }
   }
}
