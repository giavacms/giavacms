package org.giavacms.paypalweb.service;

import org.giavacms.paypalweb.model.ShoppingCart;

public interface PaypalSubmitService
{
   public boolean isReady(ShoppingCart shoppingCart);
}
