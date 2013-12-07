/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerCategory;
import org.giavacms.customer.repository.CustomerCategoryRepository;
import org.giavacms.customer.repository.CustomerRepository;

@Named
@ApplicationScoped
public class CustomerMenuProvider implements MenuProvider
{

   @Inject
   CustomerRepository customerRepository;

   @Inject
   CustomerCategoryRepository categoryRepository;

   @Override
   public List<String> getMenuItemSources()
   {
      List<String> list = new ArrayList<String>();
      list.add("categorie");
      list.add("clienti");
      return list;
   }

   @Override
   public List<MenuValue> getMenuItemValues(String name)
   {
      if (name.compareTo("categorie") == 0)
      {
         List<CustomerCategory> list = categoryRepository.getAllList();
         List<MenuValue> menuList = new ArrayList<MenuValue>();
         if (list != null && list.size() > 0)
            for (CustomerCategory category : list)
            {
               menuList.add(new MenuValue(category.getName(), ""
                        + category.getId()));
            }
         return menuList;
      }
      else if (name.compareTo("clienti") == 0)
      {
         Search<Customer> search = new Search<Customer>(new Customer());
         List<Customer> list = customerRepository.getList(search, 0, 0);
         List<MenuValue> menuList = new ArrayList<MenuValue>();
         if (list != null && list.size() > 0)
            for (Customer customer : list)
            {
               menuList.add(new MenuValue(customer.getName(), ""
                        + customer.getId()));
            }
         return menuList;
      }
      return null;
   }

   @Override
   public String getName()
   {
      return "customer";
   }

}
