package org.giavacms.catalogue.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CatalogueModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "catalogue";
   }

   @Override
   public String getDescription()
   {
      return "Catalogo Prodotti";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/catalogue/catalogue-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/catalogue/catalogue-panel.xhtml";
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
      list.add("gestione catalogue");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("catalogue", "gestione catalogo");
      return permissions;
   }

}
