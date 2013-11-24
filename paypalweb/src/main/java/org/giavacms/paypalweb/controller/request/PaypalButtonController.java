package org.giavacms.paypalweb.controller.request;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.paypalweb.controller.ShoppingCartController;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.util.ButtonUtils;

@RequestScoped
@Named
public class PaypalButtonController
{

   @Inject
   ShoppingCartController shoppingCartController;

   @Inject
   PaypalConfiguration paypalConfiguration;

   public PaypalButtonController()
   {
   }

   public String getButton()
   {

      return ButtonUtils.generate(shoppingCartController.getElement(),
               paypalConfiguration.getServiceUrl(), paypalConfiguration.getEmail(),
               paypalConfiguration.getIpnUrl(), paypalConfiguration.getCancelUrl(), paypalConfiguration.getReturnUrl());
   }
}
