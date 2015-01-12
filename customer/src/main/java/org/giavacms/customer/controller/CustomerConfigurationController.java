/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.customer.model.CustomerConfiguration;
import org.giavacms.customer.repository.CustomerConfigurationRepository;

@Named
@SessionScoped
public class CustomerConfigurationController extends
         AbstractLazyController<CustomerConfiguration>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------
   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ViewPage
   @ListPage
   @EditPage
   public static String LIST = "/private/customer/configuration.xhtml";

   // ------------------------------------------------

   @Inject
   @OwnRepository(CustomerConfigurationRepository.class)
   CustomerConfigurationRepository catalogueConfigurationRepository;

   @Override
   public CustomerConfiguration getElement()
   {
      if (super.getElement() == null)
         setElement(catalogueConfigurationRepository.load());
      return super.getElement();
   }

}
