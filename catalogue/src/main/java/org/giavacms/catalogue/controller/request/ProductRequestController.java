/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageRequestController;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.JSFUtils;

@Named
@RequestScoped
public class ProductRequestController extends
         AbstractPageRequestController<Product> implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam(CATEGORY_PARAM)
   String category;

   @Inject
   @HttpParam(CONTENT_PARAM)
   String content;

   @Inject
   @HttpParam(TYPE_PARAM)
   String type;

   public static final String CURRENT_PAGE_PARAM = "p";
   public static final String CONTENT_PARAM = "q";
   public static final String TYPE_PARAM = "t";
   public static final String CATEGORY_PARAM = "categoria";

   public static final List<String> wellKnownParams = Arrays.asList(CURRENT_PAGE_PARAM, CONTENT_PARAM, TYPE_PARAM,
            CATEGORY_PARAM);

   @Inject
   @OwnRepository(ProductRepository.class)
   ProductRepository productRepository;

   @Inject
   CategoryRepository categoryRepository;

   @Override
   public String getCurrentPageParam()
   {
      return CURRENT_PAGE_PARAM;
   }

   @Override
   protected void initSearch()
   {
      getSearch().getObj().setTitle(content);
      getSearch().getObj().getCategory().setTitle(category);
      Map<String, String[]> parametersMap = JSFUtils.getParameters();
      Map<String, String[]> valsMap = new HashMap<String, String[]>();
      for (String parameter : parametersMap.keySet())
      {
         if (!wellKnownParams.contains(parameter))
         {
            valsMap.put(parameter, parametersMap.get(parameter));
         }
      }
      if (valsMap.size() > 0)
      {
         getSearch().getObj().setVals(valsMap);
      }
      super.initSearch();
   }

   public boolean isScheda()
   {
      return getElement() != null && getElement().getId() != null;
   }

   public List<String> getProductCategories()
   {
      Search<Category> r = new Search<Category>(Category.class);
      List<Category> list = categoryRepository.getList(r, 0, 0);
      List<String> l = new ArrayList<String>();
      for (Category rnt : list)
      {
         l.add(rnt.getTitle());
      }
      return l;
   }

   public List<Category> getAllCategories()
   {
      Search<Category> r = new Search<Category>(Category.class);
      List<Category> l = categoryRepository.getList(r, 0, 0);
      return l;
   }

   @Override
   protected void handleI18N()
   {
      // @see example in RichContentRequestController
   }
}
