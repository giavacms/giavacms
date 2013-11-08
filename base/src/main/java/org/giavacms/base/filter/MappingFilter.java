package org.giavacms.base.filter;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.base.service.CacheService;
import org.giavacms.base.service.FileSystemWriterService;
import org.giavacms.common.util.BeanUtils;
import org.jboss.logging.Logger;

public class MappingFilter extends HttpServlet implements Filter
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   private static final String RESERVED_PATHS_PARAM_NAME = "reservedPaths";
   private static final String RESERVED_EXTENSIONS_PARAM_NAME = "reservedExtensions";
   public static final String PAGES_PATH_PARAM_NAME = "pagesPath";
   private static final String FACES_EXTENSION_PARAM_NAME = "facesExtension";
   private static final String PAGES_EXTENSION_PARAM_NAME = "pagesExtension";

   public static final String ORIGINAL_URI_ATTRIBUTE_NAME = "originalUri";

   private static final String SLASH_CHAR = "/";
   private static final String DOT_CHAR = ".";
   private static final String SEMICOLON_CHAR = ";";

   private FilterConfig filterConfig;
   private String[] reservedPaths;
   private String[] reservedExtensions;
   private static String pagesPath;
   private String facesExtension;
   private String pagesExtension;

   public static String getPagesPath()
   {
      return pagesPath;
   }

   public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
   {

      HttpServletRequest httpReq = (HttpServletRequest) request;
      // Get the requested URI
      String uri = httpReq.getRequestURI();

      try
      {
         logger.info("***************************");
         logger.info("MappingFilter uri: " + uri);
         logger.info("***************************");

         // Check if the URI matches mapping creteria.
         for (String reservedPath : reservedPaths)
         {
            if (uri.startsWith(reservedPath))
            {
               // no filtering here.. let's move on to the next filter in chain
               chain.doFilter(request, response);
               return;
            }
         }
         for (String reservedExtension : reservedExtensions)
         {
            if (uri.toUpperCase().endsWith(reservedExtension))
            {
               // no filtering here.. let's move on to the next filter in chain
               chain.doFilter(request, response);
               return;
            }
         }

         String originalUri = uri;
         ServletContext context = filterConfig.getServletContext();

         // Strip context path from the requested URI
         String path = context.getContextPath();
         if (uri.startsWith(path))
         {
            uri = uri.substring(path.length());
         }
         if (uri.contains(SLASH_CHAR))
         {
            uri = uri.substring(uri.lastIndexOf(SLASH_CHAR) + 1, uri.length());
         }
         // Check if there is actually a file to handle the forward.

         URL url = context.getResource(pagesPath + uri + pagesExtension);
         if (url != null)
         {
            request.setAttribute(ORIGINAL_URI_ATTRIBUTE_NAME, originalUri);

            // Generate the forward URI
            String forwardURI = pagesPath + uri + facesExtension;
            
            // Get the request dispatcher
            logger.info("***************************");
            logger.info("MappingFilter forwardURI: "
                  + forwardURI);
            logger.info("MappingFilter originalUri: "
                  + originalUri);
            logger.info("***************************");

            // Get the request dispatcher
            RequestDispatcher rd = context
                     .getRequestDispatcher(forwardURI);
            if (rd != null)
            {
               // Forward the request to FacesServlet
               rd.forward(request, response);
               return;
            }
         }

         // We are not interested for this request, pass it to the
         // FilterChain.
         chain.doFilter(request, response);

      }
      catch (ServletException sx)
      {
         filterConfig.getServletContext().log(sx.getMessage());
      }
      catch (IOException iox)
      {
         filterConfig.getServletContext().log(iox.getMessage());
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
      this.filterConfig = filterConfig;
      
      pagesPath = this.filterConfig.getServletContext().getInitParameter(PAGES_PATH_PARAM_NAME);
      if (pagesPath == null)
      {
         throw new ServletException("Missing initParameter: pagesPath");
      }
      else if (!pagesPath.endsWith(SLASH_CHAR))
      {
         pagesPath += SLASH_CHAR;
      }
      BeanUtils.getBean(FileSystemWriterService.class).setPagesPath(pagesPath);
      BeanUtils.getBean(CacheService.class).writeAll();

      String _reservedPaths = this.filterConfig.getInitParameter(RESERVED_PATHS_PARAM_NAME);
      if (_reservedPaths == null || _reservedPaths.trim().length() == 0)
      {
         reservedPaths = new String[] {};
      }
      else
      {
         reservedPaths = _reservedPaths.split(SEMICOLON_CHAR);
      }
      String _reservedExtensions = this.filterConfig.getInitParameter(RESERVED_EXTENSIONS_PARAM_NAME);
      if (_reservedExtensions == null || _reservedExtensions.trim().length() == 0)
      {
         reservedExtensions = new String[] {};
      }
      else
      {
         reservedExtensions = _reservedExtensions.split(SEMICOLON_CHAR);
      }
      pagesExtension = this.filterConfig.getInitParameter(PAGES_EXTENSION_PARAM_NAME);
      if (pagesExtension == null)
      {
         throw new ServletException("Missing initParameter: pagesExtension");
      }
      else if (!pagesExtension.startsWith(DOT_CHAR))
      {
         pagesExtension = DOT_CHAR + pagesExtension;
      }
      facesExtension = this.filterConfig.getInitParameter(FACES_EXTENSION_PARAM_NAME);
      if (facesExtension == null)
      {
         throw new ServletException("Missing initParameter: facesExtension");
      }
      else if (!facesExtension.startsWith(DOT_CHAR))
      {
         facesExtension = DOT_CHAR + facesExtension;
      }
   }

}