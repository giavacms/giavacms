/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Product;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.repository.CustomerToProductRepository;

@Named
@RequestScoped
public class CustomerToProductRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;

   @Inject
   CustomerToProductRepository customerToProductRepository;

   public CustomerToProductRequestController()
   {
      super();
   }

   public List<Product> getProductList(Customer customer)
   {
      List<Product> l = customerToProductRepository.getProductList(customer);
      return l;
   }

}
