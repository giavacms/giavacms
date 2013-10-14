package org.giavacms.paypal.controller.request;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypal.model.ShoppingCart;
import org.giavacms.paypal.producer.PaypallProducer;
import org.giavacms.paypal.repository.ShoppingCartRepository;
import org.giavacms.paypal.util.PaypalUtils;
import org.jboss.logging.Logger;

import com.paypal.core.rest.PayPalRESTException;

@Named
@RequestScoped
public class PaymentRequestController
{
   private String payerId;
   private String guid;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   PaypallProducer paypallProducer;
   
   Logger logger = Logger.getLogger(getClass().getName());

   public PaymentRequestController()
   {
   }

   public String verify()
   {
      if (getPayerId() != null && !getPayerId().isEmpty() && getGuid() != null && !getGuid().isEmpty())
      {
         // verifico che un ordine con quel guid exists
         ShoppingCart shoppingCart = shoppingCartRepository.find(Long.valueOf(getGuid()));
         if (shoppingCart != null)
            try
            {
               PaypalUtils.end(getPayerId(), getGuid(), paypallProducer.getPaypalConfiguration());
            }
            catch (PayPalRESTException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         else
            logger.info("NON ESISTE SHOPPING CART!!!");
      }
      return "";
   }

   public String getPayerId()
   {
      return payerId;
   }

   public void setPayerId(String payerId)
   {
      this.payerId = payerId;
   }

   public String getGuid()
   {
      return guid;
   }

   public void setGuid(String guid)
   {
      this.guid = guid;
   }
}
