package org.giavacms.base.util;

/**
 * Created by fiorenzo on 30/04/15.
 */
public class RichContentUtils
{

   public static String createPageId(String title)
   {
      if (title == null)
         return null;
      title = title.trim().replaceAll("[^a-zA-Z0-9\\s]", "")
               .replaceAll("[\\s]", "-");
      return title.toLowerCase();
   }
}
