/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;

@Named
@SessionScoped
public class CategoryController extends AbstractPageController<Category>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @ListPage
   @ViewPage
   public static String LIST = "/private/catalogue/category/list.xhtml";
   @EditPage
   public static String EDIT = "/private/catalogue/category/edit.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CategoryRepository.class)
   CategoryRepository categoryRepository;

   @Override
   public String getExtension()
   {
      return Category.EXTENSION;
   }

   // ---------------------------------------------------------------------

   protected void cloneFields(Category original, Category clone)
   {
      clone.setOrderNum(original.getOrderNum());
      clone.setProducts(null);

      clone.setProp1(original.getProp1());
      clone.setProp2(original.getProp2());
      clone.setProp3(original.getProp3());
      clone.setProp4(original.getProp4());
      clone.setProp5(original.getProp5());
      clone.setProp6(original.getProp6());
      clone.setProp7(original.getProp7());
      clone.setProp8(original.getProp8());
      clone.setProp9(original.getProp9());
      clone.setProp10(original.getProp10());

      clone.setRef1(original.getRef1());
      clone.setRef2(original.getRef2());
      clone.setRef3(original.getRef3());
      clone.setRef4(original.getRef4());
      clone.setRef5(original.getRef5());
      clone.setRef6(original.getRef6());
      clone.setRef7(original.getRef7());
      clone.setRef8(original.getRef8());
      clone.setRef9(original.getRef9());
      clone.setRef10(original.getRef10());
   }

   @Override
   protected void cloneDependencies(Category original, Category clone)
   {
   }
   
   @Override
   protected void postUpdate()
   {
      // TODO Auto-generated method stub
      super.postUpdate();
   }
}
