package org.giavacms.base.controller;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.giavacms.base.event.PageEvent;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.controller.AbstractLazyController;

public abstract class AbstractPageController<T extends Page> extends AbstractLazyController<T>
{

   private static final long serialVersionUID = 1L;

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
      }
      return outcome;
   }

   @Override
   public String update()
   {
      String outcome = super.update();
      if (outcome != null)
      {
         pageEvent.fire(new PageEvent(getElement()));
      }
      return outcome;
   }
}
