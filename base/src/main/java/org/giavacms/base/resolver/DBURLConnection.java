/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.giavacms.base.controller.request.PageRequestController;
import org.giavacms.base.controller.session.PageSessionController;
import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.util.BeanUtils;
import org.jboss.logging.Logger;

public class DBURLConnection extends URLConnection
{

   private String form;
   Logger logger = Logger.getLogger(getClass());
   private String content = null;
   private Page currentPage;

   public DBURLConnection(URL u)
   {
      super(u);
   }

   @Override
   public synchronized InputStream getInputStream() throws IOException
   {
      // if (!connected)
      connect();
      if (this.content != null)
      {
         return IOUtils.toInputStream(this.content);
      }
      else
      {
         return null;
      }

   }

   public String getContentType()
   {
      return "text/html";
   }

   public synchronized void connect() throws IOException
   {
      if (true || !connected)
      {
         logger.debug("url: " + url);
         logger.debug("url file: " + url.getFile());
         if (url.getFile().contains("?"))
         {
            this.form = url.getFile().substring(0,
                     url.getFile().lastIndexOf("?"));
         }
         else
         {
            this.form = url.getFile();
         }

         if (this.form.endsWith(".xhtml"))
         {
            this.form = this.form.substring(0, this.form.length() - 6);
         }

         logger.debug("form: " + this.form);
         try
         {
            this.currentPage = BeanUtils.getBean(PageRepository.class)
                     .fetch(this.form);
            PageRequestController pageRequestController = BeanUtils
                     .getBean(PageRequestController.class);
            if (pageRequestController.isWithSession())
            {
               logger.info("WITH SESSION");
               BeanUtils.getBean(PageSessionController.class).setElement(
                        currentPage);
            }
            else
            {
               pageRequestController.setElement(currentPage);
            }

            PageUtils.generateContent(this.currentPage);
            this.content = this.currentPage.getContent();

         }
         catch (Exception e)
         {
            // e.printStackTrace();
            logger.error(e.getMessage());
         }
         this.connected = true;
      }
   }

   @Override
   public long getExpiration()
   {
      return -1l;
   }

   @Override
   public long getLastModified()
   {
      return -1l;
   }
}
