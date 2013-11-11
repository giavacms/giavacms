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

   private static final Logger logger = Logger
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
      Payment.initConfig(PaypalAccountUtils.getPropertiesFromPaypalConfiguration(paypalConfiguration));
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
            logger.info("Created payment with id = "
                     + createdPayment.getId() + " and status = "
                     + createdPayment.getState());
            // ###Payment Approval Url
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext())
            {
               Links link = links.next();
               if (link.getRel().equalsIgnoreCase("approval_url"))
               {
                  logger.info(link.getHref());
               }
            }
            logger.info(Payment.getLastResponse());
            map.put(guid, createdPayment.getId());
         }
         catch (PayPalRESTException e)
         {
            e.printStackTrace();
         }

         logger.info(Payment.getLastRequest());

      }
      catch (PayPalRESTException e)
      {
         e.printStackTrace();
      }
   }

}
