package org.giavacms.paypalweb.service;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.base.service.EmailSession;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;

@Stateless
@LocalBean
public class NotificationService
{
   @Inject
   EmailSession emailSession;

   @Inject
   PaypalConfigurationRepository paypalConfigurationRepository;

   @Asynchronous
   public void notifyPayment(ShoppingCart shoppingCart)
   {
      PaypalConfiguration paypalConfiguration = getPaypalConfiguration();
      // emailSession.sendEmail(paypalConfiguration.getEmailSender(), body, title, to, cc, bcc, file);
   }

   public PaypalConfiguration getPaypalConfiguration()
   {
      return paypalConfigurationRepository.load();
   }
}
