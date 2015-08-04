package org.giavacms.seo.filter;
//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.giavacms.base.model.pojo.Resource;
import org.giavacms.base.util.ResourceUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by fiorenzo on 03/08/15.
 */

/**
 * Servlet that makes this application crawlable
 */
@WebFilter(filterName = "CrawlServlet", urlPatterns = { "/*" })
public final class CrawlServlet implements Filter
{

   private static String rewriteQueryString(String queryString) throws UnsupportedEncodingException
   {
      StringBuilder queryStringSb = new StringBuilder(queryString);
      int i = queryStringSb.indexOf("&_escaped_fragment_");
      if (i != -1)
      {
         StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
         tmpSb.append("#!");
         tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + 20, queryStringSb.length()), "UTF-8"));
         queryStringSb = tmpSb;
      }

      i = queryStringSb.indexOf("_escaped_fragment_");
      if (i != -1)
      {
         StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
         tmpSb.append("#!");
         tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + 19, queryStringSb.length()), "UTF-8"));
         queryStringSb = tmpSb;
      }
      if (queryStringSb.indexOf("#!") != 0)
      {
         queryStringSb.insert(0, '?');
      }
      queryString = queryStringSb.toString();

      return queryString;
   }

   private FilterConfig filterConfig = null;

   /**
    * Destroys the filter configuration
    */
   public void destroy()
   {
      this.filterConfig = null;
   }

   /**
    * Filters all requests and invokes headless browser if necessary
    */
   public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException
   {
      if (filterConfig == null)
      {
         System.out.println("no filter config");
         return;
      }

      HttpServletRequest req = (HttpServletRequest) request;
      HttpServletResponse res = (HttpServletResponse) response;
      String queryString = req.getQueryString();

      StringBuilder pageNameSb = new StringBuilder("http://");
      if ((queryString != null) && (queryString.contains("_escaped_fragment_")))
      {

         pageNameSb.append(req.getServerName());
         if (req.getServerPort() != 0)
         {
            pageNameSb.append(":");
            pageNameSb.append(req.getServerPort());
         }
         pageNameSb.append(req.getRequestURI());
         queryString = rewriteQueryString(queryString);
         pageNameSb.append(queryString);
         System.out.println("pageNameSb: " + pageNameSb);
         String pageName = pageNameSb.toString();
         String lastParam = pageNameSb.substring(pageNameSb.lastIndexOf("/") + 1);
         if (lastParam == null || lastParam.trim().isEmpty())
         {
            lastParam = "home";
         }
         try
         {
            Resource resource = ResourceUtils.getFileContent("cache", lastParam + ".html");
            if (resource != null)
            {
               System.out.println("YEs, CACHE FOR: " + lastParam);
               res.setContentType("text/html;charset=UTF-8");
               PrintWriter out = res.getWriter();
               out.println(resource.getFileContent());
               out.close();
            }
            else
            {
               System.out.println("NO CACHE FOR: " + lastParam);
               try
               {
                  ((HttpServletResponse) response).sendRedirect(pageNameSb.toString());
               }
               catch (Exception e)
               {
                  e.printStackTrace();
               }
            }

         }
         catch (Exception e)
         {
            e.printStackTrace();
         }

      }
      else
      {
         //facebookexternalhit/[0-9]|Twitterbot|Pinterest|Google.*snippet
         String userAgent = req.getHeader("User-Agent");
         if (userAgent != null && !userAgent.trim().isEmpty() &&
                  (
                           userAgent.contains("facebookexternalhit") || userAgent.contains("Twitterbot")
                                    || userAgent.contains("Pinterest")
                                    ||
                                    (userAgent.contains("Google") && userAgent.contains("snippet"))
                  ))
         {
            Resource resource = null;
            try
            {
               resource = ResourceUtils.getFileContent("cache", "home.html");
               System.out.println("YEs, CACHE FOR: home.html");
               res.setContentType("text/html;charset=UTF-8");
               PrintWriter out = res.getWriter();
               out.println(resource.getFileContent());
               out.close();
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
         else
         {
            try
            {
               chain.doFilter(request, response);
            }
            catch (ServletException e)
            {
               e.printStackTrace();
            }
         }

      }
   }

   //   private void getHtml(String pageNameSb, HttpServletRequest req, HttpServletResponse res) throws Exception
   //   {
   //      final WebClient webClient = new WebClient(BrowserVersion.getDefault());
   //      //         webClient.setJavaScriptEnabled(true);
   //      String pageName = pageNameSb.toString();
   //
   //      HtmlPage page = webClient.getPage(pageName);
   //      webClient.waitForBackgroundJavaScriptStartingBefore(2000);
   //      webClient.getOptions().setUseInsecureSSL(true);
   //
   //      res.setContentType("text/html;charset=UTF-8");
   //      PrintWriter out = res.getWriter();
   //      out.println("<hr>");
   //      out.println(
   //               "<center><h3>You are viewing a non-interactive page that is intended for the crawler.  You probably want to see this page: <a href=\""
   //                        + pageName + "\">" + pageName + "</a></h3></center>");
   //      out.println("<hr>");
   //      System.out.println("PAGE XML: " + page.asXml());
   //      out.println(page.asXml());
   //      webClient.close();
   //      out.close();
   //   }

   /**
    * Initializes the filter configuration
    */
   public void init(FilterConfig filterConfig)
   {
      this.filterConfig = filterConfig;
   }

   public static void main1(String[] args) throws UnsupportedEncodingException
   {
      String we = "_escaped_fragment_=/chalet_licenseNumber/97";
      System.out.println(rewriteQueryString(we));
      String lastParam = we.substring(we.lastIndexOf("/") + 1);
      System.out.println(lastParam);
   }

   public static void main(String[] args)
   {
      String userAgent = "facebookexternalhit/1.1";
      String reg = "facebookexternalhit/[0-9]|Twitterbot|Pinterest|Google.*snippet";

   }
}
