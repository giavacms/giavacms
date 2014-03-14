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

   protected boolean cloneCurrent(String newTitle)
   {
      Category original = getElement();

      addElement();
      getElement().setClone(original.isClone());
      getElement().setContent(original.getContent());

      getElement().setDescription(original.getDescription());
      getElement().setExtended(original.isExtended());
      getElement().setExtension(original.getExtension());
      getElement().setFormerTitle(null);
      getElement().setId(null);
      getElement().setTemplate(original.getTemplate());
      getElement().setTemplateId(original.getTemplateId());
      getElement().setTitle(newTitle);

      getElement().setOrderNum(original.getOrderNum());
      getElement().setProducts(null);

      getElement().setProp1(original.getProp1());
      getElement().setProp2(original.getProp2());
      getElement().setProp3(original.getProp3());
      getElement().setProp4(original.getProp4());
      getElement().setProp5(original.getProp5());
      getElement().setProp6(original.getProp6());
      getElement().setProp7(original.getProp7());
      getElement().setProp8(original.getProp8());
      getElement().setProp9(original.getProp9());
      getElement().setProp10(original.getProp10());

      getElement().setRef1(original.getRef1());
      getElement().setRef2(original.getRef2());
      getElement().setRef3(original.getRef3());
      getElement().setRef4(original.getRef4());
      getElement().setRef5(original.getRef5());
      getElement().setRef6(original.getRef6());
      getElement().setRef7(original.getRef7());
      getElement().setRef8(original.getRef8());
      getElement().setRef9(original.getRef9());
      getElement().setRef10(original.getRef10());

      if (save() == null)
      {
         super.addFacesMessage("Errori durante la copia dei dati.");
         return false;
      }

      return true;

   }

}
