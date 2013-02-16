/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.util.TemplateUtils;
import org.giavacms.base.model.Template;
import org.giavacms.base.producer.BaseProducer;
import org.giavacms.base.repository.TemplateRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;


@Named
@SessionScoped
public class TemplateController extends AbstractLazyController<Template>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static final String BACK = "/private/administration.xhtml";
   @ViewPage
   public static final String VIEW = "/private/template/view.xhtml";
   @ListPage
   public static final String LIST = "/private/template/list.xhtml";
   @EditPage
   public static final String NEW_OR_EDIT = "/private/template/edit.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(TemplateRepository.class)
   TemplateRepository templateRepository;

   @Inject
   BaseProducer baseProducer;

   // -----------------------------------------------------

   public String save()
   {
      super.save();
      baseProducer.resetItemsForClass(Template.class);
      return viewPage();
   }

   public String update()
   {
      // recupero dati in input
      // nelle sottoclassi!! ovverride!
      // salvataggio
      super.update();
      // altre dipendenze
      baseProducer.resetItemsForClass(Template.class);
      return viewPage();
   }

   public String delete()
   {
      super.delete();
      // altre dipendenze
      baseProducer.resetItemsForClass(Template.class);
      // visat di destinazione
      return listPage();
   }

   public String reallyDelete()
   {
      templateRepository.reallyDelete(getElement().getId());
      return listPage();
   }

   public String cloneElement()
   {
      Template original = (Template) getModel().getRowData();
      original = getRepository().fetch(original.getId());
      setEditMode(true);
      setReadOnlyMode(false);
      setElement(TemplateUtils.clone(original));
      return editPage();
   }

}