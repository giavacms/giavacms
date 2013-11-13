package org.giavacms.paypalweb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.giavacms.common.util.StringUtils;

@Entity
public class ShoppingCart implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String paymentId;
   private String currency;
   private boolean created;
   private boolean confirmed;
   private boolean sent;
   private Date creationDate;
   private Date confirmDate;
   private Date sentdDate;
   private BigDecimal partialAmount = BigDecimal.ZERO.setScale(2);
   private BigDecimal partialTax = BigDecimal.ZERO.setScale(2);
   private BigDecimal shipping = BigDecimal.ZERO.setScale(2);
   private BillingAddress billingAddress;
   private ShippingAddress shippingAddress;
   private List<ShoppingArticle> shoppingArticles;

   public ShoppingCart()
   {
      this.currency = "EUR";
      this.creationDate = new Date();
   }

   public ShoppingCart(String currency)
   {
      this.currency = currency;
      this.creationDate = new Date();
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

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public List<ShoppingArticle> getShoppingArticles()
   {
      if (shoppingArticles == null)
         this.shoppingArticles = new ArrayList<ShoppingArticle>();
      return shoppingArticles;
   }

   public void setShoppingArticles(List<ShoppingArticle> shoppingArticles)
   {
      this.shoppingArticles = shoppingArticles;
   }

   public void addArticle(ShoppingArticle article)
   {
      for (ShoppingArticle shoppingArticle : getShoppingArticles())
      {
         if (shoppingArticle.getIdProduct().equals(article.getIdProduct()))
         {
            shoppingArticle.inc(article.getQuantity());
            return;
         }
      }
      article.setShoppingCart(this);
      getShoppingArticles().add(article);
   }

   public String getPaymentId()
   {
      return paymentId;
   }

   public void setPaymentId(String paymentId)
   {
      this.paymentId = paymentId;
   }

   public String getCurrency()
   {
      return currency;
   }

   public void setCurrency(String currency)
   {
      this.currency = currency;
   }

   public boolean isCreated()
   {
      return created;
   }

   public void setCreated(boolean created)
   {
      this.created = created;
   }

   @Transient
   public BigDecimal getPartialAmount()
   {
      return partialAmount;
   }

   @Transient
   public BigDecimal getPartialTax()
   {
      return partialTax;
   }

   @Transient
   public double getTotal()
   {
      return partialAmount.add(partialTax).doubleValue();
   }

   public void addPartial(int quantity, String price, String vat)
   {
      BigDecimal singleAmount = null;
      if (price != null)
      {
         try
         {
            singleAmount = new BigDecimal(quantity).multiply(new BigDecimal(price)).setScale(2);
         }
         catch (NumberFormatException e)
         {
            e.printStackTrace();
         }
      }
      BigDecimal singleTax = null;
      if (vat != null)
      {
         try
         {
            singleTax = new BigDecimal(quantity).multiply(new BigDecimal(vat)).setScale(2);
         }
         catch (NumberFormatException e)
         {
            e.printStackTrace();
         }
      }
      if (singleAmount != null)
         this.partialAmount = this.partialAmount.add(singleAmount);
      if (singleTax != null)
         this.partialTax = this.partialTax.add(singleTax);

      // double singleAmount = Double.valueOf(quantity) * Double.valueOf(price);
      // double singleTax = Double.valueOf(quantity) * Double.valueOf(vat);
      // this.partialAmount += singleAmount;
      // this.partialTax += singleTax;
   }

   // ALEX
   public void removeArticle(String idProduct)
   {
      if (!StringUtils.isEmpty(idProduct))
      {
         for (Iterator<ShoppingArticle> it = getShoppingArticles().iterator(); it.hasNext();)
         {
            ShoppingArticle shoppingArticle = it.next();
            if (shoppingArticle.getIdProduct().equals(idProduct))
            {
               it.remove();
               break;
            }
         }
      }
      return;
   }

   public void changeArticleQuantity(String vat, String price, String idProduct, int quantity)
   {
      if (!StringUtils.isEmpty(idProduct))
      {
         for (ShoppingArticle shoppingArticle : getShoppingArticles())
         {
            if (shoppingArticle.getIdProduct().equals(idProduct))
            {
               if (shoppingArticle.getQuantity() + quantity <= 0)
               {
                  quantity = 1 - shoppingArticle.getQuantity();
               }
               if (quantity > 0)
               {
                  shoppingArticle.setQuantity(shoppingArticle.getQuantity() + quantity);
                  addPartial(quantity, price, vat);
               }
               break;
            }
         }
      }
      return;
   }

   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public BillingAddress getBillingAddress()
   {
      if (billingAddress == null)
         this.billingAddress = new BillingAddress();
      return billingAddress;
   }

   public void setBillingAddress(BillingAddress billingAddress)
   {
      this.billingAddress = billingAddress;
   }

   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public ShippingAddress getShippingAddress()
   {
      if (shippingAddress == null)
         this.shippingAddress = new ShippingAddress();
      return shippingAddress;
   }

   public void setShippingAddress(ShippingAddress shippingAddress)
   {
      this.shippingAddress = shippingAddress;
   }

   public boolean isConfirmed()
   {
      return confirmed;
   }

   public void setConfirmed(boolean confirmed)
   {
      this.confirmed = confirmed;
   }

   public boolean isSent()
   {
      return sent;
   }

   public void setSent(boolean sent)
   {
      this.sent = sent;
   }

   public Date getCreationDate()
   {
      return creationDate;
   }

   public void setCreationDate(Date creationDate)
   {
      this.creationDate = creationDate;
   }

   public Date getConfirmDate()
   {
      return confirmDate;
   }

   public void setConfirmDate(Date confirmDate)
   {
      this.confirmDate = confirmDate;
   }

   public Date getSentdDate()
   {
      return sentdDate;
   }

   public void setSentdDate(Date sentdDate)
   {
      this.sentdDate = sentdDate;
   }

   public void setPartialAmount(BigDecimal partialAmount)
   {
      this.partialAmount = partialAmount;
   }

   public void setPartialTax(BigDecimal partialTax)
   {
      this.partialTax = partialTax;
   }

   @Override
   public String toString()
   {
      return "ShoppingCart [id=" + id + ", paymentId=" + paymentId + ", currency=" + currency + ", created=" + created
               + ", confirmed=" + confirmed + ", sent=" + sent + ", creationDate=" + creationDate + ", confirmDate="
               + confirmDate + ", sentdDate=" + sentdDate + ", partialAmount=" + partialAmount + ", partialTax="
               + partialTax + ", billingAddress=" + billingAddress + ", shippingAddress=" + shippingAddress
               + ", shoppingArticles=" + shoppingArticles + "]";
   }

   public BigDecimal getShipping()
   {
      return shipping;
   }

   public void setShipping(BigDecimal shipping)
   {
      this.shipping = shipping;
   }

}
