/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller;

import org.giavacms.common.annotation.*;
import org.giavacms.paypalweb.model.enums.PaypalStatus;
import org.giavacms.paypalweb.repository.ShoppingCartRepository;
import org.giavacms.paypalweb.service.NotificationService;

import javax.inject.Inject;
import java.util.Date;

public class ShoppingCartController
{

//   private static final long serialVersionUID = 1L;
//
//   // --------------------------------------------------------
//   @BackPage
//   public static String BACK = "/private/administration.xhtml";
//   @ViewPage
//   public static String VIEW_PAGE = "/private/paypalweb/view.xhtml";
//   @ListPage
//   public static String LIST_PAGE = "/private/paypalweb/list.xhtml";
//   @EditPage
//   public static String EDIT_PAGE = "/private/paypalweb/edit.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(ShoppingCartRepository.class)
   ShoppingCartRepository shoppingCartRepository;

   @Inject
   NotificationService notificationService;

   @Override
   public String viewElement()
   {

      String vieL = super.viewElement();
      logger.info(getElement());

      return vieL;
   }

   public String sent()
   {
      getElement().setSentDate(new Date());
      getElement().setPaypalStatus(PaypalStatus.Sent);
      shoppingCartRepository.update(getElement());
      notificationService.notifySent(getElement());
      return viewCurrent();
   }

   public String refund()
   {
      getElement().setRefundedDate(new Date());
      getElement().setPaypalStatus(PaypalStatus.Refunded);
      shoppingCartRepository.update(getElement());
      notificationService.notifyRefunded(getElement());
      return viewCurrent();
   }

   public String undo()
   {
      getElement().setUndoDate(new Date());
      getElement().setPaypalStatus(PaypalStatus.Undo);
      shoppingCartRepository.update(getElement());
      return viewCurrent();
   }

   @Override
   public String delete()
   {
      getElement().setActive(false);
      shoppingCartRepository.update(getElement());
      return listPage();
   }
}
