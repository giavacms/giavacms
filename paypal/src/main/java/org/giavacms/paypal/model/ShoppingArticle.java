package org.giavacms.paypal.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.giavacms.common.util.StringUtils;

@Entity
public class ShoppingArticle implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String idProduct;
   private String description;
   private String price;
   private int quantity;
   private String vat;
   private ShoppingCart shoppingCart;
   private String imageUrl;

   public ShoppingArticle()
   {
   }

   public ShoppingArticle(String idProduct, String description, String price, int quantity, String vat, String imageUrl)
   {
      this.idProduct = idProduct;
      this.description = description;
      this.price = price;
      this.quantity = quantity;
      this.vat = vat;
      this.imageUrl = imageUrl;
   }

   public ShoppingArticle(String idProduct, String description, String price, int quantity, String vat,
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
         if (!StringUtils.isEmpty(getPrice()))
         {
            tot = tot.add(new BigDecimal(getPrice()).setScale(2).multiply(new BigDecimal(getQuantity())));
         }
         if (!StringUtils.isEmpty(getVat()))
         {
            tot = tot.add(new BigDecimal(getVat()).setScale(2).multiply(new BigDecimal(getQuantity())));
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

   public String getPrice()
   {
      return price;
   }

   public void setPrice(String price)
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

   public String getVat()
   {
      return vat;
   }

   public void setVat(String vat)
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