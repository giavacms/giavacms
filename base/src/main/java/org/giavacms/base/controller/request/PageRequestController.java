/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;


@SuppressWarnings("serial")
@Named
@RequestScoped
public class PageRequestController implements Serializable
{

   @Inject
   PageRepository pageRepository;
   private Page element;
   private boolean withSession = false;

   public PageRequestController()
   {
   }

   public String getPageName()
   {
      if ((this.element != null) && (this.element.getId() != null))
      {
         if (this.element.getTitle() != null
                  && !this.element.getTitle().isEmpty())
         {
            return this.element.getTitle();
         }
         this.element = pageRepository.fetch(this.element.getId());
         return this.element.getTitle();
      }
      return "";

   }

   public String go()
   {
      return "/page.jsf";
   }

   public Page getElement()
   {
      if (element == null)
         element = new Page();
      return element;
   }

   public void setElement(Page element)
   {
      this.element = element;
   }

   public boolean isWithSession()
   {
      return withSession;
   }

   public void setWithSession(boolean withSession)
   {
      this.withSession = withSession;
   }
}