/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.module;

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
      return "paypalweb";
   }

   @Override
   public String getDescription()
   {
      return "Vendite con Paypal Web";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/paypalweb/paypalweb-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/paypalweb/paypalweb-panel.xhtml";
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
      list.add("gestione paypal web");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("paypalweb", "gestione paypal web");
      return permissions;
   }

}
