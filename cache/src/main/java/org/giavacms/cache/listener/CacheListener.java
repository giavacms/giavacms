/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.cache.listener;

import java.io.File;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.FileUtils;
import org.giavacms.common.filter.MappingFilter;
import org.giavacms.rewriter.service.CacheService;
import org.jboss.logging.Logger;

@WebListener
public class CacheListener implements ServletContextListener
{

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   CacheService cacheService;

   File backupDir = null;
   File cacheDir = null;

   @Override
   public void contextInitialized(ServletContextEvent sce)
   {

      StringBuffer physicalDir = new StringBuffer();

      String docBaseProperty = sce.getServletContext().getInitParameter(
               "baseFolderSystemProperty");

      logger.info("baseFolderSystemProperty: " + docBaseProperty);

      if (docBaseProperty != null && !docBaseProperty.trim().isEmpty())
      {
         // se inizia per $ è una var di sistema
         if (docBaseProperty.trim().startsWith("$"))
         {
            String docBase = System.getenv(docBaseProperty.trim()
                     .substring(1));
            if (docBase != null && !docBase.isEmpty())
               physicalDir.append(docBase);
         }
         else
         {
            // altrimenti la suo cosi com'è
            physicalDir.append(docBaseProperty);
         }
      }

      String pagesPath = sce.getServletContext().getInitParameter(MappingFilter.PAGES_PATH_PARAM_NAME);

      physicalDir.append(pagesPath);

      backupDir = new File(physicalDir.toString());

      boolean backupRestored = false;
      if (backupDir.exists() && backupDir.isDirectory())
      {
         try
         {
            cacheDir = cacheService.getAbsolutePath(pagesPath);
            if (cacheDir.exists())
            {
               cacheDir.delete();
            }
            FileUtils.moveDirectory(backupDir, cacheDir);
            logger.info("Pages backup restored from " + backupDir.getAbsolutePath() + " --> "
                     + cacheDir.getAbsolutePath());
            backupRestored = true;
         }
         catch (Exception e)
         {
            logger.error("Pages backup NOT restored from " + backupDir.getAbsolutePath());
            logger.error(e.getMessage(), e);
         }
      }

      if (!backupRestored)
      {
         cacheService.writeAll(pagesPath);
         logger.warn("Pages backup NOT restored; brand new pages cache saved to " + pagesPath);
      }
      try
      {
         cacheDir = cacheService.getAbsolutePath(pagesPath);
      }
      catch (Exception e)
      {
      }

   }

   @Override
   public void contextDestroyed(ServletContextEvent arg0)
   {
      logger.warn("contextDestroyed");
      preDestroy();
   }

   @PreDestroy
   public void preDestroy()
   {
      logger.warn("preDestroy");
      if (backupDir == null || cacheDir == null)
      {
         return;
      }
      if (!cacheDir.exists())
      {
         return;
      }
      if (backupDir.exists())
      {
         backupDir.delete();
      }
      try
      {
         FileUtils.moveDirectory(cacheDir, backupDir);
         logger.info("Pages backup saved from " + cacheDir.getAbsolutePath() + " --> " + backupDir.getAbsolutePath());
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
   }
}