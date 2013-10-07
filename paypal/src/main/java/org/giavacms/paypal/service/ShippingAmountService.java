package org.giavacms.paypal.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.giavacms.paypal.model.ShoppingCart;

@Stateless
@LocalBean
public class ShippingAmountService
{
   public String calculate(ShoppingCart shoppingCart)
   {

      return "0";
   }
}
