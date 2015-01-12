package org.giavacms.paypalrs.model;

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
   private double shipping;
   private List<ShoppingArticle> shoppingArticles;
   private PayerInfo payerInfo;
   private String paymentId;
   private String currency;
   private Date dataStart;
   private String approvalUrl;
   private String selfUrl;
   private String executeUrl;
   private boolean created;
   private Date dataEnd;
   private boolean payed;
   private BigDecimal partialAmount = BigDecimal.ZERO.setScale(2);
   private BigDecimal partialTax = BigDecimal.ZERO.setScale(2);

   public ShoppingCart()
   {
      this.currency = "EUR";
      this.dataStart = new Date();
   }

   public ShoppingCart(String currency)
   {
      this.currency = currency;
      this.dataStart = new Date();
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

   public double getShipping()
   {
      return shipping;
   }

   public void setShipping(double shipping)
   {
      this.shipping = shipping;
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

   @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   public PayerInfo getPayerInfo()
   {
      if (payerInfo == null)
         this.payerInfo = new PayerInfo();
      return payerInfo;
   }

   public void setPayerInfo(PayerInfo payerInfo)
   {
      this.payerInfo = payerInfo;
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

   public Date getDataStart()
   {
      return dataStart;
   }

   public void setDataStart(Date data)
   {
      this.dataStart = data;
   }

   public String getApprovalUrl()
   {
      return approvalUrl;
   }

   public void setApprovalUrl(String approvalUrl)
   {
      this.approvalUrl = approvalUrl;
   }

   public String getSelfUrl()
   {
      return selfUrl;
   }

   public void setSelfUrl(String selfUrl)
   {
      this.selfUrl = selfUrl;
   }

   public String getExecuteUrl()
   {
      return executeUrl;
   }

   public void setExecuteUrl(String executeUrl)
   {
      this.executeUrl = executeUrl;
   }

   public boolean isCreated()
   {
      return created;
   }

   public void setCreated(boolean created)
   {
      this.created = created;
   }

   @Override
   public String toString()
   {
      return "ShoppingCart [id=" + id + ", shipping=" + shipping + ", payerInfo=" + payerInfo + ", paymentId="
               + paymentId + ", currency=" + currency + ", data=" + dataStart + ", approvalUrl=" + approvalUrl
               + ", selfUrl=" + selfUrl + ", executeUrl=" + executeUrl + ", created=" + created + "]";
   }

   public Date getDataEnd()
   {
      return dataEnd;
   }

   public void setDataEnd(Date dataEnd)
   {
      this.dataEnd = dataEnd;
   }

   public boolean isPayed()
   {
      return payed;
   }

   public void setPayed(boolean payed)
   {
      this.payed = payed;
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
}
