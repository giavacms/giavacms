package org.giavacms.mvel.servlet;

//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.giavacms.base.util.FileUtils;
import org.giavacms.base.util.RichContentUtils;
import org.giavacms.mvel.model.pojo.MvelPlaceholder;
import org.giavacms.mvel.util.MvelUtils;
import org.giavacms.mvel.util.WebTargetClosable;
import org.jboss.logging.Logger;

/**
 * Created by fiorenzo on 03/08/15.
 */

/**
 * Servlet that makes this application crawlable
 */

@WebServlet(urlPatterns = { "/mvel/*" })
public final class MvelServlet extends HttpServlet
{

   private static final String codeDefGetItem = "@code{ def getItem(path,canonicalClassName) { return org.giavacms.mvel.servlet.MvelServlet.getItem(path,canonicalClassName); } }\n\n";
   private static final String codeDefGetList = "@code{ def getList(path,canonicalClassName) { return org.giavacms.mvel.servlet.MvelServlet.getList(path,canonicalClassName); } }\n\n";
   private static final String mvelPath = "/mvel/";
   private static String host = "localhost";
   private static int port = 8080;

   private Logger logger = Logger.getLogger(getClass());

   private static final long serialVersionUID = 1L;

   private static final String separator = "\n\n-------------------------------------------------------------------------------------------------------\n\n";

   public static Object getItem(String path, String canonicalClassName)
   {
      try
      {
         return MvelUtils.getItem("http://" + host + ":" + port + "/api/v1" + path, Class.forName(canonicalClassName));
      }
      catch (Exception e)
      {
         return null;
      }
   }

   public static List getList(String path, String canonicalClassName)
   {
      try
      {
         return MvelUtils.getList("http://" + host + ":" + port + "/api/v1" + path, Class.forName(canonicalClassName));
      }
      catch (Exception e)
      {
         return null;
      }
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      String requestUrl = req.getRequestURL().toString();
      int start = requestUrl.indexOf(mvelPath) + mvelPath.length();
      String[] parts = requestUrl.substring(start).split("/");
      boolean debugMode = false;
      String debugModeParam = req.getParameter("debugMode");
      if (debugModeParam != null && !debugModeParam.trim().isEmpty())
      {
         try
         {
            debugMode = Boolean.parseBoolean(debugModeParam);
            // System.out.println(MvelUtils.getItem("http://localhost:8080/api/v1/richcontents/finalmente-on-line",
            // Class.forName("org.giavacms.richcontent.model.RichContent")));
         }
         catch (Exception e)
         {
         }
      }
      if (debugMode)
      {
         resp.getWriter().write(separator);
         resp.getWriter().write("   BEGIN OF DEBUG INFORMATION ");
         resp.getWriter().write(separator);
      }

      List<MvelPlaceholder> phvs = new ArrayList<>();
      try
      {
         if (parts.length > 0)
         {
            String jsonList = new String(FileUtils.getBytesFromUrl(new URL("http", host, port, "/api/v1/" + parts[0])));
            MvelPlaceholder phv_list = new MvelPlaceholder();
            phv_list.setName("list");
            phv_list.getValues().add(jsonList);
            phvs.add(phv_list);
            if (debugMode)
            {
               resp.getWriter().write(MvelUtils.phv2code(phv_list).toString());
               resp.getWriter().write(separator);
            }
         }
         if (parts.length > 1)
         {
            String jsonObject = new String(FileUtils.getBytesFromUrl(new URL("http", host, port, "/api/v1/" + parts[0]
                     + "/" + parts[1])));
            MvelPlaceholder phv_object = new MvelPlaceholder();
            phv_object.setName("item");
            phv_object.getValues().add(jsonObject);
            phvs.add(phv_object);
            if (debugMode)
            {
               resp.getWriter().write(MvelUtils.phv2code(phv_object).toString());
               resp.getWriter().write(separator);
            }
         }
      }
      catch (Exception e)
      {
      }

      try
      {
         String templateContent = null;
         if (parts.length == 0)
         {
            templateContent = new String(FileUtils.getBytesFromUrl(new URL("http", host, port,
                     "/static/templates/home.mvel")));
         }
         else if (parts.length == 1)
         {
            templateContent = new String(FileUtils.getBytesFromUrl(new URL("http", host, port, "/static/templates/"
                     + parts[0] + ".mvel")));
         }
         else
         {
            templateContent = new String(FileUtils.getBytesFromUrl(new URL("http", host, port, "/static/templates/"
                     + parts[0] + "-item.mvel")));
         }
         if (debugMode)
         {
            resp.getWriter().write(templateContent);
            resp.getWriter().write(separator);
         }
         if (debugMode)
         {
            resp.getWriter().write(separator);
            resp.getWriter().write("   END OF DEBUG INFORMATION ");
            resp.getWriter().write(separator);
         }

         String htmlContent = MvelUtils.compile(codeDefGetItem + codeDefGetList + templateContent, phvs);
         resp.getWriter().write(htmlContent);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         resp.getWriter().write("");
      }

   }
}
