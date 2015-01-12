/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jboss.logging.Logger;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class ObjectUtils
{

   private static Logger logger = Logger.getLogger(ObjectUtils.class);

   // private static BASE64Encoder encode = new BASE64Encoder();
   // private static BASE64Decoder decode = new BASE64Decoder();

   public static String serialize(Object obj)
   {
      // long start=System.currentTimeMillis();
      String out = null;
      if (obj != null)
      {
         try
         {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            // out = encode.encode(baos.toByteArray());
            out = new String(baos.toByteArray());
         }
         catch (IOException e)
         {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
         }
      }
      // long end=System.currentTimeMillis();
      // logger.info("Encode:"+(end-start));
      return out;
   }

   public static Object deserialize(String str)
   {
      // long start=System.currentTimeMillis();
      Object out = null;
      if (str != null)
      {
         try
         {
            ByteArrayInputStream bios =
                     new ByteArrayInputStream(
                              // decode.decodeBuffer(str)
                              str.getBytes()
                     );
            ObjectInputStream ois = new ObjectInputStream(bios);
            out = ois.readObject();
         }
         catch (IOException e)
         {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
         }
         catch (ClassNotFoundException e)
         {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
         }
      }
      // long end=System.currentTimeMillis();
      // logger.info("Decode:"+(end-start));
      return out;
   }

}
