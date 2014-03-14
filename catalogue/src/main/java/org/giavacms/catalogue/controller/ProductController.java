/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.controller;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.AbstractPageWithImagesAndDocumentsController;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CatalogueConfigurationRepository;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;

@Named
@SessionScoped
public class ProductController extends AbstractPageWithImagesAndDocumentsController<Product>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW = "/private/catalogue/view.xhtml";
   @ListPage
   public static String LIST = "/private/catalogue/list.xhtml";
   @EditPage
   public static String NEW_OR_EDIT = "/private/catalogue/edit.xhtml";

   private static final String EDIT_DOCS = "/private/catalogue/edit-documents.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(ProductRepository.class)
   ProductRepository prodottiRepository;

   @Inject
   CatalogueConfigurationRepository catalogueConfigurationRepository;

   @Inject
   CategoryRepository categoryRepository;

   @Override
   public String getExtension()
   {
      return Product.EXTENSION;
   }

   // -------------------------------------------------

   public void loadCategory()
   {
      if (getElement().getCategory().getId() == null)
      {
         getElement().setCategory(new Category());
         for (int i = 1; i <= 10; i++)
         {
            getElement().setVal(i, null);
         }
      }
      else
      {
         getElement().setCategory(categoryRepository.find(getElement().getCategory().getId()));
         for (int i = 1; i <= 10; i++)
         {
            getElement().setVal(i, null);
         }
      }
   }

   @Override
   protected List<Document> getElementDocuments()
   {
      return getElement().getDocuments();
   }

   @Override
   protected List<Image> getElementImages()
   {
      return getElement().getImages();
   }

   @Override
   public String editDocsPage()
   {
      return EDIT_DOCS;
   }

   // --------------------------------------------------------

   protected boolean cloneCurrent(String newTitle)
   {
      Product original = getElement();

      addElement();
      getElement().setClone(original.isClone());
      getElement().setContent(original.getContent());
      getElement().setDescription(original.getDescription());
      getElement().setExtended(original.isExtended());
      getElement().setExtension(original.getExtension());
      getElement().setFormerTitle(null);
      getElement().setId(null);
      getElement().setPreview(original.getPreview());
      getElement().setCategory(original.getCategory());
      getElement().setTemplate(original.getTemplate());
      getElement().setTemplateId(original.getTemplateId());
      getElement().setTitle(newTitle);

      getElement().setCode(original.getCode());
      getElement().setDimensions(original.getDimensions());
      getElement().setPreview(original.getPreview());
      getElement().setPrice(original.getPrice());
      getElement().setVat(original.getVat());
      getElement().setVal1(original.getVal1());
      getElement().setVal2(original.getVal2());
      getElement().setVal3(original.getVal3());
      getElement().setVal4(original.getVal4());
      getElement().setVal5(original.getVal5());
      getElement().setVal6(original.getVal6());
      getElement().setVal7(original.getVal7());
      getElement().setVal8(original.getVal8());
      getElement().setVal9(original.getVal9());
      getElement().setVal10(original.getVal10());

      if (save() == null)
      {
         super.addFacesMessage("Errori durante la copia dei dati.");
         return false;
      }

      List<Document> documents = original.getDocuments();
      List<Image> images = original.getImages();
      int lang = original.getLang();

      for (Document document : documents)
      {
         document.setId(null);
         getElement().addDocument(document);
      }
      for (Image image : images)
      {
         image.setId(null);
         getElement().addImage(image);
      }
      switch (lang)
      {
      case 1:
         getElement().setLang1id(getElement().getId());
         break;
      case 2:
         getElement().setLang2id(getElement().getId());
         break;
      case 3:
         getElement().setLang3id(getElement().getId());
         break;
      case 4:
         getElement().setLang4id(getElement().getId());
         break;
      case 5:
         getElement().setLang5id(getElement().getId());
         break;
      default:
         break;
      }

      if (update() == null)
      {
         return false;
      }

      return true;

   }

}
