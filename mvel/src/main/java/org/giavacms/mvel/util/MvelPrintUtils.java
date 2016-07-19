package org.giavacms.mvel.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

public class MvelPrintUtils
{

   private static Logger log = Logger.getLogger(MvelPrintUtils.class.getName());

   private static final String NUMBER_OF_PAGE_PATTERN = "<img.*?src=\".*?page-number.png\".*?/>";
   private static final String NUMBER_OF_PAGE_REPLACE = "<span id=\"pagenumber\" ></span>";
   private static final String NUMBER_OF_PAGES_PATTERN = "<img.*?src=\".*?page-total-number.png\".*?/>";
   private static final String NUMBER_OF_PAGES_REPLACE = "<span id=\"pagecount\" ></span>";
   // <img src="../utils/getFirma.jsp?id=21" alt="flower" />
   // <img src="JNDI.../utils/getFirma.jsp?id=21" alt="flower" />
   private static final String IMAGES_PATTERN = "<img.*?src=\"(\\.\\.).*?/>";
   private static final String IMAGES_PREFIX = "http://localhost:8080/";

   public static String correggi(String testo)
   {
      testo = correggiImmagini(testo);
      testo = correggiNumeriDiPagina(testo);
      return testo;
   }

   public static double margin(double base, double offset, String testo,
            double mmPerRiga)
   {
      long count = 0;
      int lastIndex = 0;
      double coefficiente = 1.0;
      if (testo.indexOf("large") != -1)
         coefficiente = 1.1;
      if (testo.indexOf("x-large") != -1)
         coefficiente = 1.2;
      if (testo.indexOf("xx-large") != -1)
         coefficiente = 1.3;
      while (lastIndex != -1)
      {
         lastIndex = testo.indexOf("<br />", lastIndex + 1);
         if (lastIndex != -1)
         {
            count++;
         }
      }
      lastIndex = 0;
      while (lastIndex != -1)
      {
         lastIndex = testo.indexOf("</p>", lastIndex + 1);
         if (lastIndex != -1)
         {
            count++;
         }
      }
      double margin = base + offset + mmPerRiga * count * coefficiente;
      double minimum = base + offset;
      boolean setMeTrueIfDebugging = false;
      if (setMeTrueIfDebugging)
      {
         log.info("base = " + base);
         log.info("offset = " + offset);
         log.info("count = " + count);
         log.info("mmPerRiga = " + mmPerRiga);
         log.info("coefficiente = " + coefficiente);
         log.info("minimum = " + minimum);
         log.info("margin = " + margin);
      }
      return margin < minimum ? minimum : margin;
   }

   public static String correggiNumeriDiPagina(String testo)
   {
      Pattern p = Pattern.compile(NUMBER_OF_PAGE_PATTERN);
      Matcher m = p.matcher(testo);
      while (m.find())
      {
         testo = m.replaceAll(NUMBER_OF_PAGE_REPLACE);
      }
      p = Pattern.compile(NUMBER_OF_PAGES_PATTERN);
      m = p.matcher(testo);
      while (m.find())
      {
         testo = m.replaceAll(NUMBER_OF_PAGES_REPLACE);
      }
      return testo;
   }

   public static String correggiImmagini(String testo)
   {
      Pattern p = Pattern.compile(IMAGES_PATTERN);
      Matcher m = p.matcher(testo);
      String replacement = null;

      Map<String, String> mappa_modifiche = new HashMap<String, String>();

      @SuppressWarnings("unused")
      int counter = 0;

      while (m.find())
      {
         String mGroup = m.group();
         // System.out.println(mGroup);
         replacement = mGroup.replaceAll("\\.\\.",
                  IMAGES_PREFIX);
         // result = m.replaceAll(replacement);
         mappa_modifiche.put(mGroup, replacement);

         counter++;
         // System.out.println(counter + ". " + mGroup + " -> " +
         // replacement);

      }

      for (String mGroup : mappa_modifiche.keySet())
      {
         testo = testo.replace(mGroup, mappa_modifiche.get(mGroup));
      }
      return testo;
   }

   public static void main(String[] args)
   {
      String content = "<img src=\"../utils/getImmagine.jsp?id=1\" alt=\"logo_cesvip\" width=\"203\" height=\"94\" />&nbsp;&nbsp;&nbsp; <img src=\"../utils/getFirma.jsp?id=5\" alt=\"firma_coopservice\" />"
               + "<img src=\"../utils/getImmagine.jsp?id=2\" alt=\"logo_cesvip\" width=\"203\" height=\"94\" />&nbsp;&nbsp;&nbsp; <img src=\"../utils/getFirma.jsp?id=6\" alt=\"firma_coopservice\" />"
               + "<img src=\"../utils/getImmagine.jsp?id=3\" alt=\"logo_cesvip\" width=\"203\" height=\"94\" />&nbsp;&nbsp;&nbsp; <img src=\"../utils/getFirma.jsp?id=7\" alt=\"firma_coopservice\" />"
               + "<img src=\"../utils/getImmagine.jsp?id=4\" alt=\"logo_cesvip\" width=\"203\" height=\"94\" />&nbsp;&nbsp;&nbsp; <img src=\"../utils/getFirma.jsp?id=8\" alt=\"firma_coopservice\" />";
      System.out.println("-----------------------");
      System.out.println("-----------------------");
      System.out.println("-----------------------");
      System.out.println(MvelPrintUtils.correggiImmagini(content)
               .replace("&nbsp;", "\n").replace("><", "\n"));
   }

}
