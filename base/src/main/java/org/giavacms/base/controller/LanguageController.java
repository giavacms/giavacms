/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Language;
import org.giavacms.base.producer.LanguageProducer;
import org.giavacms.base.repository.LanguageRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractController;
import org.primefaces.event.RowEditEvent;


@Named
@SessionScoped
public class LanguageController extends AbstractController<Language>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static final String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static final String LIST = "/private/language/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(LanguageRepository.class)
   LanguageRepository languageRepository;

   @Inject
   LanguageProducer languageProducer;

   @Override
   public String save()
   {
      String outcome = super.save();
      if (outcome != null)
      {
         addElement();
         languageProducer.reset();
      }
      return outcome;
   }

   public void onRowEdit(RowEditEvent ree)
   {
      Language language = (Language) ree.getObject();
      getRepository().update(language);
      languageProducer.reset();
   }

   public String delete(String id)
   {
      getRepository().delete(id);
      return listPage();
   }

   @Override
   public void refreshModel()
   {
      setModel(new ListDataModel<Language>(getRepository().getAllList()));
   }

   @Override
   public Language getElement()
   {
      Language l = super.getElement();
      if (l == null)
      {
         addElement();
         return super.getElement();
      }
      else
      {
         return l;
      }
   }

}