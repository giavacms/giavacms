package org.giavacms.chalet.service.timer;

import org.giavacms.chalet.service.ParadeService;
import org.jboss.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fiorenzo on 03/07/15.
 */
@Singleton
public class ParadeTimer
         implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   ParadeService paradeService;

   @Lock(LockType.READ)
   @Schedule(second = "0", minute = "0", hour = "*/2", persistent = false)
   public void timeout()
   {
      logger.info("TIMEOUT TO UPDATE THE CHALET PARADE");
      Date atTime = new Date();
      try
      {
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.DAY_OF_MONTH, 7);
         calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
         calendar.set(Calendar.YEAR, 2015);
         calendar.set(Calendar.HOUR, 13);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         System.out.println("NOW:" + atTime);
         System.out.println("LIMIT:" + calendar.getTime());
         if (atTime.before(calendar.getTime()))
         {
            System.out.printf("OK NEW PARADE");
            paradeService.create(atTime);
         }
         else
         {
            System.out.printf("NO NEW PARADE");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

   }

   public static void main(String[] args)
   {
      Date atTime = new Date();

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, 7);
      calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
      calendar.set(Calendar.YEAR, 2015);
      calendar.set(Calendar.HOUR, 13);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);

      System.out.println(atTime);
      System.out.println(calendar.getTime());

      if (atTime.before(calendar.getTime()))
      {
         System.out.printf("OK");
      }
      else
      {
         System.out.printf("NO NEW PARADE");
      }

   }
}