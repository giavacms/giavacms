/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.giavacms.common.module.ModuleProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CustomerModule implements ModuleProvider
{

   Logger logger = Logger.getLogger(getClass());
   Properties permissions = null;

   @Override
   public String getName()
   {
      return "customer";
   }

   @Override
   public String getDescription()
   {
      return "Lista Clienti";
   }

   @Override
   public String getMenuFragment()
   {
      return "/private/customer/customer-menu.xhtml";
   }

   @Override
   public String getPanelFragment()
   {
      return "/private/customer/customer-panel.xhtml";
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
      list.add("gestione customer");
      return list;
   }

   @Override
   public Map<String, String> getPermissions()
   {
      Map<String, String> permissions = new HashMap<String, String>();
      permissions.put("customer", "gestione clienti");
      return permissions;
   }
}
