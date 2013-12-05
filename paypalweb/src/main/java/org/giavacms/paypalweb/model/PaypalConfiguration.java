package org.giavacms.paypalweb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PaypalConfiguration implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Long id;

   private String email;
   // acquisto no
   private String cancelUrl;
   // acquisto ok
   private String returnUrl;
   // carrello
   private String shoppingCartUrl;
   // riepilogo
   private String previewShoppingCartUrl;
   // payer info
   private String payerInfoUrl;
   // paypal url
   private String serviceUrl;
   // ipn url
   private String ipnUrl;

   private String currency;

   private String emailNotification;

   private String emailSender;

   private String emailBody;
   private String emailObject;

   private String emailShipmentBody;
   private String emailShipmentObject;

   private String emailRollBackBody;
   private String emailRollBackObject;

   private String shoppingCartDirectUrl;

   private boolean logOnly;

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

   public String getPayerInfoUrl()
   {
      return payerInfoUrl;
   }

   public void setPayerInfoUrl(String payerInfoUrl)
   {
      this.payerInfoUrl = payerInfoUrl;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailBody()
   {
      return emailBody;
   }

   public void setEmailBody(String emailBody)
   {
      this.emailBody = emailBody;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailObject()
   {
      return emailObject;
   }

   public void setEmailObject(String emailObject)
   {
      this.emailObject = emailObject;
   }

   public String getEmailNotification()
   {
      return emailNotification;
   }

   public void setEmailNotification(String emailNotification)
   {
      this.emailNotification = emailNotification;
   }

   public String getEmailSender()
   {
      return emailSender;
   }

   public void setEmailSender(String emailSender)
   {
      this.emailSender = emailSender;
   }

   public String getShoppingCartDirectUrl()
   {
      return shoppingCartDirectUrl;
   }

   public void setShoppingCartDirectUrl(String shoppingCartDirectUrl)
   {
      this.shoppingCartDirectUrl = shoppingCartDirectUrl;
   }

   public boolean isLogOnly()
   {
      return logOnly;
   }

   public void setLogOnly(boolean logOnly)
   {
      this.logOnly = logOnly;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailShipmentBody()
   {
      return emailShipmentBody;
   }

   public void setEmailShipmentBody(String emailShipmentBody)
   {
      this.emailShipmentBody = emailShipmentBody;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailShipmentObject()
   {
      return emailShipmentObject;
   }

   public void setEmailShipmentObject(String emailShipmentObject)
   {
      this.emailShipmentObject = emailShipmentObject;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailRollBackBody()
   {
      return emailRollBackBody;
   }

   public void setEmailRollBackBody(String emailRollBackBody)
   {
      this.emailRollBackBody = emailRollBackBody;
   }

   @Lob
   @Column(length = 1024)
   public String getEmailRollBackObject()
   {
      return emailRollBackObject;
   }

   public void setEmailRollBackObject(String emailRollBackObject)
   {
      this.emailRollBackObject = emailRollBackObject;
   }

}
