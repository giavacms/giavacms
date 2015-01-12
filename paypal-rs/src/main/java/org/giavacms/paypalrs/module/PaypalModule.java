package org.giavacms.paypalrs.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PaypalModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "paypalrs";
   }

   @Override
   public String getDescription()
   {
      return "Vendite con Paypal RS";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/paypalrs/paypalrs-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/paypalrs/paypalrs-panel.xhtml";
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
      list.add("gestione paypal rs");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("paypalrs", "gestione paypal rs");
      return permissions;
   }

}
