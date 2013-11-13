package org.giavacms.paypalweb.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.giavacms.paypalweb.controller.application.IpnController;

@WebServlet(name = "", urlPatterns = { "" })
public class IPNServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   @Inject
   IpnController ipnController;

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
   {
      try
      {
         ipnController.handleIpn(request);
      }
      catch (IpnException e)
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
