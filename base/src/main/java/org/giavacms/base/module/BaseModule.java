/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;


@ApplicationScoped
public class BaseModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "base";
   }

   @Override
   public String getDescription()
   {
      return "Pagine, modelli, menu, permessi";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/base/base-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/base/base-panel.xhtml";
   }

   @Override
   public int getPriority()
   {
      return 1;
   }

   @Override
   public List<String> getAllowableOperations()
   {
      List<String> list = new ArrayList<String>();
      list.add("gestione modelli");
      list.add("gestione pagine");
      list.add("gestione menu");
      list.add("gestione lingue");
      list.add("gestione risorse");
      list.add("visualizza log operazioni");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("template", "gestione modelli");
      permissions.put("page", "gestione pagine");
      permissions.put("menu", "gestione menu");
      permissions.put("i18n", "gestione lingue");
      permissions.put("resource", "gestione risorse");
      permissions.put("log", "visualizza log operazioni");
      return permissions;
   }
}
