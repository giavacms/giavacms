/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.util;

import java.util.List;

import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.ShoppingArticle;
import org.giavacms.paypalweb.model.ShoppingCart;

public class ButtonUtils
{
   private static String style = "class='btn btn-success' style='width: 150px;'";

   public static String generate(ShoppingCart shoppingCart, PaypalConfiguration paypalConfiguration)
   {

      StringBuffer html = new StringBuffer();
      addIntro(html, paypalConfiguration.getServiceUrl(), paypalConfiguration.getEmail());
      articlesToHtml(shoppingCart.getShoppingArticles(), html, shoppingCart.getShipping().doubleValue());
      payerToHtml(html, shoppingCart.getId(), paypalConfiguration.getIpnUrl(), paypalConfiguration.getCancelUrl(),
               paypalConfiguration.getReturnUrl(),
               shoppingCart.getCurrency(),
               shoppingCart.getShippingAddress(),
               shoppingCart.getBillingAddress(), shoppingCart.getNotes());
      return html.toString();
   }

   private static void addIntro(StringBuffer html, String server, String email)
   {
      html.append("<form action='" + server + "' method='post'>");
      html.append("<input type='hidden' name='cmd' value='_cart'>");
      html.append("<input type='hidden' name='redirect_cmd' value='_xclick'>");
      html.append("<input type='hidden' name='business' value='" + email + "'>");
      html.append("<input type='hidden' name='upload' value='1'>");
   }

   private static void payerToHtml(StringBuffer html, Long id, String ipnUrl, String cancelUrl, String returnUrl,
            String cuncurrency, ShippingAddress shippingAddress, BillingAddress billingAddress,
            String notes)
   {

      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='notify_url' value='" + ipnUrl + "'>");
      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='cancel_return' value='" + cancelUrl + "?paymentId=" + id + "'>");
      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='return' value='" + returnUrl + "?paymentId=" + id + "'>");
      html.append("<input type='hidden' name='rm' value='2'>");
      html.append("<input type='hidden' name='currency_code' value='" + cuncurrency + "'>");

      html.append("<input type='hidden' name='first_name' value='" + shippingAddress.getFirstName() + "'>");
      html.append("<input type='hidden' name='last_name' value='" + shippingAddress.getLastName() + "'>");
      html.append("<input type='hidden' name='address1' value='" + shippingAddress.getLine1() + "'>");

      html.append("<INPUT TYPE='hidden' NAME='night_phone_a' VALUE='39'>");
      html.append("<INPUT TYPE='hidden' NAME='night_phone_b' VALUE='" + billingAddress.getPhone() + "'>");

      html.append("<input type='hidden' name='day_phone_a' value='39'>");
      html.append("<input type='hidden' name='day_phone_b' value='" + billingAddress.getPhone() + "'>");
      
      if (shippingAddress.getLine2() != null && !shippingAddress.getLine2().trim().isEmpty())
      {
         html.append("<input type='hidden' name='address2' value='" + shippingAddress.getLine2() + "'>");
      }
      html.append("<input type='hidden' name='email' value='" + billingAddress.getEmail() + "'>");
      html.append("<input type='hidden' name='city' value='" + shippingAddress.getCity() + "'>");
      html.append("<input type='hidden' name='state' value='" + shippingAddress.getState() + "'>");
      html.append("<input type='hidden' name='zip' value='" + shippingAddress.getZip() + "'>");
      // STATO = IT
      html.append("<input type='hidden' name='country' value='" + shippingAddress.getCountryCode() + "'>");
      html.append("<input type='hidden' name='lc' value='" + shippingAddress.getCountryCode() + "'>");
      html.append("<input type='hidden' name='custom' value='" + id + "'>");
      if (notes != null && !notes.trim().isEmpty())
      {
         html.append("<input type='hidden' name='cn' value='" + notes + "'>");
      }
      if (billingAddress.getVatCode() != null && !billingAddress.getVatCode().trim().isEmpty())
      {
         html.append("<input type='hidden' name='on0' value='Partita Iva'>");
         html.append("<input type='hidden' name='os0' value='" + billingAddress.getVatCode() + "'>");
      }
      html.append("<input type='hidden' name='on1' value='Telefono'>");
      html.append("<input type='hidden' name='os1' value='" + billingAddress.getPhone() + "'>");
      html.append("<input name='submit' type='submit' value='Paga con PayPal' " + style + " />");
      html.append("</form>");

   }

   private static void articlesToHtml(List<ShoppingArticle> articles, StringBuffer html,
            double shipping)
   {
      int i = 1;
      for (ShoppingArticle shA : articles)
      {
         if (i == 1)
         {
            html.append("<input type='hidden' name='shipping_" + i + "' value='" + shipping + "'>");
         }
         html.append("<input type='hidden' name='amount_" + i + "' value='" + shA.getPrice() + "'>");
         html.append("<input type='hidden' name='item_name_" + i + "' value='" + shA.getDescription() + "'>");
         html.append("<input type='hidden' name='quantity_" + i + "' value='" + shA.getQuantity() + "'>");
         i++;
      }

   }
}
