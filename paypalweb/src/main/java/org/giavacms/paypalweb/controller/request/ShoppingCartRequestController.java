package org.giavacms.paypalweb.controller.request;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.common.annotation.HttpParam;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.util.JSFUtils;
import org.giavacms.paypalweb.controller.PaypalConfigurationController;
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
public class ShoppingCartRequestController extends
         AbstractRequestController<ShoppingCart> implements Serializable
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
   PaypalConfigurationController paypalConfigurationController;

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
         String requestURI = ((HttpServletRequest) FacesContext.getCurrentInstance()
                  .getExternalContext().getRequest()).getRequestURI();
         logger.info(requestURI);
         if (requestURI.startsWith("/db:"))
            shoppingCartSessionController.setLastPage(requestURI.replace("db:", "p/").replace(".jsf", ""));
         if (requestURI.startsWith("/cache"))
            shoppingCartSessionController.setLastPage(requestURI.replace("cache", "p").replace(".jsf", ""));
         if (requestURI.startsWith("/pages"))
            shoppingCartSessionController.setLastPage(requestURI.replace("pages", "p").replace(".jsf", ""));
         logger.info("LAST PAGE: " + shoppingCartSessionController.getLastPage());

         shoppingCartSessionController.addProduct(vat, price,
                  idProduct, description,
                  Integer.valueOf(quantity), image);
      }
   }

   @Override
   public String getCurrentPageParam()
   {
      return null;
   }

   @Override
   protected String getIdParam()
   {
      return null;
   }

   public void removeProduct(String idProduct)
   {
      getElement().removeArticle(idProduct);
      try
      {
         JSFUtils.redirect(paypalConfigurationController.getElement().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void changeArticleQuantity(int quantity, String vat, String price, String idProduct)
   {
      getElement().changeArticleQuantity(vat, price, idProduct, quantity);
      try
      {
         JSFUtils.redirect(paypalConfigurationController.getElement().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void incArticleQuantity(String vat, String price, String idProduct)
   {
      changeArticleQuantity(1, vat, price, idProduct);
   }

   public void decArticleQuantity(String vat, String price, String idProduct)
   {
      changeArticleQuantity(-1, vat, price, idProduct);
   }

}
