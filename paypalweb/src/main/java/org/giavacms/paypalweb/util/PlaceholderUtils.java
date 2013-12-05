package org.giavacms.paypalweb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.giavacms.paypalweb.model.ShoppingArticle;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.jboss.logging.Logger;

public class PlaceholderUtils
{
   public static final String PLACEHOLDER_XHTML_PATTERN = "\\{((.*?):(.*?))\\}";

   static Logger log = Logger.getLogger(PlaceholderUtils.class.getName());

   private static String buildPlaceholder(String nome)
   {
      return "(\\{" + nome + "\\})";
   }

   private static String replace(String input, String replacement,
            String regex, boolean escapeHtml)
   {
      String replacementCustomEscaped = replacement
               .replace("&lt;", "&LESSTHAN;");
      Pattern p = Pattern.compile(regex);
      Matcher matcher = p.matcher(input);
      return matcher.replaceAll(Matcher.quoteReplacement(replacementCustomEscaped));
   }

   private static String replaceSingolo(final String input,
            PlaceholderValorizzato placeHolder)
   {

      if (placeHolder == null || placeHolder.getValue() == null || placeHolder.getValue().isEmpty())
         return input;

      return replace(input, placeHolder.getValue(), PlaceholderUtils.buildPlaceholder(placeHolder
               .getKey()), true);
   }

   public static String replaceAll(String input, ShoppingCart shoppingCart, String shoppingCartDirectUrl
            )
   {
      List<PlaceholderValorizzato> phList = new ArrayList<PlaceholderValorizzato>();
      phList.add(new PlaceholderValorizzato("id", "" + shoppingCart.getId()));
      phList.add(new PlaceholderValorizzato("firstName", shoppingCart.getBillingAddress().getFirstName()));
      phList.add(new PlaceholderValorizzato("lastName", shoppingCart.getBillingAddress().getLastName()));
      phList.add(new PlaceholderValorizzato("vatCode", shoppingCart.getBillingAddress().getVatCode()));
      phList.add(new PlaceholderValorizzato("line1", shoppingCart.getBillingAddress().getLine1()));
      phList.add(new PlaceholderValorizzato("line2", shoppingCart.getBillingAddress().getLine2()));
      phList.add(new PlaceholderValorizzato("city", shoppingCart.getBillingAddress().getCity()));
      phList.add(new PlaceholderValorizzato("countryCode", shoppingCart.getBillingAddress().getCountryCode()));
      phList.add(new PlaceholderValorizzato("zip", shoppingCart.getBillingAddress().getZip()));
      phList.add(new PlaceholderValorizzato("state", shoppingCart.getBillingAddress().getState()));
      phList.add(new PlaceholderValorizzato("phone", shoppingCart.getBillingAddress().getPhone()));
      phList.add(new PlaceholderValorizzato("email", shoppingCart.getBillingAddress().getEmail()));
      phList.add(new PlaceholderValorizzato("total", "" + shoppingCart.getPartialAmount() + " "
               + shoppingCart.getCurrency()));
      phList.add(new PlaceholderValorizzato("vat", "" + shoppingCart.getPartialTax() + " " + shoppingCart.getCurrency()));
      phList.add(new PlaceholderValorizzato("shipping", "" + shoppingCart.getShipping() + " "
               + shoppingCart.getCurrency()));
      phList.add(new PlaceholderValorizzato("products", getProducts(shoppingCart.getShoppingArticles(),
               shoppingCart.getCurrency())));
      phList.add(new PlaceholderValorizzato("notes", shoppingCart.getNotes()));
      if (shoppingCartDirectUrl != null && !shoppingCartDirectUrl.trim().isEmpty())
      {
         phList.add(new PlaceholderValorizzato("directUrl", "" + shoppingCartDirectUrl + shoppingCart.getId()));
      }
      if (input == null)
      {
         return "";
      }

      // String result = input.replaceAll("\\r|\\n", "").replaceAll(
      // "\\r\\n|\\r|\\n", "");

      for (PlaceholderValorizzato placeHolder : phList)
      {
         input = replaceSingolo(input, placeHolder);
      }

      return input;
   }

   private static String getProducts(List<ShoppingArticle> articles, String cuncurrency)
   {
      StringBuffer stringBuffer = new StringBuffer();
      for (ShoppingArticle shoppingArticle : articles)
      {
         stringBuffer.append(shoppingArticle.getQuantity() + " x " + shoppingArticle.getDescription() + " - "
                  + cuncurrency + " " + shoppingArticle.getPrice() + " (iva:" + shoppingArticle.getVat() + ")\n");
      }
      return stringBuffer.toString();
   }
}
