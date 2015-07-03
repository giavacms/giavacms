package org.giavacms.chalet.service.jms;

import java.util.concurrent.TimeUnit;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.giavacms.base.management.AppProperties;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.contest.util.PlivoSmsUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.logging.Logger;

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
         type = mapMessage.getString(AppKeys.MESSAGE_type.name());
         PlivoSmsUtils.sendSms(AppProperties.authId.value(), AppProperties.authToken.value(),
                  AppProperties.number.value(), dstNumber, text, type, AppProperties.url.value());
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
