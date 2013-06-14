package org.giavacms.people.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PeopleModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "people";
   }

   @Override
   public String getDescription()
   {
      return "Contenuti associati a persone, con possibilita' di caricare una immagine";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/people/people-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/people/people-panel.xhtml";
   }

   @Override
   public int getPriority()
   {
      return 20;
   }

   @Override
   public List<String> getAllowableOperations()
   {
      List<String> list = new ArrayList<String>();
      list.add("gestione contenuti associati a persone");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("people", "gestione contenuti associati a persone");
      return permissions;
   }

}
