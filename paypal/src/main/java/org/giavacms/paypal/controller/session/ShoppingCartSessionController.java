package org.giavacms.paypal.controller.session;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.util.JSFUtils;
import org.giavacms.paypal.model.ShoppingArticle;
import org.giavacms.paypal.model.ShoppingCart;
import org.giavacms.paypal.producer.PaypallProducer;
import org.giavacms.paypal.repository.ShoppingCartRepository;
import org.giavacms.paypal.service.ShippingAmountService;
import org.giavacms.paypal.util.PaypalUtils;
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
   PaypallProducer paypallProducer;

   @Inject
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   ShippingAmountService shippingAmountService;

   public ShoppingCartSessionController()
   {
   }

   public String addProduct(String vat,
            String price,
            String idProduct,
            String description, int quantity)
   {
      getElement().addArticle(new ShoppingArticle(idProduct, description, price, quantity, vat));

      logger.info("idProduct:" + idProduct + " description:" + description + " price: " + price + " quantity: "
               + quantity + " vat: "
               + vat);
      try
      {
         FacesContext.getCurrentInstance().getExternalContext()
                  .redirect(paypallProducer.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public void gotoPaypal()
   {
      double shippingAmount = shippingAmountService.calculate(getElement());
      // EMULAZIONE DATI PAYER
      getElement().getPayerInfo().setEmail("fiorenzino@gmail.com");
      getElement().getPayerInfo().setFirstName("fiorenzo");
      getElement().getPayerInfo().setLastName("pizza");
      getElement().getPayerInfo().setPhone("+393922274929");

      // EMULAZIONE COMPILAZIONE Address
      getElement().getPayerInfo().getAddress().setCity("san benedetto del tronto");
      getElement().getPayerInfo().getAddress().setCountryCode("IT");
      getElement().getPayerInfo().getAddress().setPostalCode("63074");
      getElement().getPayerInfo().getAddress().setState("AP");
      getElement().getPayerInfo().getAddress().setLine1("via cornelio nepote 8");

      // EMULAZIONE COMPILAZIONE Shipping Address
      getElement().getPayerInfo().getShippingAddress().setCity("san benedetto del tronto");
      getElement().getPayerInfo().getShippingAddress().setCountryCode("IT");
      getElement().getPayerInfo().getShippingAddress().setPostalCode("63074");
      getElement().getPayerInfo().getShippingAddress().setState("AP");
      getElement().getPayerInfo().getShippingAddress().setLine1("via cornelio nepote 8");
      getElement().getPayerInfo().getShippingAddress().setRecipientName("fiorenzo pizza");
      getElement().getPayerInfo().getShippingAddress().setType("residential");

      getElement().setShipping(shippingAmount);
      shoppingCartRepository.persist(getElement());
      try
      {
         PaypalUtils.init(paypallProducer.getPaypalConfiguration(), getElement(), false, false);
         shoppingCartRepository.update(getElement());
         if (getElement().isCreated())
         {
            logger.info("redirect: " + getElement().getApprovalUrl());
            FacesContext.getCurrentInstance().getExternalContext().redirect(getElement().getApprovalUrl());
         }
         else
         {
            logger.info("ERROR!!!");
            FacesContext.getCurrentInstance().getExternalContext()
                     .redirect(paypallProducer.getPaypalConfiguration().getShoppingCartUrl());
            return;
         }
      }
      catch (RuntimeException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void gotoLastPage()
   {
      try
      {
         JSFUtils.redirect(getLastPage());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void resetShoppingCart()
   {
      this.element = new ShoppingCart(paypallProducer.getPaypalConfiguration().getCurrency());
   }

   public void reset()
   {
      resetShoppingCart();
      try
      {
         JSFUtils.redirect(paypallProducer.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
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
