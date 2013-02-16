/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.pojo;

import java.util.List;
import javax.faces.model.ListDataModel;

import org.giavacms.base.model.Page;
import org.primefaces.model.SelectableDataModel;


public class PageDataModel extends ListDataModel<Page> implements
         SelectableDataModel<Page>
{

   public PageDataModel()
   {
   }

   public PageDataModel(List<Page> pageslist)
   {
      super(pageslist);
   }

   public Page getRowData(String rowKey)
   {
      List<Page> pages = (List<Page>) getWrappedData();
      for (Page page : pages)
      {
         if (page.getId().equals(rowKey))
            return page;
      }
      return null;
   }

   public Object getRowKey(Page page)
   {
      return page.getId();
   }

}
