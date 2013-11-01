package org.giavacms.paypal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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

}
