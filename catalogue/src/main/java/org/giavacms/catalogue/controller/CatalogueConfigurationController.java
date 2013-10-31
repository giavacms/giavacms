package org.giavacms.catalogue.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.CatalogueConfiguration;
import org.giavacms.catalogue.repository.CatalogueConfigurationRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;

@Named
@SessionScoped
public class CatalogueConfigurationController extends
         AbstractLazyController<CatalogueConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/catalogue/configuration.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CatalogueConfigurationRepository.class)
   CatalogueConfigurationRepository catalogueConfigurationRepository;

   @Override
   public CatalogueConfiguration getElement()
   {
      if (super.getElement() == null)
         setElement(catalogueConfigurationRepository.load());
      return super.getElement();
   }

   @Override
   public String update()
   {
      if (super.update() == null)
      {
         return null;
      }
      return backPage();
   }

   @Override
   public String reset()
   {
      super.reset();
      return backPage();
   }

}
