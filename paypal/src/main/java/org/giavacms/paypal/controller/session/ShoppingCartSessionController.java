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

@Named
@SessionScoped
public class ShoppingCartSessionController implements Serializable
{
   private static final long serialVersionUID = 1L;
   private String lastPage;
   private ShoppingCart element;

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

      System.out.println(" - idProduct, description, price, quantity, vat: "
               + idProduct + " " + description + " " + price + " " + quantity + " " + vat);
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
      String shippingAmount = shippingAmountService.calculate(getElement());
      getElement().setShipping(shippingAmount);
      shoppingCartRepository.persist(getElement());
      try
      {
         PaypalUtils.init(paypallProducer.getPaypalConfiguration(), getElement());
         shoppingCartRepository.update(getElement());
         if (getElement().isCreated())
            JSFUtils.redirect(getElement().getApprovalUrl());
         else
         {
            System.out.println("ERROR!!!");
            return;
         }
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

   public void reset()
   {
      this.element = new ShoppingCart(paypallProducer.getPaypalConfiguration().getCurrency());
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
