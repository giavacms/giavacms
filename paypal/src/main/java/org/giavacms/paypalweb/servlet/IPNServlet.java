/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.servlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "IPNServlet", urlPatterns = { "/ipn" })
public class IPNServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   @Inject
   IpnRequestService ipnRequestService;

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
   {
      try
      {
         ipnRequestService.handleIpn(request);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      resp.sendError(500, "GET not supported");
   }
}
