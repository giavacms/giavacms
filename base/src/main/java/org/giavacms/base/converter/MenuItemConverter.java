/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.MenuItem;
import org.giavacms.base.repository.MenuRepository;
import org.giavacms.base.repository.PageRepository;
import org.jboss.logging.Logger;


@Named
@RequestScoped
public class MenuItemConverter implements Converter
{

   Logger logger = Logger.getLogger(getClass());

   public MenuItemConverter()
   {
   }

   @Inject
   MenuRepository menuRepository;
   @Inject
   PageRepository pageRepository;

   public Object getAsObject(FacesContext facesContextImpl,
            UIComponent pickList, String menuItemAsString)
   {
      if (menuItemAsString == null || menuItemAsString.equals(""))
         return null;
      // return ObjectUtils.deserialize(menuItemAsString);
      if (menuItemAsString.indexOf("mID") >= 0)
         return menuRepository.findItem(Long.parseLong(menuItemAsString
                  .substring(menuItemAsString.indexOf("mID") + 3)));
      else if (menuItemAsString.indexOf("pID") >= 0)
      {
         return new MenuItem(pageRepository.find(menuItemAsString
                  .substring(menuItemAsString.indexOf("pID") + 3)), null);
      }
      else
      {
         return null;
      }
   }

   public String getAsString(FacesContext facesContextImpl,
            UIComponent pickList, Object menuItemAsObject)
   {
      if (menuItemAsObject == null)
         return "";
      // return ObjectUtils.serialize(menuItemAsObject);
      MenuItem mi = (MenuItem) menuItemAsObject;
      if (mi.getId() != null)
         return "mID" + mi.getId();
      else
         return "pID" + mi.getPage().getId();
   }

}
