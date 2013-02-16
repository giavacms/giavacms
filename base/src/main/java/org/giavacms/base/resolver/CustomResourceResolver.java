/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.resolver;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.logging.Logger;

import com.sun.faces.facelets.impl.DefaultResourceResolver;

public class CustomResourceResolver extends DefaultResourceResolver
{

   Logger logger = Logger.getLogger(getClass());

   URL resourceUrl = null;

   public CustomResourceResolver()
   {
   }

   @Override
   public URL resolveUrl(String resource)
   {

      resourceUrl = super.resolveUrl(resource);
      logger.debug("url: " + resourceUrl);

      if (resourceUrl == null)
      {
         if (resource.startsWith("/"))
         {

            resource = resource.substring(1);
            logger.debug("res: " + resource);
            try
            {
               // Use real factory... hmm...should I? Don't think I do.
               resourceUrl = new URL(null, resource,
                        new DBStreamHandlerFactory()
                                 .createURLStreamHandler("db"));
            }
            catch (MalformedURLException e)
            {
               // TODO Fix exceptionhandling
               // e.printStackTrace();
            }
         }
      }
      return resourceUrl;
   }
}
