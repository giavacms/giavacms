package org.giavacms.paypalrs.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.paypalrs.model.ShoppingCart;
import org.giavacms.paypalrs.repository.ShoppingCartRepository;

@Named
@SessionScoped
public class ShoppingCartController extends
         AbstractLazyController<ShoppingCart>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   public static String VIEW_PAGE = "/private/paypal/view.xhtml";
   @ListPage
   public static String LIST_PAGE = "/private/paypal/list.xhtml";
   @EditPage
   public static String EDIT_PAGE = "/private/paypal/edit.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(ShoppingCartRepository.class)
   ShoppingCartRepository shoppingCartRepository;

   @Override
   public String viewElement()
   {

      String vieL = super.viewElement();
      logger.info(getElement());

      return vieL;
   }

   public String close()
   {

      return viewCurrent();
   }

   public String rollback()
   {
      return viewCurrent();
   }
}
