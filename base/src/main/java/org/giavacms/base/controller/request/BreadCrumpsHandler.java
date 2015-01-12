/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.giavacms.base.model.MenuItem;


@Named
@RequestScoped
public class BreadCrumpsHandler implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String breadCrump;
   private String pageName;

   public BreadCrumpsHandler()
   {

   }

   public void setBreadCrump(String breadCrump)
   {
      this.breadCrump = breadCrump;
   }

   public String getBreadCrump()
   {
      String url = breadCrump;
      String[] crumbs = url.split("/");

      FacesContext fc = FacesContext.getCurrentInstance();
      String base = fc.getExternalContext().getRequestContextPath();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < crumbs.length; i++)
      {
         if (crumbs[i].trim().equals(""))
         {
            String label = "home";
            sb.append("<a href=\"" + base
                     + "/\" title=\"Home Page\">"
                     + label + "</a> ");
            sb.append("<span class=\"freccia\">&gt;</span> ");
         }
         else if (crumbs[i].trim().equals(base.replaceAll("/", "")))
         {
            String label = "home";
            sb.append("<a href=\"" + base + "/\" title=\"" + crumbs[i]
                     + "\">" + label + "</a> ");
            sb.append("<span class=\"freccia\">&gt;</span> ");
         }
         else if (crumbs[i].trim().equals("pagine"))
         {
            base += "/" + crumbs[i];
         }
         else if (!crumbs[i].trim().equals("pagine"))
         {
            base += "/" + crumbs[i];
            String label = crumbs[i].replaceAll("-", " ");

            sb.append("<a href=\"" + base + "\" title=\"" + crumbs[i]
                     + "\">" + label + "</a> ");
            if (i != crumbs.length - 1)
               sb.append("<span class=\"freccia\">&gt;</span> ");
         }
      }
      return sb.toString();
   }

   public List<MenuItem> getBreadCrumpItems()
   {
      String url = breadCrump;
      String[] crumbs = url.split("/");
      List<MenuItem> resultList = new ArrayList<MenuItem>();

      FacesContext fc = FacesContext.getCurrentInstance();
      String base = fc.getExternalContext().getRequestContextPath();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < crumbs.length; i++)
      {
         if (crumbs[i].trim().equals(""))
         {
            String label = "home";
            // sb.append("<a href=\"" + base
            // + "/\" title=\"Home Page del Comune di Colonnella\">"
            // + label + "</a> ");
            // sb.append("<span class=\"freccia\">&gt;</span> ");
            MenuItem mi = new MenuItem();
            mi.setName(label);
            mi.setPath(base + "/");
            resultList.add(mi);
         }
         else if (crumbs[i].trim().equals(base.replaceAll("/", "")))
         {
            String label = "home";
            // sb.append("<a href=\"" + base + "/\" title=\"" + crumbs[i]
            // + "\">" + label + "</a> ");
            // sb.append("<span class=\"freccia\">&gt;</span> ");
            MenuItem mi = new MenuItem();
            mi.setName(label);
            mi.setPath(base + "/");
            resultList.add(mi);
         }
         else if (crumbs[i].trim().equals("p"))
         {
            base += "/" + crumbs[i];
         }
         else if (!crumbs[i].trim().equals("p"))
         {
            base += "/" + crumbs[i];
            String label = crumbs[i].replaceAll("-", " ");

            sb.append("<a href=\"" + base + "\" title=\"" + crumbs[i]
                     + "\">" + label + "</a> ");
            if (i != crumbs.length - 1)
               sb.append("<span class=\"freccia\">&gt;</span> ");

            MenuItem mi = new MenuItem();
            mi.setName(label);
            mi.setPath(base);
            resultList.add(mi);
         }
      }
      return resultList;
   }

   public String getPageName()
   {
      return pageName;
   }

   public void setPageName(String pageName)
   {
      this.pageName = pageName;
   }
}
