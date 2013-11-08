package org.giavacms.base.view;

import java.util.List;
import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.base.filter.MappingFilter;
import org.jboss.logging.Logger;

public class MappingViewHandler extends ViewHandlerWrapper
{
   private static final String ROOT_PATH = "/";

   Logger logger = Logger.getLogger(getClass());

   private ViewHandler wrappedHandler;

   public MappingViewHandler(ViewHandler defaultHandler)
   {
      this.wrappedHandler = defaultHandler;
   }

   @Override
   public ViewHandler getWrapped()
   {
      return wrappedHandler;
   }

   /**
    * This is the only method needed to be extended. First, we get the normal URL form the original ViewHandler. Then we
    * simply return the same URL with the extension stripped of.
    */
   public String getActionURL(FacesContext context, String viewId)
   {
      HttpServletRequest httpServletRequest = (HttpServletRequest) context
               .getExternalContext().getRequest();
      Object originalUri = httpServletRequest.getAttribute(MappingFilter.ORIGINAL_URI_ATTRIBUTE_NAME);
      if (originalUri != null && !originalUri.toString().isEmpty())
      {
         return originalUri.toString();
      }
      else
      {
         return getWrapped().getActionURL(context, viewId);
      }
   }

   @Override
   public String getRedirectURL(FacesContext paramFacesContext,
            String paramString, Map<String, List<String>> paramMap,
            boolean paramBoolean)
   {
      logger.info("getRedirectURL: " + paramString);
      paramString = paramString.replace(MappingFilter.getPagesPath(), ROOT_PATH);

      if (paramString != null && !paramString.trim().isEmpty())
      {
         int dotIdx = paramString.lastIndexOf(".");
         if (dotIdx > 0)
         {
            paramString = paramString.substring(0, dotIdx);
         }
         logger.info("getRedirectURL rewrite:" + paramString);
         HttpServletRequest httpServletRequest = (HttpServletRequest) paramFacesContext
                  .getExternalContext().getRequest();
         httpServletRequest.setAttribute(MappingFilter.ORIGINAL_URI_ATTRIBUTE_NAME, paramString);
      }
      return super.getRedirectURL(paramFacesContext, paramString, paramMap,
               paramBoolean);
   }

}
