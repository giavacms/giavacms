package org.giavacms.chalet.utils;

import org.jboss.logging.Logger;

/**
 * Created by fiorenzo on 20/04/15.
 */
public class RuntimeUtil
{

   static Logger logger = Logger.getLogger(RuntimeUtil.class.getName());

   public static boolean execute(String command)
   {
      // String command =
      // "/usr/local/bin/pdf2htmlEX --split-pages 1 "
      // + " --page-filename 123.456.7890_%03d.html --zoom 1.3 --embed cfijo "
      // + " --dest-dir /Users/fiorenzo/Desktop/BACK_PIETRAIA/cataloghi/test2/123.456.7890 "
      // + " --css-filename 123.456.7890.css "
      // + "  /Users/fiorenzo/Desktop/BACK_PIETRAIA/cataloghi/Arte-Insieme-2008.pdf";
      // {pdf2htmlEXCommand} --split-pages 1 --page-filename {page-filename} --zoom 1.3 --embed cfijo --dest-dir
      // {dest-dir} --css-filename {css-filename} {pdfFileName}
      logger.info("start of" + command);
      Process proc;
      try
      {
         proc = Runtime.getRuntime().exec(command);
         // any error message?
         StreamGobbler errorGobbler = new
                  StreamGobbler(proc.getErrorStream(), "ERROR");

         // any output?
         StreamGobbler outputGobbler = new
                  StreamGobbler(proc.getInputStream(), "OUTPUT");

         // kick them off
         errorGobbler.start();
         outputGobbler.start();
         proc.waitFor();
         // any error???
         int exitVal = proc.waitFor();
         logger.info("ExitValue: " + exitVal);
         return exitVal == 0;

      }
      catch (Throwable t)
      {
         t.printStackTrace();
         return false;
      }
      finally
      {

         logger.info("end of" + command);
      }
   }
}
