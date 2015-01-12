/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.service;

import java.io.File;
import java.util.Arrays;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.base.service.EmailSession;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;
import org.giavacms.paypalweb.util.PlaceholderUtils;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class NotificationService
{
   Logger logger = Logger.getLogger(getClass().getName());
   @Inject
   EmailSession emailSession;

   @Inject
   PaypalConfigurationRepository paypalConfigurationRepository;

   @Asynchronous
   public void notifyCompleted(ShoppingCart shoppingCart)
   {
      PaypalConfiguration paypalConfiguration = paypalConfigurationRepository.load();
      String from = paypalConfiguration.getEmailSender();
      String body = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailBody(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String title = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailObject(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String[] to = new String[] { shoppingCart.getBillingAddress().getEmail() };
      String[] cc = null;
      String[] bcc = (paypalConfiguration.getEmailNotification() != null && paypalConfiguration.getEmailNotification()
               .length() > 0) ? paypalConfiguration.getEmailNotification().split(";") : new String[] {};
      File file = null;
      if (!paypalConfiguration.isLogOnly())
         emailSession.sendEmail(from, body, title, to, cc, bcc, file);
      else
         log(shoppingCart.getPaypalStatus().name(), from, body, title, to, cc, bcc);
   }

   @Asynchronous
   public void notifySent(ShoppingCart shoppingCart)
   {
      PaypalConfiguration paypalConfiguration = paypalConfigurationRepository.load();
      String from = paypalConfiguration.getEmailSender();
      String body = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailShipmentBody(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String title = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailShipmentObject(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String[] to = new String[] { shoppingCart.getBillingAddress().getEmail() };
      String[] cc = null;
      String[] bcc = (paypalConfiguration.getEmailNotification() != null && paypalConfiguration.getEmailNotification()
               .length() > 0) ? paypalConfiguration.getEmailNotification().split(";") : new String[] {};
      File file = null;
      if (!paypalConfiguration.isLogOnly())
         emailSession.sendEmail(from, body, title, to, cc, bcc, file);
      else
         log(shoppingCart.getPaypalStatus().name(), from, body, title, to, cc, bcc);
   }

   @Asynchronous
   public void notifyRefunded(ShoppingCart shoppingCart)
   {
      PaypalConfiguration paypalConfiguration = paypalConfigurationRepository.load();
      String from = paypalConfiguration.getEmailSender();
      String body = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailRollBackBody(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String title = PlaceholderUtils.replaceAll(paypalConfiguration.getEmailRollBackObject(), shoppingCart,
               paypalConfiguration.getShoppingCartDirectUrl());
      String[] to = new String[] { shoppingCart.getBillingAddress().getEmail() };
      String[] cc = null;
      String[] bcc = (paypalConfiguration.getEmailNotification() != null && paypalConfiguration.getEmailNotification()
               .length() > 0) ? paypalConfiguration.getEmailNotification().split(";") : new String[] {};
      File file = null;
      if (!paypalConfiguration.isLogOnly())
         emailSession.sendEmail(from, body, title, to, cc, bcc, file);
      else
         log(shoppingCart.getPaypalStatus().name(), from, body, title, to, cc, bcc);
   }

   private void log(String type, String from, String body, String title, String[] to, String[] cc, String[] bcc)
   {
      logger.info("EMAIL FOR SHOPPING CART TYPE: " + type);
      logger.info("from: " + from);
      logger.info("body: " + body);
      logger.info("title: " + title);
      logger.info("to: " + Arrays.toString(to));
      logger.info("cc: " + Arrays.toString(to));
      logger.info("bcc:" + Arrays.toString(to));
   }
}
