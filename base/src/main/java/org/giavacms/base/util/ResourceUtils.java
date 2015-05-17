/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.util;

import org.giavacms.base.enums.ResourceType;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.*;
import org.jboss.logging.Logger;

import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceUtils
{

   protected static Logger logger = Logger
            .getLogger(ResourceUtils.class.getName());

   static String beginRegExpr = "/(\\.|\\/)(";
   static String separatorRegExpr = "|";
   static String endRegExpr = ")$/";

   public static String ALL = "ALL";

   public static String getRegExpByTypes(String[] types)
   {
      StringBuffer result = new StringBuffer(separatorRegExpr);
      for (String type : types)
      {
         try
         {
            ResourceType resourceType = ResourceType.valueOf(type);
            switch (resourceType)
            {
            case ALL:
               for (ResourceType anyType : ResourceType.values())
               {
                  for (String extension : anyType.getExtensions())
                  {
                     result.append(separatorRegExpr + extension);
                     result.append(separatorRegExpr + extension.toUpperCase());
                  }
               }
               break;
            default:
               for (String extension : resourceType.getExtensions())
               {
                  result.append(separatorRegExpr + extension);
                  result.append(separatorRegExpr + extension.toUpperCase());
               }
               break;
            }
         }
         catch (Exception e)
         {
            logger.error(e.getMessage(), e);
            continue;
         }
      }
      if (result.length() > separatorRegExpr.length())
      {
         return beginRegExpr
                  + (result.toString().substring(1).concat(endRegExpr));
      }
      logger.info("nessun tipo corrispondente");
      return "";
   }

   public static List<String> getFilesName(String directory,
            List<String> extensions)
   {
      File rootDir = new File(getRealPath(directory));
      IOFileFilter filesFilter = new SuffixFileFilter(extensions, IOCase.INSENSITIVE);
      IOFileFilter notDirectory = new NotFileFilter(
               DirectoryFileFilter.INSTANCE);
      FilenameFilter fileFilter = new AndFileFilter(filesFilter, notDirectory);
      String[] resultFiles = rootDir.list(fileFilter);
      Arrays.sort(resultFiles);
      if (resultFiles.length > 0)
      {
         return Arrays.asList(resultFiles);
      }
      return new ArrayList<String>();
   }

   public static String getType(String fileName)
   {
      ResourceType resourceType = getResourceType(fileName);
      return resourceType == null ? null : resourceType.name();
   }

   public static ResourceType getResourceType(String fileName)
   {
      String type = fileName.substring(fileName.lastIndexOf(".") + 1);
      for (ResourceType resourceType : ResourceType.values())
      {
         if (resourceType.getExtensions().contains(type.toLowerCase()))
         {
            return resourceType;
         }
      }
      return null;
   }

   public static String getFolder(String fileName)
   {
      ResourceType resourceType = getResourceType(fileName);
      return resourceType == null ? null : resourceType.getFolder();
   }

   // pdf, p7m, doc, docx, xls, xlsx
   public static List<String> getPdfFiles()
   {
      return getFilesName(ResourceType.DOCUMENT.getFolder(), ResourceType.DOCUMENT.getExtensions());
   }

   public static List<String> getCssFiles()
   {
      return getFilesName(ResourceType.STYLESHEET.getFolder(), ResourceType.STYLESHEET.getExtensions());
   }

   public static List<String> getStaticFiles()
   {
      return getFilesName(ResourceType.STATIC.getFolder(), ResourceType.STATIC.getExtensions());
   }

   public static List<String> getJsFiles()
   {
      return getFilesName(ResourceType.JAVASCRIPT.getFolder(), ResourceType.JAVASCRIPT.getExtensions());
   }

   public static List<String> getImgFiles()
   {
      return getFilesName(ResourceType.IMAGE.getFolder(), ResourceType.IMAGE.getExtensions());
   }

   public static List<String> getFlashFiles()
   {
      return getFilesName(ResourceType.FLASH.getFolder(), ResourceType.FLASH.getExtensions());
   }

   public static List<String> getFontFiles()
   {
      return getFilesName(ResourceType.FONT.getFolder(), ResourceType.FONT.getExtensions());
   }

   public static String createImage_(String folder, String imageFileName,
            byte[] data)
   {
      try
      {
         String actualFileName = getUniqueName(getRealPath(folder),
                  imageFileName);
         FileImageOutputStream imageOutput;
         imageOutput = new FileImageOutputStream(new File(actualFileName));
         imageOutput.write(data, 0, data.length);
         imageOutput.close();
         return actualFileName.substring(actualFileName
                  .lastIndexOf(File.separator) + 1);
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
         return null;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public static String createFile_(String folder, String fileName, byte[] data)
   {
      try
      {
         String actualFileName = getUniqueName(getRealPath(folder),
                  fileName);
         FileOutputStream fos = new FileOutputStream(
                  new File(actualFileName));
         fos.write(data);
         fos.close();
         return actualFileName.substring(actualFileName
                  .lastIndexOf(File.separator) + 1);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public static String getUniqueName(String folder, String fileName)
   {
      String est = fileName.substring(fileName.indexOf(".") + 1);
      String nome = fileName.substring(0, fileName.indexOf(".")).replaceAll(
               " ", "");
      String finalName = fileName.replaceAll(" ", "");
      boolean trovato = false;
      int i = 0;
      while (!trovato)
      {
         logger.info("finalName: " + finalName);
         File file = new File(folder + File.separator + finalName);
         logger.info("found: " + finalName);
         if (file != null && file.exists())
         {
            i++;
            finalName = nome + "_" + i + "." + est;
         }
         else
         {
            trovato = true;
            return folder + File.separator + finalName;
         }
      }
      return folder + File.separator + finalName;
   }

   public static String getRealPath(String folderName)
   {
      // ServletContext servletContext = (ServletContext) FacesContext
      // .getCurrentInstance().getExternalContext().getContext();
      // String folder = servletContext.getRealPath("") + File.separator;
      // return folder;
      String root = new File(ResourceUtils.class.getClassLoader().getResource("angcms").getPath()).getParentFile()
               .getParentFile().getParent();
      File folder = new File(root, folderName);
      if (!folder.exists())
      {
         folder.mkdir();
      }
      return folder.getAbsolutePath();
   }

   public static String getFileContent(String fileName)
   {
      // TODO Auto-generated method stub
      return null;
   }

   public static void main(String[] args)
   {
      String type = getType("flower.png");
      logger.info(type);
   }
}
