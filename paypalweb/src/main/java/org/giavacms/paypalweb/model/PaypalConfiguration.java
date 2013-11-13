package org.giavacms.paypalweb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PaypalConfiguration implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Long id;

   // Service Configuration
   private String serviceUrl;

   private String email;
   private String cancelUrl;
   private String returnUrl;
   private String shoppingCartUrl;
   private String previewShoppingCartUrl;
   private String ipnUrl;
   private String currency;

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

   public String getReturnUrl()
   {
      return returnUrl;
   }

   public void setReturnUrl(String returnUrl)
   {
      this.returnUrl = returnUrl;
   }

   public String getCancelUrl()
   {
      return cancelUrl;
   }

   public void setCancelUrl(String cancelUrl)
   {
      this.cancelUrl = cancelUrl;
   }

   public String getCurrency()
   {
      return currency;
   }

   public void setCurrency(String currency)
   {
      this.currency = currency;
   }

   public String getShoppingCartUrl()
   {
      return shoppingCartUrl;
   }

   public void setShoppingCartUrl(String shoppingCartUrl)
   {
      this.shoppingCartUrl = shoppingCartUrl;
   }

   public String getPreviewShoppingCartUrl()
   {
      return previewShoppingCartUrl;
   }

   public void setPreviewShoppingCartUrl(String previewShoppingCartUrl)
   {
      this.previewShoppingCartUrl = previewShoppingCartUrl;
   }

   public String getServiceUrl()
   {
      return serviceUrl;
   }

   public void setServiceUrl(String serviceUrl)
   {
      this.serviceUrl = serviceUrl;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   @Override
   public String toString()
   {
      return "PaypalConfiguration [id=" + id + ", serviceUrl=" + serviceUrl + ", email=" + email + ", cancelUrl="
               + cancelUrl + ", returnUrl=" + returnUrl + ", shoppingCartUrl=" + shoppingCartUrl
               + ", previewShoppingCartUrl=" + previewShoppingCartUrl + ", currency=" + currency + "]";
   }

   public String getIpnUrl()
   {
      return ipnUrl;
   }

   public void setIpnUrl(String ipnUrl)
   {
      this.ipnUrl = ipnUrl;
   }

}
