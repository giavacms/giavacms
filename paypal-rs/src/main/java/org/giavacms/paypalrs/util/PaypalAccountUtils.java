package org.giavacms.paypalrs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.giavacms.paypalrs.model.PaypalConfiguration;

import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class PaypalAccountUtils
{

   public static String getAccessToken(PaypalConfiguration paypalConfiguration)
            throws PayPalRESTException
   {

      // ###AccessToken
      // Retrieve the access token from
      // OAuthTokenCredential by passing in
      // ClientID and ClientSecret
      // String clientID = ConfigManager.getInstance().getConfigurationMap().get("clientID");
      // String clientSecret = ConfigManager.getInstance().getConfigurationMap().get(
      // "clientSecret");
      return new OAuthTokenCredential(paypalConfiguration.getClientID(), paypalConfiguration.getClientSecret(),
               getMapFromPaypalConfiguration(paypalConfiguration))
               .getAccessToken();
   }

   public static Map<String, String> getMapFromPaypalConfiguration(PaypalConfiguration paypalConfiguration)
   {
      Map<String, String> map = new HashMap<String, String>();

      // # Connection Information
      // http.ConnectionTimeOut=5000
      if (paypalConfiguration.getHttp_ConnectionTimeOut() != null
               && !paypalConfiguration.getHttp_ConnectionTimeOut().isEmpty())
         map.put("http.ConnectionTimeOut", paypalConfiguration.getHttp_ConnectionTimeOut());
      // http.Retry=1
      if (paypalConfiguration.getHttp_Retry() != null
               && !paypalConfiguration.getHttp_Retry().isEmpty())
         map.put("http.Retry", paypalConfiguration.getHttp_Retry());
      // http.ReadTimeOut=30000
      if (paypalConfiguration.getHttp_ReadTimeOut() != null
               && !paypalConfiguration.getHttp_ReadTimeOut().isEmpty())
         map.put("http.ReadTimeOut", paypalConfiguration.getHttp_ReadTimeOut());
      // http.MaxConnection=100
      if (paypalConfiguration.getHttp_MaxConnection() != null
               && !paypalConfiguration.getHttp_MaxConnection().isEmpty())
         map.put("http.MaxConnection", paypalConfiguration.getHttp_MaxConnection());

      // # HTTP Proxy configuration
      // # If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
      // http.ProxyPort=8080
      if (paypalConfiguration.getHttp_ProxyPort() != null
               && !paypalConfiguration.getHttp_ProxyPort().isEmpty())
         map.put("http.ProxyPort", paypalConfiguration.getHttp_ProxyPort());
      // http.ProxyHost=127.0.0.1
      if (paypalConfiguration.getHttp_ProxyHost() != null
               && !paypalConfiguration.getHttp_ProxyHost().isEmpty())
         map.put("http.ProxyHost", paypalConfiguration.getHttp_ProxyHost());
      // http.UseProxy=false
      if (paypalConfiguration.getHttp_UseProxy() != null
               && !paypalConfiguration.getHttp_UseProxy().isEmpty())
         map.put("http.UseProxy", paypalConfiguration.getHttp_UseProxy());
      // http.ProxyUserName=null
      if (paypalConfiguration.getHttp_ProxyUserName() != null
               && !paypalConfiguration.getHttp_ProxyUserName().isEmpty())
         map.put("http.ProxyUserName", paypalConfiguration.getHttp_ProxyUserName());
      // http.ProxyPassword=null
      if (paypalConfiguration.getHttp_ProxyPassword() != null
               && !paypalConfiguration.getHttp_ProxyPassword().isEmpty())
         map.put("http.ProxyPassword", paypalConfiguration.getHttp_ProxyPassword());

      // #Set this property to true if you are using the PayPal SDK within a Google App Engine java app
      // http.GoogleAppEngine = false
      if (paypalConfiguration.getHttp_GoogleAppEngine() != null
               && !paypalConfiguration.getHttp_GoogleAppEngine().isEmpty())
         map.put("http.GoogleAppEngine", paypalConfiguration.getHttp_GoogleAppEngine());
      //
      // # Service Configuration
      // service.EndPoint=https://api.sandbox.paypal.com
      // # Live EndPoint
      // # service.EndPoint=https://api.paypal.com
      if (paypalConfiguration.getService_EndPoint() != null
               && !paypalConfiguration.getService_EndPoint().isEmpty())
         map.put("service.EndPoint", paypalConfiguration.getService_EndPoint());
      //
      // # Credentials
      // # Credentials
      // clientID=AZxEyxAdj25PxMiCZYu4-xnBuzejS7qcKRo-7Ffdm0BgBxkob3F6_Iz-b3F2
      if (paypalConfiguration.getClientID() != null
               && !paypalConfiguration.getClientID().isEmpty())
         map.put("clientID", paypalConfiguration.getClientID());
      // clientSecret=EM3yExCdMmacgKHWDNUHMyK1fsn0pSAPuEUm1kRfZ5JX17JvMbnqzkdIWBHr
      if (paypalConfiguration.getClientSecret() != null
               && !paypalConfiguration.getClientSecret().isEmpty())
         map.put("clientSecret", paypalConfiguration.getClientSecret());
      return map;
   }

   public static Properties getPropertiesFromPaypalConfiguration(PaypalConfiguration paypalConfiguration)
   {
      Properties properties = new Properties();

      // # Connection Information
      // http.ConnectionTimeOut=5000
      if (paypalConfiguration.getHttp_ConnectionTimeOut() != null
               && !paypalConfiguration.getHttp_ConnectionTimeOut().isEmpty())
         properties.setProperty("http.ConnectionTimeOut", paypalConfiguration.getHttp_ConnectionTimeOut());
      // http.Retry=1
      if (paypalConfiguration.getHttp_Retry() != null
               && !paypalConfiguration.getHttp_Retry().isEmpty())
         properties.setProperty("http.Retry", paypalConfiguration.getHttp_Retry());
      // http.ReadTimeOut=30000
      if (paypalConfiguration.getHttp_ReadTimeOut() != null
               && !paypalConfiguration.getHttp_ReadTimeOut().isEmpty())
         properties.setProperty("http.ReadTimeOut", paypalConfiguration.getHttp_ReadTimeOut());
      // http.MaxConnection=100
      if (paypalConfiguration.getHttp_MaxConnection() != null
               && !paypalConfiguration.getHttp_MaxConnection().isEmpty())
         properties.setProperty("http.MaxConnection", paypalConfiguration.getHttp_MaxConnection());

      // # HTTP Proxy configuration
      // # If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
      // http.ProxyPort=8080
      if (paypalConfiguration.getHttp_ProxyPort() != null
               && !paypalConfiguration.getHttp_ProxyPort().isEmpty())
         properties.setProperty("http.ProxyPort", paypalConfiguration.getHttp_ProxyPort());
      // http.ProxyHost=127.0.0.1
      if (paypalConfiguration.getHttp_ProxyHost() != null
               && !paypalConfiguration.getHttp_ProxyHost().isEmpty())
         properties.setProperty("http.ProxyHost", paypalConfiguration.getHttp_ProxyHost());
      // http.UseProxy=false
      if (paypalConfiguration.getHttp_UseProxy() != null
               && !paypalConfiguration.getHttp_UseProxy().isEmpty())
         properties.setProperty("http.UseProxy", paypalConfiguration.getHttp_UseProxy());
      // http.ProxyUserName=null
      if (paypalConfiguration.getHttp_ProxyUserName() != null
               && !paypalConfiguration.getHttp_ProxyUserName().isEmpty())
         properties.setProperty("http.ProxyUserName", paypalConfiguration.getHttp_ProxyUserName());
      // http.ProxyPassword=null
      if (paypalConfiguration.getHttp_ProxyPassword() != null
               && !paypalConfiguration.getHttp_ProxyPassword().isEmpty())
         properties.setProperty("http.ProxyPassword", paypalConfiguration.getHttp_ProxyPassword());

      // #Set this property to true if you are using the PayPal SDK within a Google App Engine java app
      // http.GoogleAppEngine = false
      if (paypalConfiguration.getHttp_GoogleAppEngine() != null
               && !paypalConfiguration.getHttp_GoogleAppEngine().isEmpty())
         properties.setProperty("http.GoogleAppEngine", paypalConfiguration.getHttp_GoogleAppEngine());
      //
      // # Service Configuration
      // service.EndPoint=https://api.sandbox.paypal.com
      // # Live EndPoint
      // # service.EndPoint=https://api.paypal.com
      if (paypalConfiguration.getService_EndPoint() != null
               && !paypalConfiguration.getService_EndPoint().isEmpty())
         properties.setProperty("service.EndPoint", paypalConfiguration.getService_EndPoint());
      //
      // # Credentials
      // # Credentials
      // clientID=AZxEyxAdj25PxMiCZYu4-xnBuzejS7qcKRo-7Ffdm0BgBxkob3F6_Iz-b3F2
      if (paypalConfiguration.getClientID() != null
               && !paypalConfiguration.getClientID().isEmpty())
         properties.setProperty("clientID", paypalConfiguration.getClientID());
      // clientSecret=EM3yExCdMmacgKHWDNUHMyK1fsn0pSAPuEUm1kRfZ5JX17JvMbnqzkdIWBHr
      if (paypalConfiguration.getClientSecret() != null
               && !paypalConfiguration.getClientSecret().isEmpty())
         properties.setProperty("clientSecret", paypalConfiguration.getClientSecret());
      return properties;
   }
}
