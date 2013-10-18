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
import org.giavacms.base.service.CacheService;
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
   CacheService cacheService;

   private String pageId;
   private Long templateId;
   private String resultTest;

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

   public void generateCache()
   {
      if (getTemplateId() != null)
      {
         String result = cacheService.writeByTemplate(getTemplateId());
         setResultTest(result);
         return;
      }
      if (getPageId() != null && !getPageId().isEmpty())
      {
         String result = cacheService.write(getPageId());
         setResultTest(result);
         return;
      }
   }

   public String getPageId()
   {
      return pageId;
   }

   public void setPageId(String pageId)
   {
      this.pageId = pageId;
   }

   public Long getTemplateId()
   {
      return templateId;
   }

   public void setTemplateId(Long templateId)
   {
      this.templateId = templateId;
   }

   public String getResultTest()
   {
      return resultTest;
   }

   public void setResultTest(String resultTest)
   {
      this.resultTest = resultTest;
   }

}
