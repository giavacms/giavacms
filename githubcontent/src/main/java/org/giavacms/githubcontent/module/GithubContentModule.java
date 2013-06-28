package org.giavacms.githubcontent.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GithubContentModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "githubcontent";
   }

   @Override
   public String getDescription()
   {
      return "Contenuti associati a github, con possibilita' di caricare una immagine";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/githubcontent/githubcontent-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/githubcontent/githubcontent-panel.xhtml";
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
      list.add("gestione contenuti associati a github");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("githubcontent", "gestione contenuti associati a github");
      return permissions;
   }

}
