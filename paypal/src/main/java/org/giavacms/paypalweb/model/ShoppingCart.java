/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.giavacms.base.util.StringUtils;
import org.giavacms.paypalweb.model.enums.PaypalStatus;

@Entity
@Table(name = ShoppingCart.TABLE_NAME)
public class ShoppingCart implements Serializable
{
   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "PPW_ShoppingCart";
   private Long id;
   private boolean active = true;
   private Date refundedDate;
   private Date completedDate;
   private Date initDate;
   private Date undoDate;
   private Date sentDate;
   private Date notCompletedDate;

   private BigDecimal partialAmount = BigDecimal.ZERO.setScale(2);
   private BigDecimal partialTax = BigDecimal.ZERO.setScale(2);
   private BigDecimal shipping = BigDecimal.ZERO.setScale(2);
   private String currency;
   private BillingAddress billingAddress;
   private ShippingAddress shippingAddress;
   private List<ShoppingArticle> shoppingArticles;
   private Long logId;
   private String notes;
   private PaypalStatus paypalStatus;

   public ShoppingCart()
   {
      this.currency = "EUR";
      this.initDate = new Date();
   }

   public ShoppingCart(String currency)
   {
      this.currency = currency;
      this.initDate = new Date();
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

   public String getCurrency()
   {
      return currency;
   }

   public void setCurrency(String currency)
   {
      this.currency = currency;
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

   @Transient
   public double getTotalWithSipping()
   {
      // gestire i null
      return getPartialAmount().add(getPartialTax()).add(getShipping()).doubleValue();
   }

   public void addPartial(int quantity, BigDecimal price, BigDecimal vat)
   {
      BigDecimal singleAmount = null;
      if (price != null)
      {
         try
         {
            singleAmount = new BigDecimal(quantity).multiply(price).setScale(2);
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
            singleTax = new BigDecimal(quantity).multiply(vat).setScale(2);
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

   public void setPartialAmount(BigDecimal partialAmount)
   {
      this.partialAmount = partialAmount;
   }

   public void setPartialTax(BigDecimal partialTax)
   {
      this.partialTax = partialTax;
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

   @Lob
   @Column(length = 1024)
   public String getNotes()
   {
      return notes;
   }

   public void setNotes(String notes)
   {
      this.notes = notes;
   }

   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Enumerated(EnumType.STRING)
   public PaypalStatus getPaypalStatus()
   {
      return paypalStatus;
   }

   public void setPaypalStatus(PaypalStatus paypalStatus)
   {
      this.paypalStatus = paypalStatus;
   }

   @Transient
   public boolean isRefunded()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.Refunded) ? true : false);
   }

   @Transient
   public boolean isCompleted()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.Completed) ? true : false);
   }

   @Transient
   public boolean isInit()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.Init) ? true : false);
   }

   @Transient
   public boolean isUndo()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.Undo) ? true : false);
   }

   @Transient
   public boolean isSent()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.Sent) ? true : false);
   }

   @Transient
   public boolean isNotCompleted()
   {
      return (paypalStatus != null && paypalStatus.equals(PaypalStatus.NotCompleted) ? true : false);
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getRefundedDate()
   {
      return refundedDate;
   }

   public void setRefundedDate(Date refundedDate)
   {
      this.refundedDate = refundedDate;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getCompletedDate()
   {
      return completedDate;
   }

   public void setCompletedDate(Date completedDate)
   {
      this.completedDate = completedDate;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getInitDate()
   {
      return initDate;
   }

   public void setInitDate(Date initDate)
   {
      this.initDate = initDate;
   }

   @Temporal(TemporalType.TIMESTAMP)
   public Date getUndoDate()
   {
      return undoDate;
   }

   public void setUndoDate(Date undoDate)
   {
      this.undoDate = undoDate;
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

   @Temporal(TemporalType.TIMESTAMP)
   public Date getNotCompletedDate()
   {
      return notCompletedDate;
   }

   public void setNotCompletedDate(Date notCompletedDate)
   {
      this.notCompletedDate = notCompletedDate;
   }

   @Override
   public String toString()
   {
      return "ShoppingCart [active=" + active + ", id=" + id + ", refundedDate=" + refundedDate + ", completedDate="
               + completedDate + ", initDate=" + initDate + ", undoDate=" + undoDate + ", sentDate=" + sentDate
               + ", notCompletedDate=" + notCompletedDate + ", partialAmount=" + partialAmount + ", partialTax="
               + partialTax + ", shipping=" + shipping + ", currency=" + currency + ", billingAddress="
               + billingAddress + ", shippingAddress=" + shippingAddress + ", shoppingArticles=" + shoppingArticles
               + ", logId=" + logId + ", notes=" + notes + ", paypalStatus=" + paypalStatus + "]";
   }

}
