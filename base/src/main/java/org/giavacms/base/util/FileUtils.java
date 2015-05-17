package org.giavacms.base.util;

import org.jboss.logging.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author fiorenzo pizza
 */
public class FileUtils
{

   static Logger logger = Logger.getLogger(FileUtils.class.getCanonicalName());
   static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

   public static String getAbsoluteConfigurationFilename(
            ClassLoader classLoader, String holdingResource,
            String relativeFilename)
   {
      return classLoader.getResource(holdingResource).getPath() + "/"
               + relativeFilename;
   }

   public static String getExtension(String filename)
   {
      if (filename == null)
      {
         return "";
      }
      int lastDotIndex = filename.lastIndexOf(".");
      if (lastDotIndex < 0)
      {
         return "";
      }
      if (filename.length() == lastDotIndex + 1)
      {
         return "";
      }
      return filename.substring(lastDotIndex + 1);
   }

   /**
    * Read and write a file using an explicit encoding. Removing the encoding from this code will simply cause the
    * system's default encoding to be used instead.
    */
   public static boolean writeTextFile(String fileName, String content,
            String encoding)
   {
      logger.debug("Writing text " + content + " to file named " + fileName
               + (encoding == null ? "" : (". Encoding: " + encoding)));
      Writer out = null;
      boolean result = false;
      try
      {
         if (encoding == null)
         {
            out = new OutputStreamWriter(new FileOutputStream(fileName));
         }
         else
         {
            out = new OutputStreamWriter(new FileOutputStream(fileName),
                     encoding);
         }
         out.write(content);
         result = true;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
      finally
      {
         try
         {
            out.close();
         }
         catch (Exception e)
         {
         }
      }
      return result;
   }

   public static List<String> readLinesFromTextFile(String fileName,
            String encoding)
   {
      logger.debug("Reading from file named " + fileName);
      Scanner scanner = null;
      List<String> result = new ArrayList<String>();
      try
      {
         if (encoding == null)
         {
            scanner = new Scanner(new File(fileName));
         }
         else
         {
            scanner = new Scanner(new File(fileName), encoding);
         }
         while (scanner.hasNextLine())
         {
            result.add(scanner.nextLine());
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
      finally
      {
         scanner.close();
      }
      return result;
   }

   public static byte[] getBytesFromFile(File file)
   {
      InputStream is = null;
      try
      {
         is = new FileInputStream(file);
         // Get the size of the file
         long length = file.length();
         if (length > Integer.MAX_VALUE)
         {
            // File is too large
            throw new IOException("File is too large: " + file.getName());
         }
         // Create the byte array to hold the data
         byte[] bytes = new byte[(int) length];
         // Read in the bytes
         int offset = 0;
         int numRead = 0;
         while (offset < bytes.length
                  && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
         {
            offset += numRead;
         }
         // Ensure all the bytes have been read in
         if (offset < bytes.length)
         {
            throw new IOException("Could not completely read file "
                     + file.getName());
         }
         // Close the input stream and return bytes
         return bytes;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
      finally
      {
         if (is != null)
         {
            try
            {
               is.close();
            }
            catch (Throwable t)
            {
            }
         }
      }
   }

   public static String cleanName(String fileName)
   {
      fileName = fileName.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(
               "[\\s]", "-");
      return fileName.toLowerCase();
   }

   public static String clean(String fileName)
   {
      String name = getLastPartOf(fileName);
      String prefix, ext;
      if (name.lastIndexOf(".") >= 0)
      {
         prefix = name.substring(0, name.lastIndexOf("."));
         ext = name.substring(name.lastIndexOf("."));
      }
      else
      {
         prefix = name;
         ext = "";
      }
      return cleanName(prefix) + ext;
   }

   public static String getLastPartOf(String absoluteFileName)
   {
      if (absoluteFileName == null)
         return "";
      if ("".equals(absoluteFileName))
         return "";
      if (absoluteFileName.contains("\\"))
         return absoluteFileName.substring(absoluteFileName
                  .lastIndexOf("\\") + 1);
      if (absoluteFileName.contains("/"))
         return absoluteFileName
                  .substring(absoluteFileName.lastIndexOf("/") + 1);
      return absoluteFileName;
   }

   public static boolean deleteQuietly(String abs_filename)
   {
      return org.apache.commons.io.FileUtils.deleteQuietly(new File(
               abs_filename));
   }

   public static byte[] getBytesFromUrl(URL url, String username, String password)
   {
      try
      {
         URLConnection uc = url.openConnection();
         String val = (new StringBuffer(username).append(":").append(password)).toString();
         String authorizationString = "Basic " + Base64.encodeBytes(val.getBytes());
         uc.setRequestProperty("Authorization", authorizationString);
         InputStream is = uc.getInputStream();
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         byte buffer[] = new byte[1];
         while (is.read(buffer) != -1)
         {
            os.write(buffer);
         }
         return os.toByteArray();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   public static byte[] getBytesFromUrl(URL url)
   {
      InputStream is = null;
      ByteArrayOutputStream os = null;
      try
      {
         is = url.openStream();
         os = new ByteArrayOutputStream();
         byte buffer[] = new byte[1];
         while (is.read(buffer) != -1)
         {
            os.write(buffer);
         }
         return os.toByteArray();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (Exception e)
         {
         }
         try
         {
            os.close();
         }
         catch (Exception e)
         {
         }
      }
   }

   public static boolean writeBytesToFile(File file, byte[] bytes)
   {
      try
      {
         return writeBytesToOutputStream(new FileOutputStream(file), bytes);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return false;
      }
   }

   public static boolean writeBytesToOutputStream(OutputStream outputStream,
            byte[] bytes)
   {

      boolean result = false;

      BufferedInputStream input = null;
      BufferedOutputStream output = null;

      try
      {

         // Open file.
         input = new BufferedInputStream(new ByteArrayInputStream(bytes),
                  DEFAULT_BUFFER_SIZE);

         output = new BufferedOutputStream(outputStream, DEFAULT_BUFFER_SIZE);

         // Write file contents to response.
         byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
         int length;
         while ((length = input.read(buffer)) > 0)
         {
            output.write(buffer, 0, length);
         }

         // Finalize task.
         output.flush();
         result = true;

      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
      }
      finally
      {
         // Gently close streams.
         close(output);
         close(input);
      }
      return result;
   }

   public static void close(Closeable resource)
   {
      if (resource != null)
      {
         try
         {
            resource.close();
         }
         catch (IOException e)
         {
            logger.error(e.getMessage());
         }
      }
   }

}
