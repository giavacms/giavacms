/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.swing.ImageIcon;

public class ImageUtils
{

   static Logger log = Logger.getLogger(ImageUtils.class.getName());
   static final long timeout = 10000;
   static final double MIN_HEIGHT = 15.42;

   public static String getRealPath()
   {
      ServletContext servletContext = (ServletContext) FacesContext
               .getCurrentInstance().getExternalContext().getContext();
      String folder = servletContext.getRealPath("") + File.separator;
      return folder;
   }

   public static Integer getImageWidthProportional(Object imageData,
            Integer maxWidth, Integer maxHeight)
   {
      if (imageData == null)
         return 0;
      return getImageSizeProportional((byte[]) imageData, maxWidth, maxHeight)[0];
   }

   public static Integer getImageWidthProportionalUrl(String url,
            Integer maxWidth, Integer maxHeight)
   {
      if (url == null)
         return 0;
      return getImageSizeProportional(getRealPath() + url, maxWidth,
               maxHeight)[0];
   }

   public static Integer getImageHeightProportional(Object imageData,
            Integer maxWidth, Integer maxHeight)
   {
      if (imageData == null)
         return 0;
      return getImageSizeProportional((byte[]) imageData, maxWidth, maxHeight)[1];
   }

   public static Integer getImageHeightProportionalUrl(String url,
            Integer maxWidth, Integer maxHeight)
   {
      if (url == null)
         return 0;
      return getImageSizeProportional(getRealPath() + url, maxWidth,
               maxHeight)[1];
   }

   public static int[] getImageSizeProportional(byte[] imageData,
            int maxWidth, int maxHeight)
   {
      ImageIcon imageIcon = new ImageIcon(imageData);
      return getImageSizeProportional(imageIcon, maxWidth, maxHeight);
   }

   public static int[] getImageSizeProportional(String url, int maxWidth,
            int maxHeight)
   {
      ImageIcon imageIcon = new ImageIcon(url);
      return getImageSizeProportional(imageIcon, maxWidth, maxHeight);
   }

   private static int[] getImageSizeProportional(ImageIcon imageIcon,
            int maxWidth, int maxHeight)
   {

      double ratioH = (double) maxHeight / imageIcon.getIconHeight();
      double ratioW = (double) maxWidth / imageIcon.getIconWidth();

      int targetWidth = imageIcon.getIconWidth();
      int targetHeight = imageIcon.getIconHeight();

      if (ratioW < ratioH)
      {
         if (ratioW < 1)
         {
            targetWidth = (int) (imageIcon.getIconWidth() * ratioW);
            targetHeight = (int) (imageIcon.getIconHeight() * ratioW);
         }
      }
      else /* if ratioH < ratioW */if (ratioH < 1)
      {
         targetWidth = (int) (imageIcon.getIconWidth() * ratioH);
         targetHeight = (int) (imageIcon.getIconHeight() * ratioH);
      }

      return new int[] { targetWidth, targetHeight };
   }

