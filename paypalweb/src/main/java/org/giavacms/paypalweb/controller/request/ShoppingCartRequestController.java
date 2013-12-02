package org.giavacms.paypalweb.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;
import org.giavacms.paypalweb.model.ShoppingCart;

/**
 * 
 * <form action="" method="post"> <h:outputText value="#{contactUsRequestController.returnMessage}"/> <br/>
 * Nome: <input name="name" type="text"/> <br/>
 * Email: <input name="email" type="text"/> <br/>
 * Message: <textarea name="message" cols="40" rows="6"></textarea> <input type="submit" name="submit" value="invia"/>
 * </form>
 * 
 * @author pisi79
 * 
 */
@Named
@RequestScoped
public class ShoppingCartRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;
   @Inject
   @HttpParam
   String idProduct;

   @Inject
   @HttpParam
   String description;

   @Inject
   @HttpParam
   String price;

   @Inject
   @HttpParam
   String quantity;
   @Inject
   @HttpParam
   String vat;

   @Inject
   @HttpParam
   String image;

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   @Inject
   NavigationRequestController navigationRequestController;

   public ShoppingCartRequestController()
   {
      super();
   }

   public void add()
   {
      if (idProduct != null &&
               description != null && price != null &&
               quantity != null && vat != null)
      {
         shoppingCartSessionController.addProduct(vat, price,
                  idProduct, description,
                  Integer.valueOf(quantity), image);
         navigationRequestController.goToShoppingCartUrl();
      }
      else
      {
         return;
      }
   }

   public ShoppingCart getElement()
   {
      return shoppingCartSessionController.getElement();
   }

   public void del(String idProduct)
   {
      shoppingCartSessionController.removeArticle(idProduct);
      navigationRequestController.goToShoppingCartUrl();
   }

   public void add(int quantity, String idProduct)
   {
      shoppingCartSessionController.changeArticleQuantity(quantity, idProduct);
      navigationRequestController.goToShoppingCartUrl();
   }

   public void inc(String idProduct)
   {
      shoppingCartSessionController.changeArticleQuantity(1, idProduct);
      navigationRequestController.goToShoppingCartUrl();
   }

   public void dec(String idProduct)
   {
      shoppingCartSessionController.changeArticleQuantity(-1, idProduct);
      navigationRequestController.goToShoppingCartUrl();
   }

   public String getButton()
   {
      return shoppingCartSessionController.getButton();
   }

   public String getLastPage()
   {
      return shoppingCartSessionController.getLastPage();

   }

   public void reset()
   {
      shoppingCartSessionController.resetShoppingCart();
      navigationRequestController.goToShoppingCartUrl();
   }

   public void exit()
   {
      shoppingCartSessionController.exit();
      navigationRequestController.goToShoppingCartUrl();
   }
}
