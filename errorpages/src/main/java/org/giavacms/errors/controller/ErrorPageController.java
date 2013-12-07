/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.errors.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractController;
import org.giavacms.errors.model.ErrorPage;
import org.giavacms.errors.repository.ErrorPageRepository;

@Named
@SessionScoped
public class ErrorPageController extends AbstractController<ErrorPage>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @EditPage
   public static String NEW_OR_EDIT = "/private/errorpages/edit.xhtml";
   @ListPage
   public static String LIST = "/private/errorpages/list.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(ErrorPageRepository.class)
   ErrorPageRepository errorPageRepository;

   // --------------------------------------------------------

   public ErrorPageController()
   {
   }

   @Override
   public void initController()
   {
      for (ErrorPage e : getRepository().getAllList())
      {
         getRepository().fetch(getId(e));
      }
   }

   @Override
   public Object getId(ErrorPage t)
   {
      return t.getHttpError().name();
   }

   @Override
   public String update()
   {
      String outcome = super.update();
      if (outcome == null)
      {
         super.addFacesMessage("Errori nel salvataggio della pagina;");
         return null;
      }
      return outcome;
   }

   @Override
   public void refreshModel()
   {
      setModel(new ListDataModel<ErrorPage>(getRepository().getAllList()));
   }
}
