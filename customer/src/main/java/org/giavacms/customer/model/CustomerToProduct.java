/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.giavacms.catalogue.model.Product;

@Entity
@Table(name=CustomerToProduct.TABLE_NAME)
public class CustomerToProduct
{

   private Long id;
   private Customer customer;
   private Product product;
   private Integer listOrder = 0;
   public static final String TABLE_NAME = "CustomerToProduct";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   @ManyToOne
   public Customer getCustomer()
   {
      return customer;
   }

   public void setCustomer(Customer customer)
   {
      this.customer = customer;
   }

   @ManyToOne
   public Product getProduct()
   {
      return product;
   }

   public void setProduct(Product product)
   {
      this.product = product;
   }

   public Integer getListOrder()
   {
      return listOrder;
   }

   public void setListOrder(Integer listOrder)
   {
      this.listOrder = listOrder;
   }

}
