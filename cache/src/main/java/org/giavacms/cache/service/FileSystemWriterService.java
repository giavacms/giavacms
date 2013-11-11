package org.giavacms.cache.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.util.FileUtils;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class FileSystemWriterService implements Serializable
{

   private static final long serialVersionUID = 1L;

   private static final boolean SINGLE_FILE_FOR_ALL_PAGES = true;

   public static final String XHTML_EXTENSION = ".xhtml";
   public static final String TEMPLATE_PREFIX = "_template_";
   public static final String TEMPLATE_IMPL_PREFIX = "_templateImpl_";
   public static final String TEMPLATE_IMPL_HEADER_SUFFIX = "_header";
   public static final String TEMPLATE_IMPL_COLUMN1_SUFFIX = "_column1";
   public static final String TEMPLATE_IMPL_COLUMN2_SUFFIX = "_column2";
   public static final String TEMPLATE_IMPL_COLUMN3_SUFFIX = "_column3";
   public static final String TEMPLATE_IMPL_FOOTER_SUFFIX = "_footer";
   public static final String PAGE_PREFIX = "";// "_page_";
   public static final String DOCTYPE_PROLOGUE_START = "<!DOCTYPE";
   public static final String NEWLINE = "\n";

   public static final String FACELETS_XMLNS = "xmlns:ui=\"http://java.sun.com/jsf/facelets\"";
   public static final String XML_PROLOGUE = "<?xml version=\"1.0\"?>";

   public static final String TEMPLATE_HEADER_INSERT = "<ui:insert name=\"header\"></ui:insert>";
   public static final String TEMPLATE_COLUMN1_INSERT = "<ui:insert name=\"column1\"></ui:insert>";
   public static final String TEMPLATE_COLUMN2_INSERT = "<ui:insert name=\"column2\"></ui:insert>";
   public static final String TEMPLATE_COLUMN3_INSERT = "<ui:insert name=\"column3\"></ui:insert>";
   public static final String TEMPLATE_FOOTER_INSERT = "<ui:insert name=\"footer\"></ui:insert>";

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   public String write(File absolutePath, Template template, boolean overwrite) throws Exception
   {

      File templateFile = new File(absolutePath, TEMPLATE_PREFIX + template.getId() + XHTML_EXTENSION);
      if (templateFile.exists() && !overwrite)
      {
         return null;
      }

      StringBuffer sb = new StringBuffer();

      // FACELETS SUPPORT
      if (!isFaceletsCompliant(template))
      {
         throw new Exception("Template must start with " + DOCTYPE_PROLOGUE_START
                  + " and contain facelets namespace declaration: " + FACELETS_XMLNS);
      }

      // HEADER
      if (template.getHeader_start() != null && template.getHeader_start().trim().length() > 0)
      {
         sb.append(template.getHeader_start()).append(NEWLINE);
         if (template.getHeader_stop() != null && template.getHeader_stop().length() > 0)
         {
            sb.append(TEMPLATE_HEADER_INSERT).append(NEWLINE);
            sb.append(template.getHeader_stop()).append(NEWLINE);
         }
      }

      // COLUMN1
      if (template.getCol1_start() != null && template.getCol1_start().trim().length() > 0)
      {
         sb.append(template.getCol1_start()).append(NEWLINE);
         if (template.getCol1_stop() != null && template.getCol1_stop().length() > 0)
         {
            sb.append(TEMPLATE_COLUMN1_INSERT).append(NEWLINE);
            sb.append(template.getCol1_stop()).append(NEWLINE);
         }
      }

      // COLUMN2
      if (template.getCol2_start() != null && template.getCol2_start().trim().length() > 0)
      {
         sb.append(template.getCol2_start()).append(NEWLINE);
         if (template.getCol2_stop() != null && template.getCol2_stop().length() > 0)
         {
            sb.append(TEMPLATE_COLUMN2_INSERT).append(NEWLINE);
            sb.append(template.getCol2_stop()).append(NEWLINE);
         }
      }

      // COLUMN3
      if (template.getCol3_start() != null && template.getCol3_start().trim().length() > 0)
      {
         sb.append(template.getCol3_start()).append(NEWLINE);
         if (template.getCol3_stop() != null && template.getCol3_stop().length() > 0)
         {
            sb.append(TEMPLATE_COLUMN3_INSERT).append(NEWLINE);
            sb.append(template.getCol3_stop()).append(NEWLINE);
         }
      }

      // FOOTER
      if (template.getFooter_start() != null && template.getFooter_start().trim().length() > 0)
      {
         sb.append(template.getFooter_start()).append(NEWLINE);
         if (template.getFooter_stop() != null && template.getFooter_stop().length() > 0)
         {
            sb.append(TEMPLATE_FOOTER_INSERT).append(NEWLINE);
            sb.append(template.getFooter_stop()).append(NEWLINE);
         }
      }

      write(templateFile, sb.toString());
      return templateFile.getAbsolutePath();
   }

   private void write(File file, String content) throws Exception
   {
      if (file.exists())
      {
         if (!file.delete())
         {
            throw new Exception("Failed to delete: " + file.getAbsolutePath());
         }
      }
      if (!FileUtils.writeTextFile(file.getAbsolutePath(), content, null))
      {
         throw new Exception("Failed to write: " + file.getAbsolutePath());
      }
   }

   protected List<String> write(File absolutePath, TemplateImpl templateImpl, Set<String> xmlnsSet, boolean overwrite)
            throws Exception
   {
      StringBuffer opening = new StringBuffer("<ui:composition ");
      for (String xmlns : xmlnsSet)
      {
         opening.append(xmlns).append(" ");
      }
      opening.append(">");
      String closure = "</ui:composition>";
      List<String> files = new ArrayList<String>();
      if (templateImpl.getHeader() != null)
      {
         File templateImplHeaderFile = new File(absolutePath, TEMPLATE_IMPL_PREFIX + templateImpl.getId()
                  + TEMPLATE_IMPL_HEADER_SUFFIX + XHTML_EXTENSION);
         if (!templateImplHeaderFile.exists() || overwrite)
         {
            write(templateImplHeaderFile, opening + templateImpl.getHeader() + closure);
            files.add(templateImplHeaderFile.getAbsolutePath());
         }
      }
      if (templateImpl.getCol1() != null)
      {
         File templateImplCol1File = new File(absolutePath, TEMPLATE_IMPL_PREFIX + templateImpl.getId()
                  + TEMPLATE_IMPL_COLUMN1_SUFFIX + XHTML_EXTENSION);
         if (!templateImplCol1File.exists() || overwrite)
         {
            write(templateImplCol1File, opening + templateImpl.getCol1() + closure);
            files.add(templateImplCol1File.getAbsolutePath());
         }
      }
      if (templateImpl.getCol2() != null)
      {
         File templateImplCol2File = new File(absolutePath, TEMPLATE_IMPL_PREFIX + templateImpl.getId()
                  + TEMPLATE_IMPL_COLUMN2_SUFFIX + XHTML_EXTENSION);
         if (!templateImplCol2File.exists() || overwrite)
         {
            write(templateImplCol2File, opening + templateImpl.getCol2() + closure);
            files.add(templateImplCol2File.getAbsolutePath());
         }
      }
      if (templateImpl.getCol3() != null)
      {
         File templateImplCol3File = new File(absolutePath, TEMPLATE_IMPL_PREFIX + templateImpl.getId()
                  + TEMPLATE_IMPL_COLUMN3_SUFFIX + XHTML_EXTENSION);
         if (!templateImplCol3File.exists() || overwrite)
         {
            write(templateImplCol3File, opening + templateImpl.getCol3() + closure);
            files.add(templateImplCol3File.getAbsolutePath());
         }
      }
      if (templateImpl.getFooter() != null)
      {
         File templateImplFooterFile = new File(absolutePath, TEMPLATE_IMPL_PREFIX + templateImpl.getId()
                  + TEMPLATE_IMPL_FOOTER_SUFFIX + XHTML_EXTENSION);
         if (!templateImplFooterFile.exists() || overwrite)
         {
            write(templateImplFooterFile, opening + templateImpl.getFooter() + closure);
            files.add(templateImplFooterFile.getAbsolutePath());
         }
      }
      return files;
   }

   protected String write(File absolutePath, Page page) throws Exception
   {

      StringBuffer sb = new StringBuffer();
      sb.append(
               "<ui:composition template=\"" + TEMPLATE_PREFIX + page.getTemplate().getTemplate().getId()
                        + XHTML_EXTENSION + "\" xmlns:ui=\"http://java.sun.com/jsf/facelets\">"
               ).append(NEWLINE);
      if (page.getTemplate().getHeader() != null)
      {
         sb.append("<ui:define name=\"header\"><ui:include src=\"" + TEMPLATE_IMPL_PREFIX + page.getTemplate().getId()
                  + TEMPLATE_IMPL_HEADER_SUFFIX + XHTML_EXTENSION + "\"/></ui:define>").append(NEWLINE);
      }
      if (page.getTemplate().getCol1() != null)
      {
         sb.append("<ui:define name=\"header\"><ui:include src=\"" + TEMPLATE_IMPL_PREFIX + page.getTemplate().getId()
                  + TEMPLATE_IMPL_COLUMN1_SUFFIX + XHTML_EXTENSION + "\"/></ui:define>").append(NEWLINE);
      }
      if (page.getTemplate().getCol2() != null)
      {
         sb.append("<ui:define name=\"header\"><ui:include src=\"" + TEMPLATE_IMPL_PREFIX + page.getTemplate().getId()
                  + TEMPLATE_IMPL_COLUMN2_SUFFIX + XHTML_EXTENSION + "\"/></ui:define>").append(NEWLINE);
      }
      if (page.getTemplate().getCol3() != null)
      {
         sb.append("<ui:define name=\"header\"><ui:include src=\"" + TEMPLATE_IMPL_PREFIX + page.getTemplate().getId()
                  + TEMPLATE_IMPL_COLUMN3_SUFFIX + XHTML_EXTENSION + "\"/></ui:define>").append(NEWLINE);
      }
      if (page.getTemplate().getFooter() != null)
      {
         sb.append("<ui:define name=\"header\"><ui:include src=\"" + TEMPLATE_IMPL_PREFIX + page.getTemplate().getId()
                  + TEMPLATE_IMPL_FOOTER_SUFFIX + XHTML_EXTENSION + "\"/></ui:define>").append(NEWLINE);
      }
      sb.append("</ui:composition>");

      File pageFile = new File(absolutePath, PAGE_PREFIX + page.getId()
               + XHTML_EXTENSION);
      write(pageFile, sb.toString());
      return pageFile.getAbsolutePath();
   }

   public List<String> write(File absolutePath, Page page, boolean overwrite) throws Exception
   {

      if (isFaceletsCompliant(page.getTemplate().getTemplate()))
      {
         Set<String> xmlnsSet = getXmlns(page.getTemplate().getTemplate().getHeader_start());
         List<String> files = write(absolutePath, page.getTemplate(), xmlnsSet, overwrite);
         boolean overwriteTemplate = false;
         String templateFile = write(absolutePath, page.getTemplate().getTemplate(), overwriteTemplate);
         if (templateFile != null)
         {
            files.add(templateFile);
         }
         files.add(write(absolutePath, page));
         return files;
      }
      else
      {
         logger.warn("Page '" + page.getId() + "' is not facelets-compliant");
         PageUtils.generateContent(page);
         File pageFile = new File(absolutePath, PAGE_PREFIX + page.getId()
                  + XHTML_EXTENSION);
         write(pageFile, page.getContent());
         return Arrays.asList("(" + pageFile.getAbsolutePath() + ")");
      }
   }

   public String clear(File absolutePath, String filename) throws Exception
   {
      List<String> files = new ArrayList<String>();
      for (File file : absolutePath.listFiles())
      {
         if (!file.getName().endsWith("xhtml"))
         {
            continue;
         }
         if (filename != null && filename.trim().length() > 0 && !file.getName().equals(filename + ".xhtml"))
         {
            continue;
         }
         if (!file.delete())
         {
            throw new Exception("Failed to delete: " + file.getAbsolutePath());
         }
         else
         {
            files.add(file.getName());
         }
      }
      return "Deleted: " + files.toString();
   }

   public String clearAll(File absolutePath) throws Exception
   {
      return clear(absolutePath, null);
   }

   private Set<String> getXmlns(String header)
   {
      Set<String> set = new HashSet<String>();
      set.add(FACELETS_XMLNS);
      String xmlns = "xmlns:";
      String quote = "\"";
      int xmlnsIndex = header.indexOf(xmlns);
      while (xmlnsIndex >= 0)
      {
         String declaration = header.substring(xmlnsIndex);
         int quoteIndex = declaration.indexOf(quote);
         quoteIndex = declaration.indexOf(quote, quoteIndex + 1);
         set.add(declaration.substring(0, quoteIndex + 1));
         header = header.substring(xmlnsIndex + quoteIndex);
         xmlnsIndex = header.indexOf(xmlns);
      }
      return set;
   }

   private boolean isFaceletsCompliant(Template template)
   {
      if (SINGLE_FILE_FOR_ALL_PAGES)
      {
         return false;
      }
      else if (template.getHeader_start() == null || !template.getHeader_start().startsWith(DOCTYPE_PROLOGUE_START)
               || !template.getHeader_start().contains(FACELETS_XMLNS))
      {
         return false;
      }
      else
      {
         return true;
      }
   }

}