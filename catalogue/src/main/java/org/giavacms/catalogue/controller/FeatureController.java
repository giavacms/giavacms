/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Feature;
import org.giavacms.catalogue.model.Feature;
import org.giavacms.catalogue.repository.FeatureRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.primefaces.event.RowEditEvent;

@Named
@SessionScoped
public class FeatureController extends AbstractLazyController<Feature>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/catalogue/property/list.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(FeatureRepository.class)
   FeatureRepository propertyRepository;

   // --------------------------------------------------------

   @Override
   public Feature getElement()
   {
      if (super.getElement() == null)
      {
         super.setElement(new Feature());
      }
      return super.getElement();
   }

   // ---------------------------------------------------------------------

   @Override
   public String save()
   {
      boolean ok = true;
      if (getElement().getName() == null || getElement().getName().trim().isEmpty())
      {
         super.addFacesMessage("Nome della proprieta' non specificato");
         ok = false;
      }
      if (getElement().getOption() == null || getElement().getOption().trim().isEmpty())
      {
         super.addFacesMessage("Nome dell'opzione non specificato");
         ok = false;
      }
      if (!ok)
      {
         return null;
      }
      getElement().setName(getElement().getName().trim());
      getElement().setOption(getElement().getOption().trim());
      for (String option : propertyRepository.getOptions(getElement().getName().trim()))
      {
         if (option != null && option.trim().equals(getElement().getOption()))
         {
            super.addFacesMessage("Opzione gia' presente");
            ok = false;
            break;
         }
      }
      if (!ok)
      {
         return null;
      }
      if (super.save() == null)
      {
         return null;
      }
      setElement(new Feature());
      return listPage();
   }

   @Override
   public void onRowEdit(RowEditEvent ree)
   {
      Feature property = (Feature) ree.getObject();
      if (property.getOptionOnDb().equals(property.getOption()))
      {
         return;
      }
      property.setName(property.getName().trim());
      property.setOption(property.getOption().trim());
      for (String option : propertyRepository.getOptions(property.getName().trim()))
      {
         if (option != null && option.trim().equals(property.getOption()))
         {
            super.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Opzione gia' presente", "", "featuresList");
            property.setOption(property.getOptionOnDb());
            return;
         }
      }
      super.onRowEdit(ree);
   }

}
