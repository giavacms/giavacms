/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.util;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.base.model.TemplateImpl;
import org.jboss.logging.Logger;


public class PageUtils
{

   protected static Logger logger = Logger
            .getLogger(PageUtils.class.getName());

   private static final String prologo = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
   private static final String newline = "\n";

   public static String createPageId(String title)
   {
      if (title == null)
         return null;
      title = title.trim().replaceAll("[^a-zA-Z0-9\\s]", "")
               .replaceAll("[\\s]", "-");
      return title.toLowerCase();
   }

   public static Page clone(Page original)
   {
      Page clone = new Page();
      clone.setActive(true);
      clone.setContent(original.getContent());
      clone.setExtension(original.getExtension());
      clone.setTitle(original.getTitle() + " - COPIA");
      clone.getTemplate().setTemplate(original.getTemplate().getTemplate());
      clone.getTemplate().setActive(true);
      clone.getTemplate().setCol1(original.getTemplate().getCol1());
      clone.getTemplate().setCol2(original.getTemplate().getCol2());
      clone.getTemplate().setCol3(original.getTemplate().getCol3());
      clone.getTemplate().setCol3(original.getTemplate().getCol3());
      clone.getTemplate().setFooter(original.getTemplate().getFooter());
      clone.getTemplate().setHeader(original.getTemplate().getHeader());
      return clone;
   }

   @Deprecated
   public static Page generateContent_old(Page page)
   {
      StringBuffer contentBuffer = new StringBuffer(prologo);
      if ((page.getTemplate().getHeader() != null)
               && (!"".equals(page.getTemplate().getHeader())))
      {
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getHeader_start());
         contentBuffer.append(page.getTemplate().getHeader());
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getHeader_stop());

      }
      if ((page.getTemplate().getCol1() != null)
               && (!"".equals(page.getTemplate().getCol1())))
      {
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol1_start());
         contentBuffer.append(page.getTemplate().getCol1());
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol1_stop());
      }

      if ((page.getTemplate().getCol2() != null)
               && (!"".equals(page.getTemplate().getCol2())))
      {
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol2_start());
         contentBuffer.append(page.getTemplate().getCol2());
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol2_stop());
      }

      if ((page.getTemplate().getCol3() != null)
               && (!"".equals(page.getTemplate().getCol3())))
      {
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol3_start());
         contentBuffer.append(page.getTemplate().getCol3());
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getCol3_stop());
      }
      if ((page.getTemplate().getFooter() != null)
               && (!"".equals(page.getTemplate().getFooter())))
      {
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getFooter_start());
         contentBuffer.append(page.getTemplate().getFooter());
         contentBuffer.append(page.getTemplate().getTemplate()
                  .getFooter_stop());
      }
      page.setContent(contentBuffer.toString());
      return page;
   }

   /**
    * La pagina fornisce pezzi solo per i template che prevedono start e stop di una sezione Le parti del template sono
    * comunque tutte aggiunte
    * 
    * @param page
    * @return
    */
   public static Page generateContent(Page page)
   {
      page.setContent(createContent(page));
      return page;
   }

   public static String createContent(Page page)
   {
      closeHtmlTags(page);
      Template t = page.getTemplate().getTemplate();
      TemplateImpl i = page.getTemplate();
      // closeHtmlTagsT(i);
      StringBuffer b = new StringBuffer(t.getHeader_start() == null ? prologo
               : t.getHeader_start());
      b.append(newline);

      if (t.getHeader_stop() != null && t.getHeader_stop().length() > 0)
      {
         b.append(i.getHeader() == null ? "" : i.getHeader());
         b.append(newline);
         b.append(t.getHeader_stop());
         b.append(newline);
      }

      b.append(t.getCol1_start() == null ? "" : t.getCol1_start());
      b.append(newline);
      if (t.getCol1_stop() != null && t.getCol1_stop().length() > 0)
      {
         b.append(i.getCol1() == null ? "" : i.getCol1());
         b.append(newline);
         b.append(t.getCol1_stop());
         b.append(newline);
      }

      b.append(t.getCol2_start() == null ? "" : t.getCol2_start());
      b.append(newline);
      if (t.getCol2_stop() != null && t.getCol2_stop().length() > 0)
      {
         b.append(i.getCol2() == null ? "" : i.getCol2());
         b.append(newline);
         b.append(t.getCol2_stop());
         b.append(newline);
      }

      b.append(t.getCol3_start() == null ? "" : t.getCol3_start());
      b.append(newline);
      if (t.getCol3_stop() != null && t.getCol3_stop().length() > 0)
      {
         b.append(i.getCol3() == null ? "" : i.getCol3());
         b.append(newline);
         b.append(t.getCol3_stop());
         b.append(newline);
      }

      b.append(t.getFooter_start() == null ? "" : t.getFooter_start());
      b.append(newline);
      if (t.getFooter_stop() != null && t.getFooter_stop().length() > 0)
      {
         b.append(i.getFooter() == null ? "" : i.getFooter());
         b.append(newline);
         b.append(t.getFooter_stop());
         b.append(newline);
      }

      // logger.info(b.toString());
      return b.toString();
   }

   public static void closeHtmlTags(Page page)
   {
      if (page == null || page.getTemplate() == null
               || page.getTemplate().getTemplate() == null)
      {
         return;
      }
      if (page.getTemplate().getTemplate().getStatico())
      {
         page.getTemplate().setHeader(
                  page.getTemplate().getHeader() == null ? null : HtmlUtils
                           .normalizeHtml(page.getTemplate().getHeader()));
         page.getTemplate().setCol1(
                  page.getTemplate().getCol1() == null ? null : HtmlUtils
                           .normalizeHtml(page.getTemplate().getCol1()));
         page.getTemplate().setCol2(
                  page.getTemplate().getCol2() == null ? null : HtmlUtils
                           .normalizeHtml(page.getTemplate().getCol2()));
         page.getTemplate().setCol3(
                  page.getTemplate().getCol3() == null ? null : HtmlUtils
                           .normalizeHtml(page.getTemplate().getCol3()));
         page.getTemplate().setFooter(
                  page.getTemplate().getFooter() == null ? null : HtmlUtils
                           .normalizeHtml(page.getTemplate().getFooter()));
      }
   }

   public static void closeHtmlTagsT(TemplateImpl templ)
   {
      if (templ == null)
      {
         return;
      }
      templ.setHeader(templ.getHeader() == null ? null : HtmlUtils
               .normalizeHtml(templ.getHeader()));
      templ.setCol1(templ.getCol1() == null ? null : HtmlUtils
               .normalizeHtml(templ.getCol1()));
      templ.setCol2(templ.getCol2() == null ? null : HtmlUtils
               .normalizeHtml(templ.getCol2()));
      templ.setCol3(templ.getCol3() == null ? null : HtmlUtils
               .normalizeHtml(templ.getCol3()));
      templ.setFooter(templ.getFooter() == null ? null : HtmlUtils
               .normalizeHtml(templ.getFooter()));
   }

}
