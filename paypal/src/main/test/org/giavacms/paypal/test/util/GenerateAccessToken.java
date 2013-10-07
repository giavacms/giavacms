package org.giavacms.paypal.test.util;

import com.paypal.core.ConfigManager;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class GenerateAccessToken
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
}
