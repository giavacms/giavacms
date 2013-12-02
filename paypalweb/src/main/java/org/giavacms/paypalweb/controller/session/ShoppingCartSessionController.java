package org.giavacms.paypalweb.controller.session;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.RequestUri;
import org.giavacms.paypalweb.controller.request.PaypalConfigurationRequestController;
import org.giavacms.paypalweb.model.BillingAddress;
import org.giavacms.paypalweb.model.ShippingAddress;
import org.giavacms.paypalweb.model.ShoppingArticle;
import org.giavacms.paypalweb.model.ShoppingCart;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.service.ShippingService;
import org.giavacms.paypalweb.util.ButtonUtils;
import org.giavacms.paypalweb.util.RequestUriCleaner;
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

   @Inject
   @RequestUri
   Instance<String> requestUri;

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

   public void save()
   {

      double shipping = shippingService.calculate(getElement());
      getElement().setShipping(BigDecimal.valueOf(shipping));

      shoppingCartRepository.persist(getElement());
      logger.info(getElement().getId());
   }

   public String getButton()
   {
      return ButtonUtils.generate(getElement(),
               paypalConfigurationRequestController.getPaypalConfiguration().getServiceUrl(),
               paypalConfigurationRequestController.getPaypalConfiguration().getEmail(),
               paypalConfigurationRequestController.getPaypalConfiguration().getIpnUrl(),
               paypalConfigurationRequestController.getPaypalConfiguration().getCancelUrl(),
               paypalConfigurationRequestController.getPaypalConfiguration().getReturnUrl());
   }

   public void addProduct(String vat,
            String price,
            String idProduct,
            String description, int quantity, String imageUrl)
   {
      getElement().addArticle(new ShoppingArticle(idProduct, description, price, quantity, vat, imageUrl));

      logger.info("idProduct:" + idProduct + " description:" + description + " price: " + price + " quantity: "
               + quantity + " vat: "
               + vat + " imageUrl:" + imageUrl);
      updateLastPage();
   }

   public void removeArticle(String idProduct)
   {
      getElement().removeArticle(idProduct);
   }

   public void changeArticleQuantity(int quantity, String idProduct)
   {
      getElement().changeArticleQuantity(idProduct, quantity);
   }

   public void incArticleQuantity(String idProduct)
   {
      changeArticleQuantity(1, idProduct);
   }

   public void decArticleQuantity(String idProduct)
   {
      changeArticleQuantity(-1, idProduct);
   }

   public void resetShoppingCart()
   {
      this.element = new ShoppingCart(paypalConfigurationRequestController.getPaypalConfiguration().getCurrency());
   }

   private void updateLastPage()
   {
      setLastPage(RequestUriCleaner.cleanPage(requestUri.get()));
   }

   public void reset()
   {
      resetShoppingCart();
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
