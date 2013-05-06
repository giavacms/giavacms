package org.giavacms.base.controller;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.controller.AbstractLazyController;

public abstract class AbstractPageController<T extends Page>  extends AbstractLazyController<T> 
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

}
