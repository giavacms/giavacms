/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller.request;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.util.JSFUtils;
import org.giavacms.paypalweb.controller.session.ShoppingCartSessionController;

@RequestScoped
@Named
public class NavigationRequestController
{
   @Inject
   PaypalConfigurationRequestController paypalConfigurationRequestController;

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   public NavigationRequestController()
   {
   }

   public void goToShoppingCartUrl()
   {
      try
      {
         JSFUtils.redirect(paypalConfigurationRequestController.getPaypalConfiguration().getShoppingCartUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void gotoLastPage()
   {
      try
      {
         JSFUtils.redirect(shoppingCartSessionController.getLastPage());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void gotoPreviewShoppingCartPage()
   {
      try
      {
         JSFUtils.redirect(paypalConfigurationRequestController.getPaypalConfiguration().getPreviewShoppingCartUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void gotoPayerInfoUrl()
   {
      try
      {
         JSFUtils.redirect(paypalConfigurationRequestController.getPaypalConfiguration().getPayerInfoUrl());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

}
