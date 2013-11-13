package org.giavacms.paypalweb.controller.request;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;
import org.giavacms.paypalweb.producer.PaypallProducer;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.jboss.logging.Logger;

@Named
@RequestScoped
public class PaymentRequestController
{

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   @Inject
   PaypallProducer paypallProducer;

   Logger logger = Logger.getLogger(getClass().getName());

   public PaymentRequestController()
   {
   }

}
