/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller.request;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.util.JSFUtils;

@SuppressWarnings("serial")
@Named
@RequestScoped
public class PageRequestController implements Serializable
{

   @Inject
   PageRepository pageRepository;
   private Page element;
   private String uri;

   public PageRequestController()
   {
   }

   @PostConstruct
   public void initPage()
   {
      if (element == null || element.getId() == null)
      {
         element = new Page();
         element.setId(JSFUtils.getPageId());
      }
   }

   public String getPageName()
   {
      return getElement().getTitle() == null ? "" : getElement().getTitle();

   }

   public Page getElement()
   {
      if (element.getId() != null && (element.getTitle() == null || element.getTitle().trim().isEmpty()))
      {
         element = pageRepository.fetch(element.getId());
      }
      return element;
   }

   public void setElement(Page element)
   {
      this.element = element;
   }

   public String getUri()
   {
      return uri;
   }

   public void setUri(String uri)
   {
      this.uri = uri;
   }
}