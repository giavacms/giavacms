package org.giavacms.scenario.pojo;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.giavacms.catalogue.model.Product;
import org.primefaces.model.SelectableDataModel;

public class ProductDataModel extends ListDataModel<Product> implements
         SelectableDataModel<Product>
{

   public ProductDataModel()
   {
   }

   public ProductDataModel(List<Product> pageslist)
   {
      super(pageslist);
   }

   @SuppressWarnings("unchecked")
   public Product getRowData(String rowKey)
   {
      List<Product> products = (List<Product>) getWrappedData();
      for (Product product : products)
      {
         if (product.getId().equals(rowKey))
            return product;
      }
      return null;
   }

   public Object getRowKey(Product page)
   {
      return page.getId();
   }

}
