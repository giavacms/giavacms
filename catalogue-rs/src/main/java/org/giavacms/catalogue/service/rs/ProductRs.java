/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.service.rs;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.model.Search;
import org.jboss.logging.Logger;

@Path("/v1/catalogue")
@Stateless
@LocalBean
public class ProductRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   ProductRepository productRepository;

   @GET
   @Path("/products")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Product> getList(@QueryParam("startRow") int startRow, @QueryParam("pageSize") int pageSize)
   {
      Search<Product> search = new Search<Product>(Product.class);
      return productRepository.getList(search, pageSize, pageSize);

   }

   @GET
   @Path("/products/size")
   @Produces(MediaType.APPLICATION_JSON)
   public int getListSize()
   {
      Search<Product> search = new Search<Product>(Product.class);
      return productRepository.getListSize(search);

   }

   @GET
   @Path("/products/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Product getCategory(@PathParam("id") Long id) throws Exception
   {
      return productRepository.fetch(id);
   }
}
