package org.giavacms.chalet.service.jms;

import org.giavacms.base.management.AppProperties;
import org.giavacms.chalet.management.AppConstants;
import org.giavacms.chalet.management.AppKeys;
import org.giavacms.chalet.utils.RuntimeUtil;
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
@MessageDriven(name = "ResizeImageMDB", activationConfig = {
         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
         @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConstants.QUEUE_RESIZE_IMAGE),
         @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
         @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
         @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "3600"),
         @ActivationConfigProperty(propertyName = "dLQMaxResent", propertyValue = "0") })
@TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
public class ResizeImageMDB implements MessageListener
{

   Logger logger = Logger.getLogger(getClass().getName());

   @TransactionTimeout(unit = TimeUnit.MINUTES, value = 60)
   public void onMessage(Message message)
   {
      String fileName = null;
      try
      {
         logger.info("*** " + this.getClass().getSimpleName() + " *******************************");
         MapMessage mapMessage = (MapMessage) message;
         fileName = mapMessage.getString(AppKeys.FILE_NAME.name());
         //   /usr/bin/mogrify -resize 800 *.jpg
         //         String command = "/usr/bin/mogrify -resize 800 "
         String command = AppProperties.imageResizeCommand.value() + fileName;
         boolean success = RuntimeUtil.execute(command);
         if (success)
         {
            logger.info("IMAGE RESIZED " + fileName);
         }
      }
      catch (Throwable e)
      {
         String msg = "message: " + e.getMessage() + ", operation: ";
         logger.error(msg, e);
      }
   }
}
