/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.giavacms.base.event.PageEvent;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.common.event.ResetEvent;
import org.primefaces.event.RowEditEvent;

public abstract class AbstractPageController<T extends Page> extends AbstractLazyController<T>
{

   private static final long serialVersionUID = 1L;

   @Inject
   PageRepository pageRepository;

   @Inject
   Event<ResetEvent> resetEvent;

   @Override
   public void defaultCriteria()
   {
      getSearch().getObj().setTemplate(new TemplateImpl());
   }

   @Override
   public Object getId(T t)
   {
      return t.getId();
   }

   abstract public String getExtension();

   @Inject
   Event<PageEvent> pageEvent;

   @Override
   public String save()
   {
      String outcome = super.save();
      if (outcome != null)
      {
         pageEvent.fire(new PageEvent(getElement()));

         if (getElement().isClone())
         {
            // same language of choosen main page
            Page basePage = pageRepository.find(getElement().getTemplate().getMainPageId());
            if (basePage.getLang() > 0)
            {
               pageRepository.updateLanguage(basePage.getLang(), getElement().getId());
            }
         }
      }
      resetEvent.fire(new ResetEvent(getEntityClass()));
      return viewCurrent();
   }

   @Override
   public String update()
   {
      String outcome = super.update();
      if (outcome != null)
      {
         pageEvent.fire(new PageEvent(getElement()));

         if (getElement().isClone())
         {
            // same language of choosen main page
            Page basePage = pageRepository.find(getElement().getTemplate().getMainPageId());
            if (basePage.getLang() > 0)
            {
               pageRepository.updateLanguage(basePage.getLang(), getElement().getId());
            }
         }

      }
      resetEvent.fire(new ResetEvent(getEntityClass()));
      return viewCurrent();
   }

   @Override
   public String delete()
   {
      String outcome = super.delete();
      resetEvent.fire(new ResetEvent(getEntityClass()));
      return outcome;
   }

   @Override
   public void onRowEdit(RowEditEvent ree)
   {
      super.onRowEdit(ree);
      resetEvent.fire(new ResetEvent(getEntityClass()));
   }

   @Override
   public void deleteInline()
   {
      super.deleteInline();
      resetEvent.fire(new ResetEvent(getEntityClass()));
   }

   @Override
   public String reset()
   {
      String outcome = super.reset();
      resetEvent.fire(new ResetEvent(getEntityClass()));
      return outcome;
   }
}
