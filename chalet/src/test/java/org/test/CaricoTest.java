package org.test;

import org.giavacms.base.util.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by fiorenzo on 09/07/15.
 */
public class CaricoTest
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
      List<String> righe = FileUtils.readLinesFromTextFile("src/test/resources/utenti.csv", "UTF-8");
      Calendar calendar = Calendar.getInstance();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateFormat cf = new SimpleDateFormat("yyyy-MM-dd");
//      calendar.add(Calendar.MINUTE, -60);
      int i = 0;
      for (String riga : righe)
      {
         i++;
         if (i < 51)
         {
            continue;
         }
         String[] campi = riga.split(",");
         UUID uuid = UUID.randomUUID();

         calendar.add(Calendar.MINUTE, 1);

         System.out
                  .println("INSERT INTO Account "
                           + " (phone, name ,surname, userRoles,uuid, confirmed,created) "
                           + " VALUES ('" + campi[2] + "','" + campi[1] + "','" + campi[0] + "','USER','" + uuid
                           .toString()
                           + "', '" + df.format(calendar.getTime()) + "', '" + df.format(calendar.getTime()) + "');");
         calendar.add(Calendar.MINUTE, 1);
         uuid = UUID.randomUUID();
         System.out
                  .println("INSERT INTO Vote "
                           + " ( uuid, active, confirmed, created, tocall, preference1,preference2 , preference3, phone, name, surname, dateTime) "
                           + " VALUES ('" + uuid.toString() + "', 1, '" + cf.format(calendar.getTime()) + "', '" + cf
                           .format(calendar.getTime()) + "', '+39 06 9480 6802', '18', 'NULL', 'NULL','" + campi[2].substring(2)
                           + "','"
                           + campi[1]
                           + "','" + campi[0]
                           + "','" + df.format(calendar.getTime()) + "');");
         calendar.add(Calendar.MINUTE, 1);
         uuid = UUID.randomUUID();
         System.out
                  .println("INSERT INTO Vote "
                           + " ( uuid, active, confirmed, created,tocall, preference1,preference2 , preference3, phone, name, surname, dateTime) "
                           + " VALUES ('" + uuid.toString() + "', 1, '" + cf.format(calendar.getTime()) + "', '" + cf
                           .format(calendar.getTime()) + "', 'NULL', '18', 'NULL', 'NULL','" + campi[2]
                           + "','NULL','NULL','" + df.format(calendar.getTime()) + "');");
         calendar.add(Calendar.MINUTE, 1);
         uuid = UUID.randomUUID();
         System.out
                  .println("INSERT INTO Vote "
                           + " ( uuid, active, confirmed, created,tocall, preference1,preference2 , preference3, phone, name, surname, dateTime) "
                           + " VALUES ('" + uuid.toString() + "', 1, '" + cf.format(calendar.getTime()) + "', '" + cf
                           .format(calendar.getTime()) + "', 'NULL', '18', 'NULL', 'NULL','" + campi[2]
                           + "','NULL','NULL','" + df.format(calendar.getTime()) + "');");
         if (i == 1000)
         {
            break;
         }
         else
         {
//            System.out.println("I: " + i);
//            System.out.println("------------");
//            System.out.println("------------");
         }

      }

   }

}
