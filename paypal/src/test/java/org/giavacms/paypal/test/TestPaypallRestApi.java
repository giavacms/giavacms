package org.giavacms.paypal.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import org.giavacms.paypal.model.PaypalConfiguration;
import org.giavacms.paypal.util.PaypalAccountUtils;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;

public class TestPaypallRestApi 
{

   private static final Logger LOGGER = Logger
            .getLogger(TestPaypallRestApi.class.getCanonicalName());
   private static Map<String, String> map = new HashMap<String, String>();

   public static void main(String[] args) throws PayPalRESTException
   {
      // Payment.initConfig(new File("src/main/test/resources/sdk_config.properties"));

      PaypalConfiguration paypalConfiguration = new PaypalConfiguration();
      // # service.EndPoint=https://api.paypal.com
      paypalConfiguration.setService_EndPoint("https://api.sandbox.paypal.com");
      //
      // # Credentials
      // # Credentials
      // clientID=AZxEyxAdj25PxMiCZYu4-xnBuzejS7qcKRo-7Ffdm0BgBxkob3F6_Iz-b3F2
      paypalConfiguration.setClientID("AZxEyxAdj25PxMiCZYu4-xnBuzejS7qcKRo-7Ffdm0BgBxkob3F6_Iz-b3F2");
      // clientSecret=EM3yExCdMmacgKHWDNUHMyK1fsn0pSAPuEUm1kRfZ5JX17JvMbnqzkdIWBHr
      paypalConfiguration.setClientSecret("EM3yExCdMmacgKHWDNUHMyK1fsn0pSAPuEUm1kRfZ5JX17JvMbnqzkdIWBHr");
      Payment.initConfig(getPropertiesFromPaypalConfiguration(paypalConfiguration));
      APIContext apiContext = null;
      String accessToken = null;
      try
      {
         accessToken = PaypalAccountUtils.getAccessToken(paypalConfiguration);

         // ### Api Context
         // Pass in a `ApiContext` object to authenticate
         // the call and to send a unique request id
         // (that ensures idempotency). The SDK generates
         // a request id if you do not pass one explicitly.
         apiContext = new APIContext(accessToken);
         // Use this variant if you want to pass in a request id
         // that is meaningful in your application, ideally
         // a order id.
         /*
          * String requestId = Long.toString(System.nanoTime(); APIContext apiContext = new APIContext(accessToken,
          * requestId ));
          */

         // ###Details
         // Let's you specify details of a payment amount.
         Details details = new Details();
         details.setShipping("1");
         details.setSubtotal("5");
         details.setTax("1");

         // ###Amount
         // Let's you specify a payment amount.
         Amount amount = new Amount();
         amount.setCurrency("EUR");
         // Total must be equal to sum of shipping, tax and subtotal.
         amount.setTotal("7");
         amount.setDetails(details);

         // ###Transaction
         // A transaction defines the contract of a
         // payment - what is the payment for and who
         // is fulfilling it. Transaction is created with
         // a `Payee` and `Amount` types
         Transaction transaction = new Transaction();
         transaction.setAmount(amount);
         transaction
                  .setDescription("This is the payment transaction description.");

         // The Payment creation API requires a list of
         // Transaction; add the created `Transaction`
         // to a List
         List<Transaction> transactions = new ArrayList<Transaction>();
         transactions.add(transaction);

         // ###Payer
         // A resource representing a Payer that funds a payment
         // Payment Method
         // as 'paypal'
         Payer payer = new Payer();
         payer.setPaymentMethod("paypal");

         // ###Payment
         // A Payment Resource; create one using
         // the above types and intent as 'sale'
         Payment payment = new Payment();
         payment.setIntent("sale");
         payment.setPayer(payer);
         payment.setTransactions(transactions);

         // ###Redirect URLs
         RedirectUrls redirectUrls = new RedirectUrls();
         String guid = UUID.randomUUID().toString().replaceAll("-", "");
         redirectUrls.setCancelUrl("http://giavacms.org/paymentwithpaypal?guid=" + guid);
         redirectUrls.setReturnUrl("http://giavacms.org/paymentwithpaypal?guid=" + guid);
         payment.setRedirectUrls(redirectUrls);

         // Create a payment by posting to the APIService
         // using a valid AccessToken
         // The return object contains the status;
         try
         {
            Payment createdPayment = payment.create(apiContext);
            LOGGER.info("Created payment with id = "
                     + createdPayment.getId() + " and status = "
                     + createdPayment.getState());
            // ###Payment Approval Url
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext())
            {
               Links link = links.next();
               if (link.getRel().equalsIgnoreCase("approval_url"))
               {
                  System.out.println(link.getHref());
               }
            }
            System.out.println(Payment.getLastResponse());
            map.put(guid, createdPayment.getId());
         }
         catch (PayPalRESTException e)
         {
            e.printStackTrace();
         }

         System.out.println(Payment.getLastRequest());

      }
      catch (PayPalRESTException e)
      {
         e.printStackTrace();
      }
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
