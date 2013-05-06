package org.giavacms.catalogue.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageController;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateImplRepository;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.producer.CatalogueProducer;
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

   @Inject
   CatalogueProducer catalogueProducer;

   @Inject
   TemplateImplRepository templateImplRepository;

   @Inject
   PageRepository pageRepository;

   @Override
   public String getExtension()
   {
      return Category.EXTENSION;
   }

   @Override
   public void initController()
   {
      if (getElement() == null)
      {
         setElement(new Category());
      }
   }

   @Override
   public String reset()
   {
      catalogueProducer.reset();
      return super.reset();
   }

   // ---------------------------------------------------------------------

   @Override
   public String save()
   {
      catalogueProducer.reset();
      if (super.save() == null)
      {
         return null;
      }
      setElement(new Category());
      return listPage();
   }

}
