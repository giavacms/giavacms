package org.giavacms.multisite.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MultisiteModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "multisite";
   }

   @Override
   public String getDescription()
   {
      return "Multisite";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/multisite/multisite-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/multisite/multisite-panel.xhtml";
   }

   @Override
   public int getPriority()
   {
      return 10;
   }

   @Override
   public List<String> getAllowableOperations()
   {
      List<String> list = new ArrayList<String>();
      list.add("gestione messaggi");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("multisite", "gestione multisite");
      return permissions;
   }
}
