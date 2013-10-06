package org.giavacms.paypal.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShoppingArticle implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String idProduct;
   private String description;
   private String price;
   private String quantity;
   private String vat;

   public ShoppingArticle()
   {
   }

   public ShoppingArticle(String idProduct, String description, String price, String quantity, String vat)
   {
      this.idProduct = idProduct;
      this.description = description;
      this.price = price;
      this.quantity = quantity;
      this.vat = vat;
   }

   public ShoppingArticle(String idProduct, String description, String price, String quantity, String vat,
            String currency)
   {
      this.idProduct = idProduct;
      this.description = description;
      this.price = price;
      this.quantity = quantity;
      this.vat = vat;
   }

   public String getTotal()
   {
      return "";
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

   public String getPrice()
   {
      return price;
   }

   public void setPrice(String price)
   {
      this.price = price;
   }

   public String getQuantity()
   {
      return quantity;
   }

   public void setQuantity(String quantity)
   {
      this.quantity = quantity;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getVat()
   {
      return vat;
   }

   public void setVat(String vat)
   {
      this.vat = vat;
   }

}