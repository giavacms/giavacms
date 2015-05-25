/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.servlet;

import org.giavacms.paypalweb.model.IpnContent;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.model.enums.PaypalStatus;
import org.giavacms.paypalweb.repository.IpnContentRepository;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.service.NotificationService;
import org.giavacms.paypalweb.util.IpnUtils;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Named
@Stateless
public class IpnRequestService implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Logger logger = Logger.getLogger(getClass());

   @Inject
   PaypalConfigurationRepository paypalConfigurationRepository;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   IpnContentRepository ipnContentRepository;

   @Inject
   NotificationService notificationService;

   boolean completed = false;

   public void handleIpn(HttpServletRequest request) throws Exception
   {
      logger.info("inside IpnController.handleIpn");
      // 1. Read all posted request parameters
      // 2. Prepare 'notify-validate' command with exactly the same parameters
      // 3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
      // 4. Read response from Paypal
      String res;
      try
      {
         PaypalConfiguration paypalConf = paypalConfigurationRepository.load();
         res = IpnUtils.postToPaypal(request, paypalConf.getServiceUrl());
         logger.info("IpnRequestController.handleIpn - postToPaypal reponse: " + res);
         // 5. Capture Paypal IPN information
         IpnContent ipnContent = IpnUtils.fromRequest(request, res);
         ipnContentRepository.persist(ipnContent);
         logger.info(ipnContent.getId());
         // 6. Validate captured Paypal IPN Information
         if (res.equals("VERIFIED"))
         {
            logger.info("Paypal IPN Information: VERIFIED");
            completed = true;
            // 6.1. Check the existence of shoppingCart with custom ID
            ShoppingCart shoppingCart = shoppingCartRepository.find(Long.parseLong(ipnContent.getCustom()));
            if (shoppingCart == null)
            {
               completed = false;
               logger.info("doesn't exist shoppingCart with custom id {" + ipnContent.getCustom() + "}");
            }

            PaypalStatus paypalStatus = PaypalStatus.get(ipnContent.getPaymentStatus());
            if (paypalStatus == null)
            {
               completed = false;
               logger.info("payment_status IS NULLL ");
            }
            else
            {
               shoppingCart.setPaypalStatus(paypalStatus);
               logger.info("payment_status IS " + paypalStatus.name());
            }

            // 6.2. Check that txnId has not been previously processed
            IpnContent oldIpnInfo = ipnContentRepository.findByTxnId(ipnContent.getTxnId(), ipnContent.getId());
            if (oldIpnInfo != null)
            {
               completed = false;
               logger.info("txn_id is already processed {old ipn_info " + oldIpnInfo.getId());
            }
            // 6.3. Check that receiverEmail matches with configured {@link IpnConfig#receiverEmail}
            if (!ipnContent.getReceiverEmail().equalsIgnoreCase(paypalConf.getEmail()))
            {
               completed = false;
               logger.info("receiver_email " + ipnContent.getReceiverEmail()
                        + " does not match with configured ipn email "
                        + paypalConf.getEmail());
            }
            //
            // // 6.4. Check that paymentAmount matches with configured {@link IpnConfig#paymentAmount}
            if ((paypalStatus.equals(PaypalStatus.Completed) || paypalStatus.equals(PaypalStatus.Pending))
                     && Double.parseDouble(ipnContent.getPaymentAmount()) != shoppingCart
                     .getTotalWithSipping())
            {
               completed = false;
               logger.info("payment amount mc_gross " + ipnContent.getPaymentAmount()
                        + " does not match with configured ipn amount " + shoppingCart
                        .getTotalWithSipping());
            }
            if (paypalStatus.equals(PaypalStatus.Refunded)
                     && Double.parseDouble(ipnContent.getPaymentAmount()) != (-shoppingCart
                     .getTotalWithSipping()))
            {
               completed = false;
               logger.info("payment amount mc_gross " + ipnContent.getPaymentAmount()
                        + " does not match with configured ipn amount " + shoppingCart
                        .getTotalWithSipping());
            }
            // // 6.5. Check that paymentCurrency matches with configured {@link IpnConfig#paymentCurrency}
            if (!ipnContent.getPaymentCurrency().equalsIgnoreCase(shoppingCart.getCurrency()))
            {
               completed = false;
               logger.info("payment currency mc_currency " + ipnContent.getPaymentCurrency()
                        + " does not match with configured ipn currency " + shoppingCart.getCurrency());
            }
            if (completed)
            {
               switch (paypalStatus)
               {
               case Completed:
                  logger.info("payment_status IS COMPLETED");
                  logger.info("completed");
                  logger.info("update shopping cart: confirmed");
                  shoppingCart.setCompletedDate(new Date());
                  shoppingCart.setLogId(ipnContent.getId());
                  shoppingCartRepository.update(shoppingCart);
                  notificationService.notifyCompleted(shoppingCart);
                  break;
               case Pending:
                  logger.info("payment_status IS PENDING");
                  if (paypalConf.isLogOnly())
                  {
                     // IN TEST IF YOU USE REAL ACCOUNT TO PAY, PAYPAL RESPONSE WITH Pending STATUS
                     logger.info("IN TEST - payment_status IS COMPLETED");
                     logger.info("completed");
                     logger.info("update shopping cart: confirmed");
                     shoppingCart.setCompletedDate(new Date());
                     shoppingCart.setLogId(ipnContent.getId());
                     shoppingCartRepository.update(shoppingCart);
                     notificationService.notifyCompleted(shoppingCart);
                  }
                  break;
               case Refunded:
                  logger.info("payment_status IS REFUNDED");
                  shoppingCart.setRefundedDate(new Date());
                  shoppingCartRepository.update(shoppingCart);
                  notificationService.notifyRefunded(shoppingCart);
                  break;
               case Canceled_Reversal:
               case Created:
               case Denied:
               case Expired:
               case Failed:
               case Reversed:
               case Processed:
               case Voided:
                  logger.info("payment_status IS " + paypalStatus.name());
                  shoppingCart.setNotCompletedDate(new Date());
                  shoppingCartRepository.update(shoppingCart);
                  break;
               }

            }
            else
            {
               logger.info("not completed");

               logger.info("update shopping cart: not confirmed");
               shoppingCart.setNotCompletedDate(new Date());
               shoppingCartRepository.update(shoppingCart);

            }
         }
         else
         {
            logger.info("Invalid response {" + res + "} expecting {VERIFIED}");
         }
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
