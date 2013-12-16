/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.servlet;

import java.util.Hashtable;

import javax.servlet.ServletException;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.naming.resources.FileDirContext;
import org.apache.naming.resources.ProxyDirContext;

public class StaticOpenShiftServlet extends DefaultServlet
{

   private static final long serialVersionUID = 1L;

   public void init() throws ServletException
   {
      super.init();
      final Hashtable<String, Object> env = new Hashtable<String, Object>();
      env.put(ProxyDirContext.HOST, resources.getHostName());
      env.put(ProxyDirContext.CONTEXT, resources.getContextName());
      StringBuffer phisicalDir = new StringBuffer();

      String docBaseProperty = getServletConfig().getInitParameter(
               "baseFolderSystemProperty");
      log("baseFolderSystemProperty: " + docBaseProperty);
      if (docBaseProperty != null && !docBaseProperty.trim().isEmpty())
      {
         // se inizia per $ è una var di sistema
         if (docBaseProperty.trim().startsWith("$"))
         {
            String docBase = System.getenv(docBaseProperty.trim()
                     .substring(1));
            if (docBase != null && !docBase.isEmpty())
               phisicalDir.append(docBase);
         }
         else
         {
            // altrimenti la suo cosi com'è
            phisicalDir.append(docBaseProperty);
         }
      }

      String folderName = getServletConfig().getInitParameter("folderName");
      log("folderName: " + folderName);
      if (folderName != null && !folderName.trim().isEmpty())
         phisicalDir.append(folderName);

      FileDirContext context = new FileDirContext(env);
      context.setDocBase(phisicalDir.toString());
      // Load the proxy dir context.
      resources = new ProxyDirContext(env, context);
      if (super.debug > 0)
      {
         log("FileServingServlet:  docBase=" + phisicalDir.toString());
      }

   }
}
