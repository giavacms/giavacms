/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.giavacms.base.controller.session.PageSessionController;
import org.jboss.logging.Logger;

import com.ocpsoft.pretty.PrettyContext;

@Named
@RequestScoped
public class UrlParsingBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Inject
   PageRequestController pageRequestController;

   @Inject
   PageSessionController pageSessionController;

   @Inject
   BreadCrumpsHandler breadCrumpsHandler;

   protected Logger logger = Logger.getLogger(getClass().getName());

   private String idPage;

   public UrlParsingBean()
   {
   }

   private boolean isInAdditionalParams(String uri)
   {
      FacesContext ctx = FacesContext.getCurrentInstance();
      String additionalUri = ctx.getExternalContext().getInitParameter(
               "additionalUri");
      if (additionalUri != null && !additionalUri.isEmpty())
      {
         return uri.startsWith(additionalUri);
      }
      return false;
   }

   public String parseComplexUrl() throws UnsupportedEncodingException
   {
      logger.info("start uri****************");
      String uri = PrettyContext.getCurrentInstance().getRequestURL().toURL();
      logger.info("uri: " + uri);
      logger.info("stop uri****************");
      String pageId = uri.substring(uri.lastIndexOf("/") + 1);

      logger.info("start pageId****************");
      logger.info("pageId: " + pageId);
      logger.info("stop pageId****************");
      if (!(uri.startsWith("/s/") || uri.startsWith("/p/") || uri.startsWith("/c/") || isInAdditionalParams(uri))
               || uri.endsWith("js")
               || uri.endsWith("css")
               || uri.endsWith("png")
               || uri.endsWith("gif")
               || uri.endsWith("jpg") || uri.endsWith("jpeg"))
      {
         logger.info("skip: " + uri);
         return uri;
      }

      String contextPath = PrettyContext.getCurrentInstance()
               .getContextPath();
      logger.info("contextPath: " + contextPath);
      breadCrumpsHandler.setBreadCrump(contextPath + uri);

      if (uri.startsWith("/c/"))
      {
         pageRequestController.setUri(uri);
         pageRequestController.getElement().setId(pageId);
         return "/cache/" + pageId + ".xhtml";
      }
      if (uri.startsWith("/s/"))
      {
         pageRequestController.setWithSession(true);
         pageSessionController.getElement().setId(pageId);
         pageSessionController.setUri(uri);
         return "/db:" + pageId;
      }
      else if (new File(((ServletContext) FacesContext
               .getCurrentInstance().getExternalContext().getContext()).getRealPath("cache"), pageId + ".xhtml")
               .exists())
      {
         pageRequestController.setUri(uri);
         pageRequestController.getElement().setId(pageId);
         return "/cache/" + pageId + ".xhtml";
      }
      else
      {
         pageRequestController.setUri(uri);
         pageRequestController.getElement().setId(pageId);
         return "/db:" + pageId;
      }

   }

   public String getIdPage()
   {
      return idPage;
   }

   public void setIdPage(String idPage)
   {
      this.idPage = idPage;
   }
}
