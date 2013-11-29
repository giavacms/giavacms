package org.giavacms.paypalweb.controller.request;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.paypalweb.model.IpnContent;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.IpnContentRepository;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.util.IpnUtils;
import org.jboss.logging.Logger;

@RequestScoped
@Named
public class IpnController implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Logger logger = Logger.getLogger(getClass());

   @Inject
   PaypalConfiguration paypalConfiguration;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   IpnContentRepository ipnContentRepository;

   boolean completed = false;

   public void handleIpn(HttpServletRequest request) throws IOException
   {
      logger.info("inside IpnController.handleIpn");
      // 1. Read all posted request parameters
      // 2. Prepare 'notify-validate' command with exactly the same parameters
      // 3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
      // 4. Read response from Paypal
      String res = IpnUtils.postToPaypal(request, paypalConfiguration.getIpnUrl());
      logger.info("postToPaypal reponse: " + res);
      // 5. Capture Paypal IPN information
      IpnContent ipnContent = IpnUtils.fromRequest(request, res);
      ipnContentRepository.persist(ipnContent);
      logger.info(ipnContent);
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
         // 6.1. Check that paymentStatus=Completed
         if (ipnContent.getPaymentStatus() == null || !ipnContent.getPaymentStatus().equalsIgnoreCase("COMPLETED"))
         {
            completed = false;
            logger.info("payment_status IS NOT COMPLETED {" + ipnContent.getPaymentStatus() + "}");
         }
         // 6.2. Check that txnId has not been previously processed
         IpnContent oldIpnInfo = ipnContentRepository.findByTxnId(ipnContent.getTxnId());
         if (oldIpnInfo != null)
         {
            completed = false;
            logger.info("txn_id is already processed {old ipn_info " + oldIpnInfo);
         }
         // 6.3. Check that receiverEmail matches with configured {@link IpnConfig#receiverEmail}
         if (!ipnContent.getReceiverEmail().equalsIgnoreCase(paypalConfiguration.getEmail()))
         {
            completed = false;
            logger.info("receiver_email " + ipnContent.getReceiverEmail()
                     + " does not match with configured ipn email " + paypalConfiguration.getEmail());
         }
         //
         // // 6.4. Check that paymentAmount matches with configured {@link IpnConfig#paymentAmount}
         if (Double.parseDouble(ipnContent.getPaymentAmount()) != shoppingCart
                  .getTotal())
         {
            completed = false;
            logger.info("payment amount mc_gross " + ipnContent.getPaymentAmount()
                     + " does not match with configured ipn amount " + shoppingCart
                              .getTotal());
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
            logger.info("completed");
            /*
             * $totale = $totale + $sped - $total; if ($totale == 0) { $pagato = $total; $stato = "in consegna"; } else
             * { $pagato = $total; $stato = "pagamento scorretto"; } $tot_agg = array('stato' => q($stato), 'pagato'
             * =>q($pagato)); $where = " id = '".$cartid."'"; UpdateArray("acquisti", $tot_agg, $where);
             */
            if (shoppingCart != null)
            {
               logger.info("update shopping cart: confirmed");
               shoppingCart.setConfirmDate(new Date());
               shoppingCart.setConfirmed(true);
               shoppingCart.setLogId(ipnContent.getId());
               shoppingCartRepository.update(shoppingCart);
            }
            else
            {
               logger.info("update shopping cart: NOT FOUND");
            }
         }
         else
         {
            logger.info("not completed");
            if (shoppingCart != null)
            {
               logger.info("update shopping cart: not confirmed");
               shoppingCart.setConfirmDate(new Date());
               shoppingCart.setConfirmed(false);
               shoppingCartRepository.update(shoppingCart);
            }
            else
            {
               logger.info("update shopping cart: NOT FOUND");
            }
         }
      }
      else
      {
         logger.info("Inavlid response {" + res + "} expecting {VERIFIED}");
      }

   }
}
