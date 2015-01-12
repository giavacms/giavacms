package org.giavacms.paypalweb.util;

public class RequestUriCleaner
{
   public static String cleanPage(String page)
   {
      if (page.startsWith("/db:"))
         return page.replace("db:", "p/").replace(".jsf", "");
      if (page.startsWith("/cache"))
         return page.replace("cache", "p").replace(".jsf", "");
      if (page.startsWith("/pages"))
         return page.replace("pages", "p").replace(".jsf", "");
      return "";
   }
}
