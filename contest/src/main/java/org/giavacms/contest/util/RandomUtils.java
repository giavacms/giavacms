package org.giavacms.contest.util;

import java.util.Random;

/**
 * Created by fiorenzo on 31/07/15.
 */
public class RandomUtils
{

   public static String getRandomNumber(String phones)
   {
      String[] rnds = phones.split(";|,");
      if (rnds.length > 0)
      {
         int idx = new Random().nextInt(rnds.length);
         String random = (rnds[idx]);
         return random;
      }
      return rnds[0];
   }
}
