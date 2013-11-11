package org.giavacms.paypal.controller.session;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.EmailUtils;
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
   private boolean emulation = true;

   public ShoppingCartSessionController()
   {
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
                  .redirect(paypallProducer.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public String addPayer()
   {
      StringBuffer msg = new StringBuffer();

      // CONTROLLO INFORMAZIONI SUL PAGANTE
      if (getElement().getPayerInfo().getEmail() == null || !getElement().getPayerInfo().getEmail().isEmpty())
      {
         msg.append("Email vuota o nulla. ");
      }
      else
      {
         boolean isValid = EmailUtils.isValidEmailAddress(getElement().getPayerInfo().getEmail());
         if (!isValid)
            msg.append("Email non valida. ");
      }
      if (getElement().getPayerInfo().getFirstName() == null || !getElement().getPayerInfo().getFirstName().isEmpty())
      {
         msg.append("Nome vuoto o nullo. ");
      }
      if (getElement().getPayerInfo().getLastName() == null || !getElement().getPayerInfo().getLastName().isEmpty())
      {
         msg.append("Cognome vuoto o nullo. ");
      }
      if (getElement().getPayerInfo().getPhone() == null || !getElement().getPayerInfo().getPhone().isEmpty())
      {
         msg.append("Telefono vuoto o nullo. ");
      }
      // CONTROLLO INFORMAZIONI SU INDIRIZZO PAGANTE
      if (getElement().getPayerInfo().getAddress().getCity() == null
               || !getElement().getPayerInfo().getAddress().getCity().isEmpty())
      {
         msg.append("Citta' dell'indirizzo vuota o nulla. ");
      }
      if (getElement().getPayerInfo().getAddress().getCountryCode() == null
               || !getElement().getPayerInfo().getAddress().getCountryCode().isEmpty())
      {
         msg.append("Stato dell'indirizzo vuota o nulla. ");
      }
      if (getElement().getPayerInfo().getAddress().getPostalCode() == null
               || !getElement().getPayerInfo().getAddress().getPostalCode().isEmpty())
      {
         msg.append("CAP dell'indirizzo vuota o nulla. ");
      }
      if (getElement().getPayerInfo().getAddress().getState() == null
               || !getElement().getPayerInfo().getAddress().getState().isEmpty())
      {
         msg.append("Provincia dell'indirizzo vuota o nulla. ");
      }
      getElement().getPayerInfo().getAddress().setLine1("via cornelio nepote 8");
      // CONTROLLO INFORMAZIONI SU INDIRIZZO PAGAMENTO
      if (msg.toString().isEmpty())
         return "CONCLUDI";

      return null;
   }

   public void gotoPaypal()
   {
      if (emulation)
      {
         // EMULAZIONE COMPILAZIONE Address Billing
         getElement().getPayerInfo().getAddress().setCity("san benedetto del tronto");
         getElement().getPayerInfo().getAddress().setCountryCode("IT");
         getElement().getPayerInfo().getAddress().setPostalCode("63074");
         getElement().getPayerInfo().getAddress().setState("AP");
         getElement().getPayerInfo().getAddress().setLine1("via cornelio nepote 8");

         // EMULAZIONE DATI PAYER
         getElement().getPayerInfo().setEmail("fiorenzino@gmail.com");
         getElement().getPayerInfo().setFirstName("fiorenzo");
         getElement().getPayerInfo().setLastName("pizza");
         getElement().getPayerInfo().setPhone("+393922274929");

         // EMULAZIONE COMPILAZIONE Shipping Address
         getElement().getPayerInfo().getShippingAddress().setCity("san benedetto del tronto");
         getElement().getPayerInfo().getShippingAddress().setCountryCode("IT");
         getElement().getPayerInfo().getShippingAddress().setPostalCode("63074");
         getElement().getPayerInfo().getShippingAddress().setState("AP");
         getElement().getPayerInfo().getShippingAddress().setLine1("via cornelio nepote 8");
         getElement().getPayerInfo().getShippingAddress().setRecipientName("fiorenzo pizza");
         getElement().getPayerInfo().getShippingAddress().setType("residential");

      }
      double shippingAmount = shippingAmountService.calculate(getElement());

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
