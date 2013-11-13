package org.giavacms.paypalrs.model;

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
   // Connection Information
   private String http_ConnectionTimeOut;
   private String http_Retry;
   private String http_ReadTimeOut;
   private String http_MaxConnection;

   // HTTP Proxy configuration
   // If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
   private String http_ProxyPort;
   private String http_ProxyHost;
   private String http_UseProxy;
   private String http_ProxyUserName;
   private String http_ProxyPassword;

   // Set this property to true if you are using the PayPal SDK within a Google App Engine java app
   private String http_GoogleAppEngine;

   // Service Configuration
   private String service_EndPoint;
   // Live EndPoint
   // service.EndPoint=https://api.paypal.com
   // Credentials
   // Credentials
   private String clientID;
   private String clientSecret;

   private String cancelUrl;
   private String returnUrl;
   private String shoppingCartUrl;
   private String previewShoppingCartUrl;
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

   public String getHttp_ConnectionTimeOut()
   {
      return http_ConnectionTimeOut;
   }

   public void setHttp_ConnectionTimeOut(String http_ConnectionTimeOut)
   {
      this.http_ConnectionTimeOut = http_ConnectionTimeOut;
   }

   public String getHttp_Retry()
   {
      return http_Retry;
   }

   public void setHttp_Retry(String http_Retry)
   {
      this.http_Retry = http_Retry;
   }

   public String getHttp_ReadTimeOut()
   {
      return http_ReadTimeOut;
   }

   public void setHttp_ReadTimeOut(String http_ReadTimeOut)
   {
      this.http_ReadTimeOut = http_ReadTimeOut;
   }

   public String getHttp_MaxConnection()
   {
      return http_MaxConnection;
   }

   public void setHttp_MaxConnection(String http_MaxConnection)
   {
      this.http_MaxConnection = http_MaxConnection;
   }

   public String getHttp_ProxyPort()
   {
      return http_ProxyPort;
   }

   public void setHttp_ProxyPort(String http_ProxyPort)
   {
      this.http_ProxyPort = http_ProxyPort;
   }

   public String getHttp_ProxyHost()
   {
      return http_ProxyHost;
   }

   public void setHttp_ProxyHost(String http_ProxyHost)
   {
      this.http_ProxyHost = http_ProxyHost;
   }

   public String getHttp_UseProxy()
   {
      return http_UseProxy;
   }

   public void setHttp_UseProxy(String http_UseProxy)
   {
      this.http_UseProxy = http_UseProxy;
   }

   public String getHttp_ProxyUserName()
   {
      return http_ProxyUserName;
   }

   public void setHttp_ProxyUserName(String http_ProxyUserName)
   {
      this.http_ProxyUserName = http_ProxyUserName;
   }

   public String getHttp_ProxyPassword()
   {
      return http_ProxyPassword;
   }

   public void setHttp_ProxyPassword(String http_ProxyPassword)
   {
      this.http_ProxyPassword = http_ProxyPassword;
   }

   public String getHttp_GoogleAppEngine()
   {
      return http_GoogleAppEngine;
   }

   public void setHttp_GoogleAppEngine(String http_GoogleAppEngine)
   {
      this.http_GoogleAppEngine = http_GoogleAppEngine;
   }

   public String getService_EndPoint()
   {
      return service_EndPoint;
   }

   public void setService_EndPoint(String service_EndPoint)
   {
      this.service_EndPoint = service_EndPoint;
   }

   public String getClientID()
   {
      return clientID;
   }

   public void setClientID(String clientID)
   {
      this.clientID = clientID;
   }

   public String getClientSecret()
   {
      return clientSecret;
   }

   public void setClientSecret(String clientSecret)
   {
      this.clientSecret = clientSecret;
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

}
