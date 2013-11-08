/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.resolver;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class DBStreamHandlerFactory implements URLStreamHandlerFactory
{

   public URLStreamHandler createURLStreamHandler(String protocol)
   {

      if (protocol.equalsIgnoreCase("db"))
      {
         return new DBProtocolHandler();
      }
      else
      {
         return null;
      }
   }
}
