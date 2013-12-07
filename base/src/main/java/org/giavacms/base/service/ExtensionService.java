/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import org.giavacms.base.model.Page;

@ApplicationScoped
public class ExtensionService
{

   @PersistenceContext
   EntityManager em;

   @SuppressWarnings("rawtypes")
   @Produces
   @Named
   public SelectItem[] getExtensionTypeItems()
   {
      List<SelectItem> valori = new ArrayList<SelectItem>();
      valori.add(new SelectItem(null, "tipologia..."));
      List<String> extensions = new ArrayList<String>();
      for (EntityType<?> entity : em.getMetamodel().getEntities())
      {
         Class clazz = entity.getJavaType();
         if (Page.class.isAssignableFrom(clazz))
         {
            try
            {
               String extension = ((Page) Class.forName(
                        clazz.getCanonicalName()).newInstance())
                        .getExtension();
               if (extension != null && !extensions.contains(extension))
               {
                  extensions.add(extension);
               }
            }
            catch (Exception e)
            {
            }
         }
      }
      Collections.sort(extensions);
      for (String extension : extensions)
      {
         valori.add(new SelectItem(extension));
      }
      return valori.toArray(new SelectItem[] {});
   }

}
