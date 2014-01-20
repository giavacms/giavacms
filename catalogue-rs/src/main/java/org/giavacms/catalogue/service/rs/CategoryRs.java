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

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.common.model.Search;
import org.jboss.logging.Logger;

@Path("/v1/catalogue")
@Stateless
@LocalBean
public class CategoryRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass().getCanonicalName());

   @Inject
   CategoryRepository categoryRepository;

   @GET
   @Path("/categories")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Category> getList(@QueryParam("startRow") int startRow, @QueryParam("pageSize") int pageSize)
   {
      Search<Category> search = new Search<Category>(Category.class);
      return categoryRepository.getList(search, pageSize, pageSize);

   }

   @GET
   @Path("/categories/size")
   @Produces(MediaType.APPLICATION_JSON)
   public int getListSize()
   {
      Search<Category> search = new Search<Category>(Category.class);
      return categoryRepository.getListSize(search);

   }

   @GET
   @Path("/categories/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Category getCategory(@PathParam("id") Long id) throws Exception
   {
      return categoryRepository.fetch(id);
   }

}
