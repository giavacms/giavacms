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

import org.giavacms.base.model.PageConfiguration;
import org.giavacms.base.repository.PageConfigurationRepository;
import org.giavacms.base.service.EmailSession;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;

@Named
@SessionScoped
public class PageConfigurationController extends
         AbstractLazyController<PageConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";

   @EditPage
   @ViewPage
   @ListPage
   public static String CONF = "/private/pageConfiguration/edit.xhtml";
   // --------------------------------------------------------


   @Inject
   EmailSession emailSession;

   @Inject
   @OwnRepository(PageConfigurationRepository.class)
   PageConfigurationRepository PageConfigurationRepository;

   public PageConfigurationController()
   {

   }

   @Override
   public PageConfiguration getElement()
   {
      if (super.getElement() == null)
         setElement(PageConfigurationRepository.load());
      return super.getElement();
   }

   @Override
   public String update()
   {
      super.update();
      return CONF;
   }

}
