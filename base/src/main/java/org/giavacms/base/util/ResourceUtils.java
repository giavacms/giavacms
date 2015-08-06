/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.util;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.*;
import org.giavacms.base.enums.ResourceType;
import org.giavacms.base.model.pojo.Resource;
import org.jboss.logging.Logger;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ResourceUtils
{

   protected static Logger logger = Logger
            .getLogger(ResourceUtils.class.getName());

   static String beginRegExpr = "/(\\.|\\/)(";
   static String separatorRegExpr = "|";
   static String endRegExpr = ")$/";

   public static String ALL = "ALL";

   static String METAINF = "META-INF";
   static String WEBINF = "WEB-INF";

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
            List<String> extensions) throws Exception
   {
      File rootDir = new File(getRealPath(directory, false));
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

   public static List<Resource> getAllFiles(String directory) throws Exception
   {
      File rootDir = new File(getRealPath(directory, false));
      if (!rootDir.exists())
         return null;
      String root = getRoot();
      File[] resultFiles = rootDir.listFiles();
      if (resultFiles != null && resultFiles.length > 0)
      {
         Arrays.sort(resultFiles);
         List<Resource> resources = new ArrayList<>();
         for (File file : resultFiles)
         {
            if (file.getName().equals(WEBINF) || file.getName().equals(METAINF))
            {
               continue;
            }
            Resource resource = new Resource(file, root);
            resources.add(resource);
         }
         return resources;
      }
      return null;
   }

   public static List<Resource> getAllFolders(String folder, List<Resource> resources) throws Exception
   {
      Collection<File> files = org.apache.commons.io.FileUtils.listFiles(
               new File(getRealPath(folder, false)),
               new RegexFileFilter("^(.*?)"),
               DirectoryFileFilter.DIRECTORY
      );

      //      File dir = new File(getRealPath(folder, false));
      //      if (resources == null)
      //      {
      //         resources = new ArrayList<>();
      //      }
      //      if (!dir.exists())
      //         return resources;
      //      //      File[] files = dir.listFiles();
      //      //      if (files == null || files.length == 0)
      //      //         return resources;
      if (files == null || files.isEmpty())
         return resources;
      for (File file : files)
      {
         if (file.getName().equals(WEBINF) || file.getName().equals(METAINF))
         {
            continue;
         }

         if (file.isDirectory())
         {
            resources.add(new Resource(file, getRoot()));
            System.out.println("Directory Name==>:" + file.getCanonicalPath());
            getAllFolders(file.getAbsolutePath(), resources);
         }
         else
         {
            System.out.println("file Not Acess===>" + file.getCanonicalPath());
         }
      }
      return resources;
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
   public static List<String> getPdfFiles() throws Exception
   {
      return getFilesName(ResourceType.DOCUMENT.getFolder(), ResourceType.DOCUMENT.getExtensions());
   }

   public static List<String> getCssFiles() throws Exception
   {
      return getFilesName(ResourceType.STYLESHEET.getFolder(), ResourceType.STYLESHEET.getExtensions());
   }

   public static List<String> getStaticFiles() throws Exception
   {
      return getFilesName(ResourceType.STATIC.getFolder(), ResourceType.STATIC.getExtensions());
   }

   public static List<String> getJsFiles() throws Exception
   {
      return getFilesName(ResourceType.JAVASCRIPT.getFolder(), ResourceType.JAVASCRIPT.getExtensions());
   }

   public static List<String> getImgFiles() throws Exception
   {
      return getFilesName(ResourceType.IMAGE.getFolder(), ResourceType.IMAGE.getExtensions());
   }

   public static List<String> getFlashFiles() throws Exception
   {
      return getFilesName(ResourceType.FLASH.getFolder(), ResourceType.FLASH.getExtensions());
   }

   public static List<String> getFontFiles() throws Exception
   {
      return getFilesName(ResourceType.FONT.getFolder(), ResourceType.FONT.getExtensions());
   }

   public static String createImage_(String folder, String imageFileName,
            byte[] data) throws Exception
   {
      String actualFileName = getUniqueName(getRealPath(folder, true),
               imageFileName);
      FileImageOutputStream imageOutput;
      imageOutput = new FileImageOutputStream(new File(actualFileName));
      imageOutput.write(data, 0, data.length);
      imageOutput.close();
      return actualFileName.substring(actualFileName
               .lastIndexOf(File.separator) + 1);
   }

   public static String createFile_(String folder, String fileName, byte[] data) throws Exception
   {

      String actualFileName = getUniqueName(getRealPath(folder, true),
               fileName);
      FileOutputStream fos = new FileOutputStream(
               new File(actualFileName));
      fos.write(data);
      fos.close();
      return actualFileName.substring(actualFileName
               .lastIndexOf(File.separator) + 1);

   }

   public static String createAbsoluteFile(String folder, String fileName, byte[] data) throws Exception
   {

      String actualFileName = getUniqueName(getRealPath(folder, true),
               fileName);
      FileOutputStream fos = new FileOutputStream(
               new File(actualFileName));
      fos.write(data);
      fos.close();
      return actualFileName;

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

   public static String getRoot() throws Exception
   {
      // con solo persistence.xml non funziona. con META-INF/persistence.xml si
      return new File(ResourceUtils.class.getClassLoader().getResource("META-INF/persistence.xml").getPath())
               .getParentFile().getParentFile().getParentFile().getParentFile().getAbsolutePath();
   }

   public static String getRealPath(String folderName, boolean create) throws Exception
   {
      // con solo persistence.xml non funziona. con META-INF/persistence.xml si
      String root = getRoot();
      File folder = new File(root, folderName);
      if (!folder.exists() && create)
      {
         folder.mkdir();
      }
      return folder.getAbsolutePath();
   }

   public static Resource getFileContent(String folder, String fileName) throws Exception
   {
      String root = getRoot();
      String pathName = root + File.separator + folder + File.separator + fileName;
      logger.info("getFileContent - pathName: " + pathName);
      Path path = Paths.get(pathName);

      String fileContent = new String(
               java.nio.file.Files.readAllBytes(path), Charset.defaultCharset());

      Resource resource = new Resource(new File(pathName), root, fileContent);
      return resource;
   }

   public static Resource setFileContent(String folder, String fileName, String fileContent) throws Exception
   {
      String root = getRoot();
      String pathName = root + File.separator + folder + File.separator + fileName;
      logger.info("setFileContent - pathName: " + pathName);
      Path path = Paths.get(pathName);

      Files.write(path, fileContent.getBytes());

      Resource resource = new Resource(new File(pathName), root, fileContent);
      return resource;
   }

   public static Resource createFileContent(String folder, String fileName, String fileContent) throws Exception
   {
      String root = getRoot();
      String pathName = root + File.separator + folder + File.separator + fileName;
      logger.info("setFileContent - pathName: " + pathName);
      Path path = Paths.get(pathName);

      if (Files.exists(path))
      {
         throw new Exception("file exists");
      }
      Files.write(path, fileContent.getBytes());

      Resource resource = new Resource(new File(pathName), root, fileContent);
      return resource;
   }

   public static Resource createSubFolder(String folder, String subFolder) throws Exception
   {
      String root = getRoot();
      String pathName = root + File.separator + folder + File.separator + subFolder;
      logger.info("createSubFolder - pathName: " + pathName);
      Path path = Paths.get(pathName);

      if (Files.exists(path))
      {
         throw new Exception("SubFolder exists");
      }
      Files.createDirectory(path);

      Resource resource = new Resource(new File(pathName), root);
      return resource;
   }

   public static boolean deleteFileContent(String folder, String fileName) throws Exception
   {
      String root = getRoot();
      String pathName = root + File.separator + folder + File.separator + fileName;
      logger.info("setFileContent - pathName: " + pathName);
      Path path = Paths.get(pathName);

      Files.delete(path);

      return true;
   }

}
