/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.model.Search;

@Named
@ApplicationScoped
public class CatalogueMenuProvider implements MenuProvider
{

   @Inject
   ProductRepository productRepository;

   @Inject
   CategoryRepository categoryRepository;

   @Override
   public List<String> getMenuItemSources()
   {
      List<String> list = new ArrayList<String>();
      list.add("categorie");
      list.add("prodotti");
      return list;
   }

   @Override
   public List<MenuValue> getMenuItemValues(String name)
   {
      if (name.compareTo("categorie") == 0)
      {
         List<Category> list = categoryRepository.getAllList();
         List<MenuValue> menuList = new ArrayList<MenuValue>();
         if (list != null && list.size() > 0)
            for (Category category : list)
            {
               menuList.add(new MenuValue(category.getTitle(), ""
                        + category.getId()));
            }
         return menuList;
      }
      else if (name.compareTo("prodotti") == 0)
      {
         Search<Product> search = new Search<Product>(new Product());
         List<Product> list = productRepository.getList(search, 0, 0);
         List<MenuValue> menuList = new ArrayList<MenuValue>();
         if (list != null && list.size() > 0)
            for (Product product : list)
            {
               menuList.add(new MenuValue(product.getTitle(), ""
                        + product.getId()));
            }
         return menuList;
      }
      return null;
   }

   @Override
   public String getName()
   {
      return "catalogue";
   }

}
