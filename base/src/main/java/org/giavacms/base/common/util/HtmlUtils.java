/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Logger;
import org.w3c.tidy.Tidy;

public class HtmlUtils
{

   protected static Logger logger = Logger
            .getLogger(HtmlUtils.class.getName());

   public static String encode(String toEncode)
   {
      try
      {
         return URLEncoder.encode(toEncode, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return "";
   }

   public static String normalizeHtml(String code)
   {
      if (code == null)
      {
         code = "";
      }
      Tidy tidy = new Tidy();
      tidy.setXHTML(true);
      tidy.setTidyMark(false);
      tidy.setDocType("omit");
      tidy.setPrintBodyOnly(true);
      tidy.setInputEncoding("UTF-8");
      tidy.setShowErrors(0);
      tidy.setShowWarnings(false);
      InputStream is;
      String content = "";
      OutputStream arg1 = new ByteArrayOutputStream();
      try
      {
         is = new ByteArrayInputStream(code.getBytes("UTF-8"));

         tidy.parse(is, arg1);
         // logger.info("*****************PRIMA: ");
         // logger.info(arg1.toString());
         // logger.info("*******************DOPO: ");
         // StringEscapeUtils.unescapeHtml(arg0)
         content = StringEscapeUtils.unescapeHtml(arg1.toString());
         content = handleAmpersand(content);
         // logger.info(content);

      }
      catch (UnsupportedEncodingException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return content;
   }

   private static String handleAmpersand(String content)
   {
      return (content == null) ? null : content.replaceAll("&", "&amp;");
   }

}
