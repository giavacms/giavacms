package org.test;

import org.giavacms.base.util.FileUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fiorenzo on 09/07/15.
 */
public class FbQueryTest
{

   @Test
   public void test()
   {
      if (true)
         return;
      List<String> righe = FileUtils.readLinesFromTextFile("src/test/resources/chalet_www_fb.csv", "UTF-8");
      for (String riga : righe)
      {
         String[] campi = riga.split(",");
         String OPEN = " UPDATE DB_touristapp.chalets SET ";
         String FINAL = " WHERE id= '%s' ;";
         String facebook = " facebook = '%s'";
         String website = " website = '%s'";
         String instagram = " instagram = '%s'";
         String google = " google = '%s'";
         StringBuffer query = new StringBuffer(OPEN);
         boolean virgola = false;
         if (campi != null && campi.length > 2)
         {
//            ID,NOME,WWW,FACEBOOK,INSTAGRAM,GOOGLE
//            System.out.println(campi.length + ")" + Arrays.toString(campi));
            if (campi.length >= 3 &&campi[2] != null && !campi[2].trim().isEmpty())
            {
               query.append(String.format(website, campi[2].trim()));
               virgola = true;
            }

            if (campi.length >= 4 && campi[3] != null && !campi[3].trim().isEmpty())
            {
               if (virgola)
                  query.append(",");
               query.append(String.format(facebook, campi[3].trim()));
               virgola = true;
            }

            if (campi.length >= 5 && campi[4] != null && !campi[4].trim().isEmpty())
            {
               if (virgola)
                  query.append(",");
               query.append(String.format(instagram, campi[3].trim()));
               virgola = true;
            }

            if (campi.length >= 6 && campi[5] != null && !campi[5].trim().isEmpty())
            {
               if (virgola)
                  query.append(",");
               query.append(String.format(google, campi[5].trim()));
               virgola = true;
            }
            query.append(String.format(FINAL, campi[0].trim()));
            //ID,NOME,WWW,FACEBOOK,INSTAGRAM,GOOGLE

            System.out.println(query);

         }
      }

   }

}
