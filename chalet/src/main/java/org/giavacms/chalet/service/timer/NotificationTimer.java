package org.giavacms.chalet.service.timer;

import org.giavacms.chalet.service.NotificationService;
import org.jboss.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Singleton
public class NotificationTimer
         implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   NotificationService notificationService;

   @Lock(LockType.READ)
   @Schedule(second = "0", minute = "30", hour = "19", persistent = false)
   public void timeout()
   {
      logger.info("TIMEOUT TO SEND NOTIFCATION");
      try
      {
         notificationService.sendParadeSms();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      try
      {
         notificationService.sendTickets();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}