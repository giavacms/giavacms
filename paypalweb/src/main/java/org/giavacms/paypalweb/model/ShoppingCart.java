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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.giavacms.common.util.StringUtils;

@Entity
public class ShoppingCart implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String paymentId;
   private boolean created;
   private boolean confirmed;
   private boolean sent;
   private Date creationDate;
   private Date confirmDate;
   private Date sentDate;
   private BigDecimal partialAmount = BigDecimal.ZERO.setScale(2);
   private BigDecimal partialTax = BigDecimal.ZERO.setScale(2);
   private BigDecimal shipping = BigDecimal.ZERO.setScale(2);
   private String currency;
   private BillingAddress billingAddress;
   private ShippingAddress shippingAddress;
   private List<ShoppingArticle> shoppingArticles;
   private Long logId;

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

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "shoppingCart")
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
            addPartial(article.getQuantity(), article.getPrice(), article.getVat());
            return;
         }
      }
      article.setShoppingCart(this);
      getShoppingArticles().add(article);
      addPartial(article.getQuantity(), article.getPrice(), article.getVat());
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

   public BigDecimal getPartialAmount()
   {
      if (partialAmount == null)
         this.partialAmount = new BigDecimal(0);
      return partialAmount;
   }

   public BigDecimal getPartialTax()
   {
      if (partialTax == null)
         this.partialTax = new BigDecimal(0);
      return partialTax;
   }

   @Transient
   public double getTotal()
   {
      // gestire i null
      return getPartialAmount().add(getPartialTax()).doubleValue();
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
   }

   // ALEX
   public void removeArticle(String idProduct)
   {
      if (!StringUtils.isEmpty(idProduct))
      {
         ShoppingArticle toRemove = null;
         for (ShoppingArticle shoppingArticle : getShoppingArticles())
         {
            if (shoppingArticle.getIdProduct().equals(idProduct))
            {
               toRemove = shoppingArticle;
               break;
            }
         }
         if (toRemove != null)
         {
            addPartial(-toRemove.getQuantity(), toRemove.getPrice(), toRemove.getVat());
            getShoppingArticles().remove(toRemove);
         }
      }

   }

   public void changeArticleQuantity(String idProduct, int quantity)
   {
      if (!StringUtils.isEmpty(idProduct))
      {
         for (ShoppingArticle shoppingArticle : getShoppingArticles())
         {
            if (shoppingArticle.getIdProduct().equals(idProduct))
            {
               if ((shoppingArticle.getQuantity() + quantity) <= 0)
               {
                  quantity = 1 - shoppingArticle.getQuantity();
               }
               else
               {
                  shoppingArticle.setQuantity(shoppingArticle.getQuantity() + quantity);
                  addPartial(quantity, shoppingArticle.getPrice(), shoppingArticle.getVat());
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

   @Temporal(TemporalType.TIMESTAMP)
   public Date getCreationDate()
   {
      return creationDate;
   }

   public void setCreationDate(Date creationDate)
   {
      this.creationDate = creationDate;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getConfirmDate()
   {
      return confirmDate;
   }

   public void setConfirmDate(Date confirmDate)
   {
      this.confirmDate = confirmDate;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getSentDate()
   {
      return sentDate;
   }

   public void setSentDate(Date sentDate)
   {
      this.sentDate = sentDate;
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
               + confirmDate + ", sentdDate=" + sentDate + ", partialAmount=" + partialAmount + ", partialTax="
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

   public Long getLogId()
   {
      return logId;
   }

   public void setLogId(Long logId)
   {
      this.logId = logId;
   }

}
