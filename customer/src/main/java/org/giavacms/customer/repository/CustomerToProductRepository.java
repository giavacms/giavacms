/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerToProduct;

@Named
@Stateless
@LocalBean
public class CustomerToProductRepository extends
         AbstractRepository<CustomerToProduct>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "listOrder asc";
   }

   @Override
   public CustomerToProduct fetch(Object key)
   {
      try
      {
         Long id;
         if (key instanceof String)
         {
            id = Long.valueOf((String) key);
         }
         else if (key instanceof Long)
         {
            id = (Long) key;
         }
         else
         {
            throw new Exception("key type is not correct!!");
         }
         CustomerToProduct customerToProduct = find(id);
         if (customerToProduct.getCustomer() != null)
         {
            for (Document document : customerToProduct.getCustomer()
                     .getDocuments())
            {
               document.getName();
            }

            for (Image image : customerToProduct.getCustomer().getImages())
            {
               image.getName();
               image.getFilename();
            }
         }
         if (customerToProduct.getProduct() != null)
         {
            for (Document document : customerToProduct.getProduct()
                     .getDocuments())
            {
               document.getName();
            }

            for (Image image : customerToProduct.getProduct().getImages())
            {
               image.getName();
               image.getFilename();
            }
         }
         return customerToProduct;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   protected void applyRestrictions(Search<CustomerToProduct> search,
            String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      if (search.getObj().getCustomer() != null
               && search.getObj().getCustomer().getId() != null
               && search.getObj().getCustomer().getId() > 0)
      {
         sb.append(separator).append(alias).append(".customer.id = :IDC ");
         params.put("IDC", search.getObj().getCustomer().getId());
      }
      if (search.getObj().getProduct() != null
               && search.getObj().getProduct().getId() != null
               && search.getObj().getProduct().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".product.id = :IDP ");
         params.put("IDP", search.getObj().getProduct().getId());
      }
   }

   @Override
   public List<CustomerToProduct> getList(Search<CustomerToProduct> ricerca,
            int startRow, int pageSize)
   {
      List<CustomerToProduct> list = super.getList(ricerca, startRow,
               pageSize);
      for (CustomerToProduct customerToProduct : list)
      {
         if (customerToProduct.getCustomer() != null)
         {
            for (Document document : customerToProduct.getCustomer()
                     .getDocuments())
            {
               document.getName();
            }

            for (Image image : customerToProduct.getCustomer().getImages())
            {
               image.getName();
               image.getFilename();
            }
         }
         if (customerToProduct.getProduct() != null)
         {
            for (Document document : customerToProduct.getProduct()
                     .getDocuments())
            {
               document.getName();
            }

            for (Image image : customerToProduct.getProduct().getImages())
            {
               image.getName();
               image.getFilename();
            }
         }
      }
      return list;
   }

   public List<Product> getProductList(Customer customer)
   {
      ArrayList<Product> res = new ArrayList<Product>();
      if (customer != null)
      {
         Search<CustomerToProduct> search = new Search<CustomerToProduct>(
                  CustomerToProduct.class);
         search.getObj().setCustomer(customer);
         List<CustomerToProduct> l = getList(search, 0, 0);
         if (l != null)
         {
            for (CustomerToProduct ctp : l)
            {
               if (ctp.getProduct() != null)
                  res.add(ctp.getProduct());
            }
         }
      }
      return res;
   }
}
