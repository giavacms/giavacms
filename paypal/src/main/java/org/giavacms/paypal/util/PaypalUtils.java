package org.giavacms.paypal.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.giavacms.paypal.model.PaypalConfiguration;
import org.giavacms.paypal.model.ShoppingArticle;
import org.giavacms.paypal.model.ShoppingCart;
import org.jboss.logging.Logger;

import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Sale;
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

   public static void end(ShoppingCart shoppingCart, String payerId, String guid,
            PaypalConfiguration paypalConfiguration)
            throws PayPalRESTException, ParseException
   {
      Payment payment = new Payment();
      payment.setId(shoppingCart.getPaymentId());
      PaymentExecution paymentExecution = new PaymentExecution();
      paymentExecution.setPayerId(payerId);
      Payment result = payment.execute(getAPIContext(guid, paypalConfiguration), paymentExecution);

      if (result != null && result.getPayer() != null && result.getPayer().getPayerInfo() != null)
      {
         setPayerInfo(shoppingCart, result.getPayer().getPayerInfo());
         if (result.getUpdateTime() != null && !result.getUpdateTime().isEmpty())
         {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            shoppingCart.setDataStart(df.parse(result.getUpdateTime()));

         }
      }
      getSaleInfo(shoppingCart.getPaymentId(), paypalConfiguration);
      System.out.println("END RESULT: " + result.toJSON());
   }

   private static void getSaleInfo(String id, PaypalConfiguration paypalConfiguration)
   {
      try
      {
         Sale sale = Sale.get(PaypalAccountUtils.getAccessToken(paypalConfiguration), id);
         System.out.println(sale.toJSON());
      }
      catch (PayPalRESTException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static void init(PaypalConfiguration paypalConfiguration, ShoppingCart shoppingCart,
            boolean addShippingAddress, boolean addPayerInfo) throws Exception
   {
      DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(java.util.Locale.ENGLISH);
      decimalFormat.applyLocalizedPattern("#,###,##0.00");
      decimalFormat.setGroupingUsed(false);
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
      double shipping = Double.valueOf(shoppingCart.getShipping());
      ItemList itemList = new ItemList();
      itemList.setItems(new ArrayList<Item>());
      int i = 0;
      for (ShoppingArticle article : shoppingCart.getShoppingArticles())
      {
         i++;
         logger.info("article" + i + ") " + article.toString());
         itemList.getItems().add(new Item("" + article.getQuantity(), article.getDescription(), article.getPrice(),
                  shoppingCart.getCurrency()));
         totalAmount += Double.valueOf(article.getQuantity()) * Double.valueOf(article.getPrice());
         totalTax += Double.valueOf(article.getQuantity()) * Double.valueOf(article.getVat());
      }

      double total = totalAmount + totalTax + shipping;
      logger.info("total: " + decimalFormat.format(total) + " - totalAmount: " + decimalFormat.format(totalAmount)
               + " - totalTax: "
               + decimalFormat.format(totalTax) + " - shipping:"
               + decimalFormat.format(shipping));
      // ###Details
      // Let's you specify details of a payment amount.
      Details details = new Details();
      details.setShipping(decimalFormat.format(shipping));
      details.setSubtotal(decimalFormat.format(totalAmount));
      details.setTax(decimalFormat.format(totalTax));

      // ###Amount
      // Let's you specify a payment amount.
      // Total must be equal to sum of shipping, tax and subtotal.
      Amount amount = new Amount("EUR", decimalFormat.format(total));
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
      if (addPayerInfo)
         payer.setPayerInfo(getPayerInfo(shoppingCart, addShippingAddress));
      else
         payer.setPayerInfo(new PayerInfo());
      // ###Payment
      // A Payment Resource; create one using
      // the above types and intent as 'sale'
      Payment payment = new Payment();
      payment.setIntent("sale");
      payment.setTransactions(transactions);
      payment.setPayer(payer);
      // Payment payment = new Payment("sale", payer, transactions);

      // ###Redirect URLs
      RedirectUrls redirectUrls = new RedirectUrls();
      // String guid = UUID.randomUUID().toString().replaceAll("-", "");
      redirectUrls.setCancelUrl(paypalConfiguration.getCancelUrl() + "?guid="
               + shoppingCart.getId());
      redirectUrls.setReturnUrl(paypalConfiguration.getReturnUrl() + "?guid="
               + shoppingCart.getId());
      payment.setRedirectUrls(redirectUrls);

      logger.info("CancelUrl: " + redirectUrls.getCancelUrl());

      logger.info("ReturnUrl: " + redirectUrls.getReturnUrl());

      // Create a payment by posting to the APIService
      // using a valid AccessToken
      // The return object contains the status;
      Payment createdPayment = payment.create(getAPIContext("" + shoppingCart.getId(), paypalConfiguration));
      logger.info("Created payment with id = "
               + createdPayment.getId() + " and status = "
               + createdPayment.getState());
      logger.info("JSON RESPONSE: " + createdPayment.toJSON());
      if (createdPayment.getState().equalsIgnoreCase("created"))
      {
         shoppingCart.setPaymentId(createdPayment.getId());
         if (createdPayment.getCreateTime() != null && !createdPayment.getCreateTime().isEmpty())
         {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            shoppingCart.setDataStart(df.parse(createdPayment.getCreateTime()));
         }
         logger.info("payment created!!");
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
               logger.info("approval_url: " + shoppingCart.getApprovalUrl());
            }
            if (link.getRel().equalsIgnoreCase("self"))
            {
               shoppingCart.setSelfUrl(link.getHref());
               logger.info("self: " + shoppingCart.getSelfUrl());
            }
            if (link.getRel().equalsIgnoreCase("execute"))
            {
               shoppingCart.setExecuteUrl(link.getHref());
               logger.info("execute: " + shoppingCart.getExecuteUrl());
            }
         }
      }
      logger.info(shoppingCart.toString());

   }

   private static void setPayerInfo(ShoppingCart shoppingCart, PayerInfo payerInfo)
   {
      shoppingCart.getPayerInfo().setEmail(payerInfo.getEmail());
      shoppingCart.getPayerInfo().setFirstName(payerInfo.getFirstName());
      shoppingCart.getPayerInfo().setLastName(payerInfo.getLastName());
      shoppingCart.getPayerInfo().setPhone(payerInfo.getPhone());
      if (payerInfo.getShippingAddress() != null)
      {
         shoppingCart.getPayerInfo().getAddress().setCity(payerInfo.getShippingAddress().getCity());
         shoppingCart.getPayerInfo().getAddress().setCountryCode(payerInfo.getShippingAddress().getCountryCode());
         shoppingCart.getPayerInfo().getAddress().setLine1(payerInfo.getShippingAddress().getLine1());
         shoppingCart.getPayerInfo().getAddress().setLine2(payerInfo.getShippingAddress().getLine2());
         shoppingCart.getPayerInfo().getAddress().setPhone(payerInfo.getShippingAddress().getPhone());
         shoppingCart.getPayerInfo().getAddress().setPostalCode(payerInfo.getShippingAddress().getPostalCode());
         shoppingCart.getPayerInfo().getAddress().setState(payerInfo.getShippingAddress().getState());
      }
   }

   private static PayerInfo getPayerInfo(ShoppingCart shoppingCart, boolean addShippingAddress)
   {
      PayerInfo payerInfo = new PayerInfo();
      payerInfo.setEmail(shoppingCart.getPayerInfo().getEmail());
      payerInfo.setFirstName(shoppingCart.getPayerInfo().getFirstName());
      payerInfo.setLastName(shoppingCart.getPayerInfo().getLastName());
      payerInfo.setPhone(shoppingCart.getPayerInfo().getPhone());
      if (addShippingAddress)
      {
         Address shippingAddress = new Address();
         shippingAddress.setCity(shoppingCart.getPayerInfo().getAddress().getCity());
         shippingAddress.setCountryCode(shoppingCart.getPayerInfo().getAddress().getCountryCode());
         shippingAddress.setLine1(shoppingCart.getPayerInfo().getAddress().getLine1());
         shippingAddress.setLine2(shoppingCart.getPayerInfo().getAddress().getLine2());
         shippingAddress.setPhone(shoppingCart.getPayerInfo().getAddress().getPhone());
         shippingAddress.setPostalCode(shoppingCart.getPayerInfo().getAddress().getPostalCode());
         shippingAddress.setState(shoppingCart.getPayerInfo().getAddress().getState());
         payerInfo.setShippingAddress(shippingAddress);
      }
      return payerInfo;
   }

   public static void main(String[] args)
   {
      DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(java.util.Locale.ENGLISH);
      decimalFormat.applyLocalizedPattern("#,###,##0.00");
      decimalFormat.setGroupingUsed(false);
      double total = 23450.2222D;
      System.out.println(decimalFormat.format(total));
   }
}
