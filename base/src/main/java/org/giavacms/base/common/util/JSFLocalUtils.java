/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.security.Principal;
import java.util.Collection;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

@SuppressWarnings("unchecked")
public class JSFLocalUtils
{

   static Logger logger = Logger.getLogger(JSFLocalUtils.class);

   @SuppressWarnings("rawtypes")
   public static <T> T getBean(Class<T> beanClass)
   {
      try
      {
         Context initCtx = new InitialContext();
         Context envCtx = (Context) initCtx.lookup("java:comp");
         BeanManager beanManager = (BeanManager) envCtx
                  .lookup("BeanManager");

         Bean phBean = (Bean) beanManager.getBeans(beanClass).iterator()
                  .next();
         CreationalContext cc = beanManager.createCreationalContext(phBean);
         T bean = (T) beanManager.getReference(phBean, beanClass, cc);
         return bean;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }

   @SuppressWarnings("rawtypes")
   public static int count(Collection collection)
   {
      return collection == null ? 0 : collection.size();
   }

   // /**
   // * @param ricerca
   // * @param ejb
   // * @param idField
   // * il nome del campo del par il cui valore è da usare come
   // * selectItem.value
   // * @param valueField
   // * il nome del campo del par il cui valore è da usare
   // * selectItem.label
   // * @param emptyMessage
   // * messaggio da mettere in caso di no risultati:
   // * selectItem(null,"nessun entity trovato...")
   // * @param labelMessage
   // * messaggio da mettere nel primo selectitem in caso di
   // * no-selezione: select(null,"scegli l'entity....")
   // * @return
   // */
   // @SuppressWarnings("rawtypes")
   // public static SelectItem[] setupItems(Search<T> ricerca, SuperSession
   // ejb,
   // String idField, String valueField, String emptyMessage,
   // String labelMessage) {
   // Class clazz = ricerca.getOggetto().getClass();
   // SelectItem[] selectItems = new SelectItem[1];
   // selectItems[0] = new SelectItem(null, emptyMessage);
   // List entities = ejb.getList(ricerca, 0, 0);
   // if (entities != null && entities.size() > 0) {
   // selectItems = new SelectItem[entities.size() + 1];
   // selectItems[0] = new SelectItem(null, labelMessage);
   // int i = 1;
   // for (Object o : entities) {
   // try {
   // Field ID_Field = clazz.getDeclaredField(idField);
   // ID_Field.setAccessible(true);
   // Field VALUE_Field = clazz.getDeclaredField(valueField);
   // VALUE_Field.setAccessible(true);
   // selectItems[i] = new SelectItem(ID_Field.get(o), ""
   // + VALUE_Field.get(o));
   // i++;
   // } catch (IllegalArgumentException e) {
   // logger.error(e.getMessage(), e);
   // } catch (IllegalAccessException e) {
   // logger.error(e.getMessage(), e);
   // } catch (SecurityException e) {
   // logger.error(e.getMessage(), e);
   // } catch (NoSuchFieldException e) {
   // logger.error(e.getMessage(), e);
   // }
   // }
   // }
   // return selectItems;
   // }

   public static String breadcrumbs()
   {
      HttpServletRequest hsr = (HttpServletRequest) FacesContext
               .getCurrentInstance().getExternalContext().getRequest();
      String url = hsr.getRequestURL().toString();
      url = url.substring("http://".length());
      if (url.indexOf("/") >= 0)
         url = url.substring(url.indexOf("/") + 1);
      String[] crumbs = url.split("/");

      String base = "/";
      StringBuffer sb = new StringBuffer();
      String label = "";
      for (int i = 0; i < crumbs.length; i++)
      {
         base += crumbs[i];
         if (i == 0)
         {
            label = "home";
            if (url.contains("/private"))
            {
               logger.info("siamo nellla sezione amministrazione!");
               base += "/";
               continue;
            }
         }
         else
         {
            label = crumbs[i];
         }
         if (label.contains("."))
         {
            label = label.substring(0, label.indexOf("."));
            sb.append("<b>" + label + "</b>");
         }
         else
         {
            sb.append("<a href=\"" + base + "\" title=\"" + crumbs[i]
                     + "\">" + label + "</a> ");
            sb.append("<span class=\"freccia\">&gt;</span> ");
         }
         base += "/";
      }
      return sb.toString();
   }

   public static Principal getPrincipal()
   {
      try
      {
         FacesContext context = FacesContext.getCurrentInstance();
         HttpServletRequest req = (HttpServletRequest) context
                  .getExternalContext().getRequest();
         Principal pr = req.getUserPrincipal();
         return pr;
      }
      catch (Exception e)
      {
         return null;
      }

   }

   public static HttpSession getHttpSession()
   {
      try
      {
         FacesContext context = FacesContext.getCurrentInstance();
         HttpSession session = (HttpSession) context.getExternalContext()
                  .getSession(false);
         return session;
      }
      catch (Exception e)
      {
         return null;
      }

   }

   // public static String getUserName() {
   // FacesContext context = FacesContext.getCurrentInstance();
   // HttpServletRequest req = (HttpServletRequest) context
   // .getExternalContext().getRequest();
   // // String rem = req.getRemoteUser();
   // // logger.info("******************************");
   // // logger.info("REM USER: " + rem);
   // Principal pr = req.getUserPrincipal();
   // // logger.info("PRINC USER: " + pr.getName());
   // // logger.info("******************************");
   //
   // if (pr == null)
   // return context.getExternalContext().getInitParameter(
   // "jflower.DEFAULT_USER");
   // return pr.getName();
   // }
   //
   // public static boolean isUserInRole(String role) {
   // FacesContext context = FacesContext.getCurrentInstance();
   // HttpServletRequest req = (HttpServletRequest) context
   // .getExternalContext().getRequest();
   // return req.isUserInRole(role);
   // }

   public static java.lang.Boolean urlContains(String nome)
   {
      try
      {
         FacesContext context = FacesContext.getCurrentInstance();
         String request_uri = context.getExternalContext().getRequestMap()
                  .get("javax.servlet.forward.request_uri").toString();
         return request_uri.contains("index") ? true : request_uri
                  .toUpperCase().indexOf(nome.toUpperCase()) >= 0;
      }
      catch (Exception e)
      {
         return false;
      }
   }

   // public static String getContextPath() {
   // FacesContext fc = FacesContext.getCurrentInstance();
   // String cp = fc.getExternalContext().getRequestContextPath();
   // return cp;
   // }
   //
   // public static String getAbsolutePath() {
   // FacesContext fc = FacesContext.getCurrentInstance();
   // HttpServletRequest httpServletRequest = (HttpServletRequest) fc
   // .getExternalContext().getRequest();
   // String scheme = httpServletRequest.getScheme();
   // String hostName = httpServletRequest.getServerName();
   // int port = httpServletRequest.getServerPort();
   // // Because this is rendered in a <div> layer, portlets for some reason
   // // need the scheme://hostname:port part of the URL prepended.
   // return scheme + "://" + hostName + ":" + port + getContextPath();
   // }

   // public static void redirect(String nameUrl) throws IOException {
   // try {
   // String url = getAbsolutePath() + nameUrl;
   // FacesContext context = FacesContext.getCurrentInstance();
   // try {
   // context.getExternalContext().redirect(url);
   // context.responseComplete();
   // } catch (Exception e) {
   // logger.info(e.getMessage());
   // }
   // } catch (Exception e) {
   // e.printStackTrace();
   // }
   // }

}
