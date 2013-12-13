/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller.session;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import org.giavacms.paypalweb.model.enums.PaypalStatus;
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
   private boolean oneAddress = true;

   public ShoppingCartSessionController()
   {
   }

   public boolean load(Long id)
   {
      ShoppingCart shoppingCart = shoppingCartRepository.find(id);
      setElement(shoppingCart);
      if (shoppingCart != null)
         return true;
      return false;
   }

   public void addBillingAddress(BillingAddress billingAddress)
   {
      getElement().setBillingAddress(billingAddress);
   }

   public void setNotes(String notes)
   {
      getElement().setNotes(notes);
   }

   public void addShippingAddress(ShippingAddress shippingAddress)
   {
      getElement().setShippingAddress(shippingAddress);
   }

   public void save()
   {
      getElement().setPaypalStatus(PaypalStatus.Init);
      getElement().setInitDate(new Date());
      double shipping = shippingService.calculate(getElement());
      getElement().setShipping(BigDecimal.valueOf(shipping));
      if (getElement().getId() == null)
      {
         shoppingCartRepository.persist(getElement());
      }
      else
         shoppingCartRepository.update(getElement());
      logger.info(getElement().getId());
   }

   public String getButton()
   {
      return ButtonUtils.generate(getElement(),
               paypalConfigurationRequestController.getPaypalConfiguration());
   }

   public void addProduct(String vat,
            String price,
            String idProduct,
            String description, int quantity, String imageUrl)
   {
      getElement().addArticle(new ShoppingArticle(idProduct, description, price == null ? null :new BigDecimal(price), quantity, 
    		  vat == null ? null : new BigDecimal(vat), imageUrl));

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
      this.element.getBillingAddress().setCountryCode("IT");
      this.element.getShippingAddress().setCountryCode("IT");
   }

   private void updateLastPage()
   {
      setLastPage(RequestUriCleaner.cleanPage(requestUri.get()));
   }

   public void reset()
   {
      resetShoppingCart();
   }

   public void exit()
   {
      if (getElement().getId() != null)
      {
         getElement().setPaypalStatus(PaypalStatus.Undo);
         getElement().setUndoDate(new Date());
         shoppingCartRepository.update(getElement());
      }
      resetShoppingCart();
   }

   public ShoppingCart getElement()
   {
      if (this.element == null)
      {
         this.element = new ShoppingCart();
         this.element.getBillingAddress().setCountryCode("IT");
         this.element.getShippingAddress().setCountryCode("IT");
      }
      return element;
   }

   public void setElement(ShoppingCart element)
   {
      this.element = element;
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

   public boolean isOneAddress()
   {
      return oneAddress;
   }

   public void setOneAddress(boolean oneAddress)
   {
      this.oneAddress = oneAddress;
   }

}
