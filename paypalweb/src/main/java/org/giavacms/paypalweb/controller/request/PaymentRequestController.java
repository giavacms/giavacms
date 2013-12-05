package org.giavacms.paypalweb.controller.request;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.service.NotificationService;
import org.giavacms.paypalweb.util.FaceletRenderer;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class PaymentRequestController implements Serializable
{
   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getName());

   String paymentId;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   ShoppingCartRequestController shoppingCartRequestController;

   @Inject
   NotificationService notificationService;

   public void verifyConfirm()
   {
      if (paymentId != null && !paymentId.isEmpty())
      {
         logger.info("ID to confirm: " + paymentId);
         ShoppingCart shoppingCart = shoppingCartRepository.find(Long.valueOf(paymentId));
         if (shoppingCart != null && shoppingCart.getConfirmDate() == null)
         {
            shoppingCart.setConfirmDate(new Date());
            shoppingCart.setConfirmed(true);
            shoppingCartRepository.update(shoppingCart);
            logger.info("update shopping cart with confirm=true");
            notificationService.notifyPayment(shoppingCart);
            shoppingCartRequestController.reset();
         }
         else
         {
            logger.info("NO SHOPPINGCART OR PREVIUOUSLY CONFIRMED!");
         }
      }
      else
      {
         logger.info("verifyConfirm: NO ID!");
      }
   }

   public void verifyExit()
   {
      if (paymentId != null && !paymentId.isEmpty())
      {
         logger.info("ID TO NOT CONFIRMED: " + paymentId);
         ShoppingCart shoppingCart = shoppingCartRepository.find(Long.valueOf(paymentId));
         if (shoppingCart != null && shoppingCart.getConfirmDate() == null)
         {
            shoppingCart.setConfirmDate(new Date());
            shoppingCart.setConfirmed(false);
            shoppingCartRepository.update(shoppingCart);
            logger.info("update shopping cart with confirm=false");
            shoppingCartRequestController.reset();
         }
         else
         {
            logger.info("NO SHOPPINGCART OR PREVIUOUSLY CONFIRMED!");
         }
      }
      else
      {
         logger.info("verifyExit: NO ID!");
      }
   }

   public void test(String path)
   {
      logger.info(FaceletRenderer.renderView(path));
   }

   public String getPaymentId()
   {
      return paymentId;
   }

   public void setPaymentId(String paymentId)
   {
      this.paymentId = paymentId;
   }

}
