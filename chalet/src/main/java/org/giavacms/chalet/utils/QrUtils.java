package org.giavacms.chalet.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiorenzo on 26/07/15.
 */
public class QrUtils
{

   static String FORMAT = "PNG";

   //content = "http://votalatua.estate"
   //new File("src/test/resources/images", "100.png")
   public static byte[] encode(String content, URL url, int width, int height) throws Exception
   {

      Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      com.google.zxing.Writer qrWriter = new QRCodeWriter();
      BitMatrix matrix = qrWriter.encode(content,
               BarcodeFormat.QR_CODE,
               width,
               height,
               hints);

      BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
      BufferedImage overlay = ImageIO.read(url);

      //Calculate the delta height and width
      int deltaHeight = image.getHeight() - overlay.getHeight();
      int deltaWidth = image.getWidth() - overlay.getWidth();

      //Draw the new image
      BufferedImage combined = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) combined.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
      g.drawImage(overlay, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

      ImageIO.write(combined, FORMAT, stream);
      return stream.toByteArray();
   }
}
