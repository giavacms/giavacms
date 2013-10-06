package org.giavacms.paypal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ShoppingCart implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Long id;
   private String shipping;
   private List<ShoppingArticle> articles;
   private PayerInfo payerInfo;
   private String paymentId;
   private String currency;
   private Date data;
   private String approvalUrl;
   private String selfUrl;
   private String executeUrl;
   private boolean created;

   public ShoppingCart()
   {
      this.currency = "EUR";
      this.data = new Date();
   }

   public ShoppingCart(String currency)
   {
      this.currency = currency;
      this.data = new Date();
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

   public String getShipping()
   {
      return shipping;
   }

   public void setShipping(String shipping)
   {
      this.shipping = shipping;
   }

   @ManyToOne(cascade = CascadeType.ALL)
   public List<ShoppingArticle> getArticles()
   {
      if (articles == null)
         this.articles = new ArrayList<ShoppingArticle>();
      return articles;
   }

   public void setArticles(List<ShoppingArticle> articles)
   {
      this.articles = articles;
   }

   public void addArticle(ShoppingArticle article)
   {
      getArticles().add(article);
   }

   @OneToOne
   public PayerInfo getPayerInfo()
   {
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

   public Date getData()
   {
      return data;
   }

   public void setData(Date data)
   {
      this.data = data;
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

}
