/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.service;

import org.giavacms.paypalweb.model.ShoppingCart;

public interface ShippingService
{
   public double calculate(ShoppingCart shoppingCart);
}
