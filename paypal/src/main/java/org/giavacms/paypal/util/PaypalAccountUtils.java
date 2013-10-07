package org.giavacms.paypal.util;

import java.util.Properties;

import org.giavacms.paypal.model.PaypalConfiguration;

import com.paypal.core.ConfigManager;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class PaypalAccountUtils
{

   public static String getAccessToken() throws PayPalRESTException
   {

      // ###AccessToken
      // Retrieve the access token from
      // OAuthTokenCredential by passing in
      // ClientID and ClientSecret
      String clientID = ConfigManager.getInstance().getConfigurationMap().get("clientID");
      String clientSecret = ConfigManager.getInstance().getConfigurationMap().get(
               "clientSecret");
      return new OAuthTokenCredential(clientID, clientSecret)
               .getAccessToken();
   }

   public static Properties getPropertiesFromPaypalConfiguration(PaypalConfiguration paypalConfiguration)
   {
      Properties properties = new Properties();

      // # Connection Information
      // http.ConnectionTimeOut=5000
      properties.setProperty("http.ConnectionTimeOut", paypalConfiguration.getHttp_ConnectionTimeOut());
      // http.Retry=1
      properties.setProperty("http.Retry", paypalConfiguration.getHttp_Retry());
      // http.ReadTimeOut=30000
      properties.setProperty("http.ReadTimeOut", paypalConfiguration.getHttp_ReadTimeOut());
      // http.MaxConnection=100
      properties.setProperty("http.MaxConnection", paypalConfiguration.getHttp_MaxConnection());

      // # HTTP Proxy configuration
      // # If you are using proxy set http.UseProxy to true and replace the following values with your proxy parameters
      // http.ProxyPort=8080
      properties.setProperty("http.ProxyPort", paypalConfiguration.getHttp_ProxyPort());
      // http.ProxyHost=127.0.0.1
      properties.setProperty("http.ProxyHost", paypalConfiguration.getHttp_ProxyHost());
      // http.UseProxy=false
      properties.setProperty("http.UseProxy", paypalConfiguration.getHttp_UseProxy());
      // http.ProxyUserName=null
      properties.setProperty("http.ProxyUserName", paypalConfiguration.getHttp_ProxyUserName());
      // http.ProxyPassword=null
      properties.setProperty("http.ProxyPassword", paypalConfiguration.getHttp_ProxyPassword());

      // #Set this property to true if you are using the PayPal SDK within a Google App Engine java app
      // http.GoogleAppEngine = false
      properties.setProperty("http.GoogleAppEngine", paypalConfiguration.getHttp_GoogleAppEngine());
      //
      // # Service Configuration
      // service.EndPoint=https://api.sandbox.paypal.com
      // # Live EndPoint
      // # service.EndPoint=https://api.paypal.com
      properties.setProperty("service.EndPoint", paypalConfiguration.getService_EndPoint());
      //
      // # Credentials
      // # Credentials
      // clientID=AZxEyxAdj25PxMiCZYu4-xnBuzejS7qcKRo-7Ffdm0BgBxkob3F6_Iz-b3F2
      properties.setProperty("clientID", paypalConfiguration.getClientID());
      // clientSecret=EM3yExCdMmacgKHWDNUHMyK1fsn0pSAPuEUm1kRfZ5JX17JvMbnqzkdIWBHr
      properties.setProperty("clientSecret", paypalConfiguration.getClientSecret());
      return properties;
   }
}
