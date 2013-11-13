package org.giavacms.paypalweb.service;

import org.giavacms.paypalweb.model.ShoppingCart;

public interface ShippingService
{
   public double calculate(ShoppingCart shoppingCart);
}
