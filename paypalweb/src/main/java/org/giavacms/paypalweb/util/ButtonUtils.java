package org.giavacms.paypalweb.util;

import java.util.List;

import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.ShoppingArticle;
import org.giavacms.paypalweb.model.ShoppingCart;

public class ButtonUtils
{

   public static String generate(ShoppingCart shoppingCart, String server, String email, String ipnUrl,
            String cancelUrl, String returnUrl)
   {
      StringBuffer html = new StringBuffer();
      html.append("<form action='" + server + "' method='post'>");
      html.append("<input type='hidden' name='cmd' value='_cart'>");
      html.append("<input type='hidden' name='redirect_cmd' value='_xclick'>");
      html.append("<input type='hidden' name='email' value='" + email + "'>");
      articlesToHtml(shoppingCart.getShoppingArticles(), html);
      html.append("<input type='hidden' name='shipping_1' value='" + shoppingCart.getShipping().doubleValue() + "'>");
      payerToHtml(html, shoppingCart.getId(), ipnUrl, cancelUrl, returnUrl, shoppingCart.getCurrency(), email,
               shoppingCart.getShippingAddress(),
               shoppingCart.getBillingAddress());
      return html.toString();
   }

   private static void payerToHtml(StringBuffer html, Long id, String ipnUrl, String cancelUrl, String returnUrl,
            String cuncurrency, String email, ShippingAddress shippingAddress, BillingAddress billingAddress)
   {

      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='notify_url' value='" + ipnUrl + "'>");
      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='cancel_return' value='" + cancelUrl + "?id=" + id + "'>");
      // ATTACCACI ID PAGAMENTO
      html.append("<input type='hidden' name='return' value='" + returnUrl + "?id=" + id + "'>");
      html.append("<input type='hidden' name='rm' value='2'>");
      html.append("<input type='hidden' name='currency_code' value='" + cuncurrency + "'>");
      html.append("<input type='hidden' name='business' value='" + email + "'>");
      html.append("<input type='hidden' name='first_name' value='" + shippingAddress.getFirstName() + "'>");
      html.append("<input type='hidden' name='last_name' value='" + shippingAddress.getLastName() + "'>");
      html.append("<input type='hidden' name='address1' value='" + shippingAddress.getLine1() + "'>");
      html.append("<input type='hidden' name='address2' value='" + shippingAddress.getLine2() + "'>");
      html.append("<input type='hidden' name='city' value='" + shippingAddress.getCity() + "'>");
      html.append("<input type='hidden' name='state' value='" + shippingAddress.getState() + "'>");
      html.append("<input type='hidden' name='zip' value='" + shippingAddress.getZip() + "'>");
      html.append("<input type='hidden' name='country' value='" + shippingAddress.getCountryCode() + "'>");
      html.append("<input type='hidden' name='lc' value='" + shippingAddress.getCountryCode() + "'>");
      html.append("<input type='hidden' name='shipping' value='0.00'>");
      html.append("<input type='hidden' name='custom' value='" + id + "'>");
      html.append("<input type='hidden' name='upload' value='1'>");
      if (billingAddress.getVatCode() != null && !billingAddress.getVatCode().trim().isEmpty())
      {
         html.append("<input type='hidden' name='on0' value='Codice Fiscale/Partita Iva'>");
         html.append("<input type='hidden' name='os0' value='" + billingAddress.getVatCode() + "'>");
      }
      html.append("<input type='hidden' name='on1' value='Telefono'>");
      html.append("<input type='hidden' name='os1' value='" + billingAddress.getPhone() + "'>");
      html.append("<input name='submit' type='submit' value='Paga con PayPal' image='https://www.paypalobjects.com/webstatic/mktg/logo-center/logo_paypal_pagamento.jpg'/>");
      html.append("</form>");

   }

   private static void articlesToHtml(List<ShoppingArticle> articles, StringBuffer html)
   {
      int i = 1;
      for (ShoppingArticle shA : articles)
      {
         html.append("<input type='hidden' name='amount_" + i + "' value='" + shA.getTotal() + "'>");
         html.append("<input type='hidden' name='item_name_" + i + "' value='" + shA.getDescription() + "'>");
         html.append("<input type='hidden' name='quantity_" + i + "' value='" + shA.getQuantity() + "'>");
         i++;
      }

   }
}
