/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = ShoppingArticle.TABLE_NAME)
public class ShoppingArticle implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   public static final String TABLE_NAME = "PPW_ShoppingArticle";

   private String idProduct;
   private String description;
   private BigDecimal price = BigDecimal.ZERO.setScale(2);
   private int quantity = 0;
   private BigDecimal vat = BigDecimal.ZERO.setScale(2);
   private ShoppingCart shoppingCart;
   private String imageUrl;

   public ShoppingArticle()
   {
   }

   public ShoppingArticle(String idProduct, String description, BigDecimal price, int quantity, BigDecimal vat, String imageUrl)
   {
      this.idProduct = idProduct;
      this.description = description;
      this.price = price;
      this.quantity = quantity;
      this.vat = vat;
      this.imageUrl = imageUrl;
   }

   public ShoppingArticle(String idProduct, String description, BigDecimal price, int quantity, BigDecimal vat,
            String currency, String imageUrl)
   {
      this.idProduct = idProduct;
      this.description = description;
      this.price = price;
      this.quantity = quantity;
      this.vat = vat;
      this.imageUrl = imageUrl;
   }

   @Transient
   public String getTotal()
   {
      BigDecimal tot = BigDecimal.ZERO;
      if (getQuantity() > 0)
      {
         if (getPrice()!=null)
         {
            tot = tot.add(getPrice().setScale(2).multiply(new BigDecimal(getQuantity())));
         }
         if (getVat()!=null)
         {
            tot = tot.add(getVat().setScale(2).multiply(new BigDecimal(getQuantity())));
         }
      }
      return tot.toString();
   }

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

   public String getIdProduct()
   {
      return idProduct;
   }

   public void setIdProduct(String idProduct)
   {
      this.idProduct = idProduct;
   }

   public BigDecimal getPrice()
   {
      return price;
   }

   public void setPrice(BigDecimal price)
   {
      this.price = price;
   }

   public int getQuantity()
   {
      return quantity;
   }

   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }

   public void inc(int quantity)
   {
      this.quantity += quantity;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public BigDecimal getVat()
   {
      return vat;
   }

   public void setVat(BigDecimal vat)
   {
      this.vat = vat;
   }

   @ManyToOne
   public ShoppingCart getShoppingCart()
   {
      return shoppingCart;
   }

   public void setShoppingCart(ShoppingCart shoppingCart)
   {
      this.shoppingCart = shoppingCart;
   }

   @Override
   public String toString()
   {
      return "ShoppingArticle [id=" + id + ", idProduct=" + idProduct + ", description=" + description + ", price="
               + price + ", quantity=" + quantity + ", vat=" + vat + "]";
   }

   public String getImageUrl()
   {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl)
   {
      this.imageUrl = imageUrl;
   }

}