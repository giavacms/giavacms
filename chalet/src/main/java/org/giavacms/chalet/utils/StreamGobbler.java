package org.giavacms.chalet.utils;

/**
 * Created by fiorenzo on 20/04/15.
 */

import org.jboss.logging.Logger;

import java.io.*;

public class StreamGobbler extends Thread
{

   static Logger logger = Logger.getLogger(RuntimeUtil.class.getName());
   InputStream is;
   String type;
   OutputStream os;

   StreamGobbler(InputStream is, String type)
   {
      this(is, type, null);
   }

   StreamGobbler(InputStream is, String type, OutputStream redirect)
   {
      this.is = is;
      this.type = type;
      this.os = redirect;
   }

   public void run()
   {
      try
      {
         PrintWriter pw = null;
         if (os != null)
            pw = new PrintWriter(os);

         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);
         String line = null;
         while ((line = br.readLine()) != null)
         {
            if (pw != null)
               pw.println(line);
            logger.info(type + ">" + line);
         }
         if (pw != null)
            pw.flush();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }
}
