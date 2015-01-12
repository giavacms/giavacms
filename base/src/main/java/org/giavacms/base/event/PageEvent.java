/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.event;

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
