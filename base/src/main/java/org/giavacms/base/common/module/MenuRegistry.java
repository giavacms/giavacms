/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Singleton
@ApplicationScoped
public class MenuRegistry
{

   private Map<String, MenuProvider> menuModulesMap = new HashMap<String, MenuProvider>();

   @Inject
   Instance<MenuProvider> menuProviders;

   @PostConstruct
   public void postConstruct()
   {
      for (MenuProvider provider : menuProviders)
      {
         menuModulesMap.put(provider.getName(), provider);
      }
   }

   @Named
   @Produces
   public List<MenuProvider> getMenuProvider()
   {
      try
      {
         List<MenuProvider> list = new ArrayList<MenuProvider>(
                  menuModulesMap.values());
         if (list != null)
            return list;
      }
      catch (Exception e)
      {

      }
      return new ArrayList<MenuProvider>();
   }

   @Named
   @Produces
   public SelectItem[] getMenuSourceItems()
   {
      List<MenuProvider> moduli = getMenuProvider();
      if (moduli != null)
      {
         List<SelectItem> ruoliItems = new ArrayList<SelectItem>();
         for (MenuProvider module : moduli)
         {
            List<String> source_list = module.getMenuItemSources();
            if (source_list != null && source_list.size() > 0)
            {
               for (String key : source_list)
               {
                  ruoliItems.add(new SelectItem(module.getName() + ":"
                           + key, module.getName() + "-" + key));
               }
            }
         }
         return ruoliItems.toArray(new SelectItem[] {});
      }
      return new SelectItem[] {};
   }

   public MenuProvider getMenuProvider(String moduleName)
   {
      return menuModulesMap.get(moduleName);
   }

   public List<MenuValue> getMenuValues(String moduleNameDotmenuItemSource)
   {
      if (moduleNameDotmenuItemSource.contains(":"))
      {
         String[] split = moduleNameDotmenuItemSource.split(":");
         MenuProvider menuProvider = getMenuModules().get(split[0]);
         if (menuProvider != null)
            return menuProvider.getMenuItemValues(split[1]);

      }
      return new ArrayList<MenuValue>();

   }

   public Map<String, MenuProvider> getMenuModules()
   {
      return menuModulesMap;
   }

   public void setMenuModules(Map<String, MenuProvider> menuModulesMap)
   {
      this.menuModulesMap = menuModulesMap;
   }
}