package org.giavacms.paypalweb.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class PaypalButtonController
{

   @Inject
   ShoppingCartController shoppingCartController;

   public PaypalButtonController()
   {
   }

   public String getButton()
   {

      return "";
   }
}
