/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Feature;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.FeatureRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class CatalogueProducer implements Serializable
{

   Logger logger = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;

   @Inject
   private CategoryRepository categoryRepository;
   @Inject
   private FeatureRepository propertyRepository;

   @SuppressWarnings("rawtypes")
   private Map<Class, SelectItem[]> items = null;

   public CatalogueProducer()
   {
      // TODO Auto-generated constructor stub
   }

   @Produces
   @Named
   public SelectItem[] getCategoryItems()
   {
      if (items.get(Category.class) == null)
      {
         items.put(Category.class, JSFUtils.setupItems(new Search<Category>(
                  Category.class), categoryRepository, "id", "title",
                  "nessuna categoria", "seleziona categoria..."));
      }
      return items.get(Category.class);
   }

   @Produces
   @Named
   public SelectItem[] getFeatureItems()
   {
      if (items.get(Feature.class) == null)
      {
         List<String> propertyNames = propertyRepository.getRefereanceableNames();
         List<SelectItem> selectItems = new ArrayList<SelectItem>();
         selectItems.add(new SelectItem(null, "proprieta' ..."));
         for (String propertyName : propertyNames)
         {
            if (propertyName != null && propertyName.trim().length() > 0)
            {
               selectItems.add(new SelectItem(propertyName));
            }
         }
         if (selectItems.size() > 1)
         {
            items.put(Feature.class, selectItems.toArray(new SelectItem[] {}));
         }
         else
         {
            items.put(Feature.class, new SelectItem[] { new SelectItem(null, "nessuna proprieta'") });
         }
      }
      return items.get(Feature.class);
   }

   public SelectItem[] getOptions(String featureName)
   {
      List<String> options = propertyRepository.getOptions(featureName);
      List<SelectItem> selectItems = new ArrayList<SelectItem>();
      selectItems.add(new SelectItem(null, "..."));
      for (String option : options)
      {
         if (option != null && option.trim().length() > 0)
         {
            selectItems.add(new SelectItem(option));
         }
      }
      if (selectItems.size() > 1)
      {
         return selectItems.toArray(new SelectItem[] {});
      }
      else
      {
         return new SelectItem[] { new SelectItem(null, "nessuna opzione disponibile") };
      }
   }

   @SuppressWarnings("rawtypes")
   public void resetItemsForClass(Class clazz)
   {
      if (items.containsKey(clazz))
      {
         items.remove(clazz);
      }
   }

   // ==============================================================================

   @SuppressWarnings("rawtypes")
   @PostConstruct
   public void reset()
   {
      items = new HashMap<Class, SelectItem[]>();
   }

}
