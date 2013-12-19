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

   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/catalogue/category/list.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CategoryRepository.class)
   CategoryRepository categoryRepository;

   @Override
   public String getExtension()
   {
      return Category.EXTENSION;
   }

   @Override
   public Category getElement()
   {
      if (super.getElement() == null)
      {
         super.setElement(new Category());
      }
      return super.getElement();
   }

   // ---------------------------------------------------------------------

   @Override
   public String save()
   {
      if (super.save() == null)
      {
         return null;
      }
      setElement(new Category());
      return listPage();
   }

}
