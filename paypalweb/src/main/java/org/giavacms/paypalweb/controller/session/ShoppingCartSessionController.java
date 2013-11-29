package org.giavacms.paypalweb.controller.session;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.util.JSFUtils;
import org.giavacms.paypalweb.controller.request.PaypalConfigurationRequestController;
import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.ShoppingArticle;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.service.ShippingService;
import org.jboss.logging.Logger;

@Named
@SessionScoped
public class ShoppingCartSessionController implements Serializable
{
   private static final long serialVersionUID = 1L;
   private String lastPage;
   private ShoppingCart element;

   Logger logger = Logger.getLogger(getClass().getName());

   @Inject
   PaypalConfigurationRequestController paypalConfigurationRequestController;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @EJB
   ShippingService shippingService;

   public ShoppingCartSessionController()
   {
   }

   public void addBillingAddress(BillingAddress billingAddress)
   {
      getElement().setBillingAddress(billingAddress);
   }

   public void addShippingAddress(ShippingAddress shippingAddress)
   {
      getElement().setShippingAddress(shippingAddress);
   }

   public String addProduct(String vat,
            String price,
            String idProduct,
            String description, int quantity, String imageUrl)
   {
      getElement().addArticle(new ShoppingArticle(idProduct, description, price, quantity, vat, imageUrl));
      getElement().addPartial(quantity, price, vat);
      logger.info("idProduct:" + idProduct + " description:" + description + " price: " + price + " quantity: "
               + quantity + " vat: "
               + vat + " imageUrl:" + imageUrl);
      try
      {
         FacesContext.getCurrentInstance().getExternalContext()
                  .redirect(paypalConfigurationRequestController.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public void gotoLastPage()
   {
      try
      {
         JSFUtils.redirect(getLastPage());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void resetShoppingCart()
   {
      this.element = new ShoppingCart(paypalConfigurationRequestController.getPaypalConfiguration().getCurrency());
   }

   public void reset()
   {
      resetShoppingCart();
      try
      {
         JSFUtils.redirect(paypalConfigurationRequestController.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public ShoppingCart getElement()
   {
      if (this.element == null)
         this.element = new ShoppingCart();
      return element;
   }

   public String getLastPage()
   {
      if (this.lastPage != null && !this.lastPage.isEmpty())
      {
         return lastPage;
      }
      return "";
   }

   public void setLastPage(String lastPage)
   {
      this.lastPage = lastPage;
   }

}
