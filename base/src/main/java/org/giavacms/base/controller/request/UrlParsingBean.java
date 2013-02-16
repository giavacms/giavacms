/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.session.PageSessionController;
import org.jboss.logging.Logger;


import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.url.QueryString;

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

   @Inject
   ParamsController paramsHandler;

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
      if (!(uri.startsWith("/s/") || uri.startsWith("/p/") || isInAdditionalParams(uri))
               || uri.endsWith("js")
               || uri.endsWith("css")
               || uri.endsWith("png")
               || uri.endsWith("gif")
               || uri.endsWith("jpg") || uri.endsWith("jpeg"))
      {
         logger.info("skip: " + uri);
         return uri;
      }

      // if (uri.startsWith("/css") || uri.startsWith("/img")
      // || uri.startsWith("/styles")) {
      // return uri;
      // }
      String contextPath = PrettyContext.getCurrentInstance()
               .getContextPath();
      logger.info("contextPath: " + contextPath);
      // parte del sito senza parametri
      // String uri =
      // PrettyContext.getCurrentInstance().getRequestURL().toURL();
      logger.info("uri: " + uri);
      // String uri = PrettyContext.getCurrentInstance().getOriginalUri();
      // requestQuery = ?
      // String uri =
      // PrettyContext.getCurrentInstance().getRequestURL().toURL();
      QueryString queryParams = PrettyContext.getCurrentInstance()
               .getRequestQueryString();
      // queryParams.getParameterMap()
      // if (uri.contains("?")) {
      // uri = uri.substring(0, uri.lastIndexOf("?"));
      // }
      // logger.info("uri: " + uri);

      if (!queryParams.isEmpty())
      {
         logger.info("start queryParams****************");
         Map<String, String[]> mappa = queryParams.getParameterMap();
         for (String key : mappa.keySet())
         {
            String[] value = mappa.get(key);
            if (value != null && value.length > 0)
            {
               paramsHandler.addParam(key, value[0]);
               logger.info(key + ": " + value[0]);
               if (key.equals("lang"))
               {
                  logger.info("attenzione potrebbe essere un cambio lingua");
               }
            }
         }
         logger.info("stop queryParams****************");

      }
      else
      {
         logger.info("NO queryParams****************");
      }

      breadCrumpsHandler.setBreadCrump(contextPath + uri);
      // #{pagesHandler.pageId}
      // logger.info("pageId:" + pageId);

      // prima di impostare la pagina
      // verifico che la lang tra i parametri sia la stessa della pagina
      // corrente, altrimenti cerco la pagina nella lingua specificata
      // altrimenti cerco la index nella lingua specificata
      if (uri.startsWith("/s/"))
      {
         pageRequestController.setWithSession(true);
         pageSessionController.getElement().setId(pageId);
         return "/pageS.xhtml";
      }
      else
      {
         pageRequestController.getElement().setId(pageId);
         return "/page.xhtml";
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
