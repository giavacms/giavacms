package org.giavacms.base.annotation.event;

import org.giavacms.base.model.Page;

public class PageEvent
{
   Page page;

   public Page getPage()
   {
      return page;
   }

   public PageEvent(Page page)
   {
      this.page = page;
   }

}
