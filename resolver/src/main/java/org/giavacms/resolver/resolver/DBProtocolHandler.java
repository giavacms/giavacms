/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.resolver.resolver;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class DBProtocolHandler extends URLStreamHandler
{

   DBURLConnection dburlConnection = null;

   @Override
   protected URLConnection openConnection(URL u) throws IOException
   {
      if (dburlConnection == null)
      {
         this.dburlConnection = new DBURLConnection(u);
      }
      return dburlConnection;
   }
}
