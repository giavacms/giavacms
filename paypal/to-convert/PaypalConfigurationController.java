/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.paypalweb.model.PaypalConfiguration;
import org.giavacms.paypalweb.repository.PaypalConfigurationRepository;

@Named
@SessionScoped
public class PaypalConfigurationController extends
         AbstractLazyController<PaypalConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/paypalweb/configuration/edit.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(PaypalConfigurationRepository.class)
   PaypalConfigurationRepository paypalConfigurationRepository;

   @Override
   public PaypalConfiguration getElement()
   {
      if (super.getElement() == null)
         setElement(paypalConfigurationRepository.load());
      return super.getElement();
   }
}
