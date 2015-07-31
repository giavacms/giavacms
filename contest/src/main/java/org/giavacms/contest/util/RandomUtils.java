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

   public static void main(String[] args)
   {
      String num = "+39 06 9480 1443;+39 06 9480 6802;+39 06 9480 1527;+39 06 9480 1571;+39 0522 152 0065";
      System.out.println(getRandomNumber(num));
      System.out.println(getRandomNumber(num));
      System.out.println(getRandomNumber(num));
      System.out.println(getRandomNumber(num));
      System.out.println(getRandomNumber(num));

   }
}