   public static byte[] resizeImage(byte[] imageData, int maxWidthOrHeight,
            String type) throws IOException
   {
      // Create an ImageIcon from the image data
      ImageIcon imageIcon = new ImageIcon(imageData);
      int width = imageIcon.getIconWidth();
      int height = imageIcon.getIconHeight();
      // log.info("imageIcon width: " + width + "  height: " + height);

      // landscape (W>H) or portrait image (W<=H)?
      boolean isPortraitImage;
      if (width <= height)
         // vertical image (portrait)
         isPortraitImage = true;
      else
         // horizontal image (landscape)
         isPortraitImage = false;

      // vertical image, i have to care about height
      if (isPortraitImage && maxWidthOrHeight > 0
               && height > maxWidthOrHeight)
      {
         // Determine the shrink ratio
         double ratio = (double) maxWidthOrHeight
                  / imageIcon.getIconHeight();
         log.info("resize ratio: " + ratio);
         width = (int) (imageIcon.getIconWidth() * ratio);
         height = maxWidthOrHeight;
         log.info("imageIcon post scale width: " + width + "  height: "
                  + height);
      }

      // horizontal image, i have to care about width
      if (!isPortraitImage && maxWidthOrHeight > 0
               && width > maxWidthOrHeight)
      {
         // Determine the shrink ratio
         double ratio = (double) maxWidthOrHeight / imageIcon.getIconWidth();
         log.info("resize ratio: " + ratio);
         height = (int) (imageIcon.getIconHeight() * ratio);
         width = maxWidthOrHeight;
         log.info("imageIcon post scale width: " + width + "  height: "
                  + height);
      }

      // Create a new empty image buffer to "draw" the resized image into
      BufferedImage bufferedResizedImage = new BufferedImage(width, height,
               BufferedImage.TYPE_INT_RGB);
      // Create a Graphics object to do the "drawing"
      Graphics2D g2d = bufferedResizedImage.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
               RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      // Draw the resized image
      g2d.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
      g2d.dispose();
      // Now our buffered image is ready
      // Encode it as a JPEG
      ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedResizedImage, type.toUpperCase(),
               encoderOutputStream);
      // QUESTE CLASSI NON GIRANO SOTTO JAVA 6
      // JPEGImageEncoder encoder =
      // JPEGCodec.createJPEGEncoder(encoderOutputStream);
      // encoder.encode(bufferedResizedImage);
      byte[] resizedImageByteArray = encoderOutputStream.toByteArray();
      return resizedImageByteArray;
   }

   public static byte[] resizeImage(byte[] imageData, int newWidth, int newHeight,
            String type) throws IOException
   {
      // Create an ImageIcon from the image data
      ImageIcon imageIcon = new ImageIcon(imageData);

      // Create a new empty image buffer to "draw" the resized image into
      BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight,
               BufferedImage.TYPE_INT_RGB);
      // Create a Graphics object to do the "drawing"
      Graphics2D g2d = bufferedResizedImage.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
               RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      // Draw the resized image
      g2d.drawImage(imageIcon.getImage(), 0, 0, newWidth, newHeight, null);
      g2d.dispose();
      // Now our buffered image is ready
      // Encode it as a JPEG
      ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedResizedImage, type.toUpperCase(),
               encoderOutputStream);
      // QUESTE CLASSI NON GIRANO SOTTO JAVA 6
      // JPEGImageEncoder encoder =
      // JPEGCodec.createJPEGEncoder(encoderOutputStream);
      // encoder.encode(bufferedResizedImage);
      byte[] resizedImageByteArray = encoderOutputStream.toByteArray();
      return resizedImageByteArray;
   }

   public static int[] getBarcodeSize(String barcodeUrl)
   {
      int[] widthAndHeight = new int[] { -1, -1 };
      try
      {
         Image image = Toolkit.getDefaultToolkit().getImage(
                  new URL(barcodeUrl));

         int width = -1;
         int height = -1;

         Observer observer = new Observer();

         long endTime = System.currentTimeMillis() + timeout;

         boolean keepWaiting = true;

         while (keepWaiting)
         {

            width = image.getWidth(observer);
            height = image.getHeight(observer);

            if (height >= 0 && width >= 0)
               keepWaiting = false;

            else
            {
               long waitTime = endTime - System.currentTimeMillis();

               if (waitTime < 0)
                  log.info("Image loading timed out.");

               else
               {
                  synchronized (observer)
                  {
                     try
                     {
                        observer.wait(waitTime);
                     }
                     catch (InterruptedException e)
                     {
                        e.printStackTrace();
                     }
                  }
                  if (observer.isImageAborted())
                  {
                     log.info("Image was aborted before production was complete.");
                  }
               }

            }
         }
         widthAndHeight[0] = width;
         widthAndHeight[1] = height;

      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
      }
      return widthAndHeight;
   }

   private static class Observer implements ImageObserver
   {

      private boolean isImageAborted;

      public synchronized boolean imageUpdate(Image img, int infoflags,
               int x, int y, int width, int height)
      {
         notifyAll();
         isImageAborted = (infoflags & ImageObserver.ABORT) > 0;
         boolean heightReady = (infoflags & ImageObserver.HEIGHT) > 0;
         boolean widthReady = (infoflags & ImageObserver.WIDTH) > 0;
         boolean keepLoading = !isImageAborted
                  && (!heightReady || !widthReady);
         return keepLoading;
      }

      public synchronized boolean isImageAborted()
      {
         return isImageAborted;
      }

   }

}
