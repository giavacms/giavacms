package org.giavacms.paypal.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.giavacms.common.util.JSFUtils;
import org.giavacms.paypal.model.PaypalConfiguration;
import org.giavacms.paypal.model.ShoppingArticle;
import org.giavacms.paypal.model.ShoppingCart;
import org.jboss.logging.Logger;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

public class PaypalUtils
{

   static Logger logger = Logger.getLogger(PaypalUtils.class);

   public static APIContext getAPIContext(String requestId, PaypalConfiguration paypalConfiguration)
            throws PayPalRESTException
   {
      APIContext apiContext;
      if (requestId != null && !requestId.isEmpty())
      {
         apiContext = new APIContext(PaypalAccountUtils.getAccessToken(paypalConfiguration), requestId);
      }
      else
      {
         apiContext = new APIContext(PaypalAccountUtils.getAccessToken(paypalConfiguration));
      }
      return apiContext;
   }

   public static void end(String payerId, String guid, PaypalConfiguration paypalConfiguration)
            throws PayPalRESTException
   {
      Payment payment = new Payment();
      PaymentExecution paymentExecution = new PaymentExecution();
      paymentExecution.setPayerId(payerId);
      payment.execute(getAPIContext(guid, paypalConfiguration), paymentExecution);
      System.out.println(Payment.getLastResponse());
   }

   public static void init(PaypalConfiguration paypalConfiguration, ShoppingCart shoppingCart) throws Exception
   {

      PayPalResource.initConfig(PaypalAccountUtils.getPropertiesFromPaypalConfiguration(paypalConfiguration));

      // ### Api Context
      // Pass in a `ApiContext` object to authenticate
      // the call and to send a unique request id
      // (that ensures idempotency). The SDK generates
      // a request id if you do not pass one explicitly.

      // apiContext = new APIContext(accessToken);

      // Use this variant if you want to pass in a request id
      // that is meaningful in your application, ideally
      // a order id.

      // APIContext apiContext = new APIContext(PaypalAccountUtils.getAccessToken(), "" + shoppingCart.getId());
      // The Payment creation API requires a list of
      // Transaction; add the created `Transaction`
      // to a List
      List<Transaction> transactions = new ArrayList<Transaction>();
      double totalAmount = 0;
      double totalTax = 0;
      ItemList itemList = new ItemList();
      itemList.setItems(new ArrayList<Item>());
      for (ShoppingArticle article : shoppingCart.getShoppingArticles())
      {
         itemList.getItems().add(new Item("" + article.getQuantity(), article.getDescription(), article.getPrice(),
                  shoppingCart.getCurrency()));
         totalAmount += Double.valueOf(article.getPrice());
         totalTax += Double.valueOf(article.getVat());
      }
      double total = totalAmount + totalTax + Double.valueOf(shoppingCart.getShipping());
      // ###Details
      // Let's you specify details of a payment amount.
      Details details = new Details();
      details.setShipping(shoppingCart.getShipping());
      details.setSubtotal("" + totalAmount);
      details.setTax("" + totalTax);

      // ###Amount
      // Let's you specify a payment amount.
      // Total must be equal to sum of shipping, tax and subtotal.
      Amount amount = new Amount("EUR", "" + total);
      amount.setDetails(details);

      // ###Transaction
      // A transaction defines the contract of a
      // payment - what is the payment for and who
      // is fulfilling it. Transaction is created with
      // a `Payee` and `Amount` types
      Transaction transaction = new Transaction(amount);

      transaction.setItemList(itemList)
               .setDescription("This is the payment transaction description.");
      transactions.add(transaction);

      // ###Payer
      // A resource representing a Payer that funds a payment
      // Payment Method
      // as 'paypal'
      Payer payer = new Payer("paypal");

      // ###Payment
      // A Payment Resource; create one using
      // the above types and intent as 'sale'
      Payment payment = new Payment("sale", payer, transactions);

      // ###Redirect URLs
      RedirectUrls redirectUrls = new RedirectUrls();
      // String guid = UUID.randomUUID().toString().replaceAll("-", "");
      redirectUrls.setCancelUrl(JSFUtils.getAbsolutePath() + paypalConfiguration.getCancelUrl() + "?guid="
               + shoppingCart.getId());
      redirectUrls.setReturnUrl(JSFUtils.getAbsolutePath() + paypalConfiguration.getReturnUrl() + "?guid="
               + shoppingCart.getId());
      payment.setRedirectUrls(redirectUrls);

      // Create a payment by posting to the APIService
      // using a valid AccessToken
      // The return object contains the status;
      Payment createdPayment = payment.create(getAPIContext("" + shoppingCart.getId(), paypalConfiguration));
      logger.info("Created payment with id = "
               + createdPayment.getId() + " and status = "
               + createdPayment.getState());
      if (createdPayment.getState().equalsIgnoreCase("created"))
      {
         shoppingCart.setCreated(true);
         // ###Payment Approval Url
         Payment.getLastResponse();
         // map.put(guid, createdPayment.getId());
         // ###Payment Approval Url
         Iterator<Links> links = createdPayment.getLinks().iterator();
         while (links.hasNext())
         {
            Links link = links.next();
            if (link.getRel().equalsIgnoreCase("approval_url"))
            {
               shoppingCart.setApprovalUrl(link.getHref());
            }
            if (link.getRel().equalsIgnoreCase("self"))
            {
               shoppingCart.setSelfUrl(link.getHref());
            }
            if (link.getRel().equalsIgnoreCase("execute"))
            {
               shoppingCart.setExecuteUrl(link.getHref());
            }
         }
      }

   }
}
