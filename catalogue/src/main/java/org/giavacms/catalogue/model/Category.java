/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;

@Entity
@DiscriminatorValue(value = Category.EXTENSION)
@Table(name = Category.TABLE_NAME)
public class Category extends Page implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String EXTENSION = "Category";
   public static final String TABLE_NAME = "Category";
   public static final boolean HAS_DETAILS = true;

   public Category()
   {
      super();
      super.setExtension(EXTENSION);
   }

   private List<Product> products;
   // name --> super.title
   // description --> super.description
   // active --> active;
   private int orderNum;

   @Transient
   @Deprecated
   public String getName()
   {
      return super.getTitle();
   }

   @Deprecated
   public void setName(String name)
   {
      super.setTitle(name);
   }

   @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
   @OrderBy("title")
   public List<Product> getProducts()
   {
      if (products == null)
         this.products = new ArrayList<Product>();
      return products;
   }

   public void setProducts(List<Product> products)
   {
      this.products = products;
   }

   public void addProduct(Product product)
   {
      getProducts().add(product);
   }

   @Override
   public String toString()
   {
      return "Category [id=" + super.getId() + ", title=" + super.getTitle()
               + ", description=" + super.getDescription() + ", active="
               + super.isActive() + "]";
   }

   public int getOrderNum()
   {
      return orderNum;
   }

   public void setOrderNum(int orderNum)
   {
      this.orderNum = orderNum;
   }

}
