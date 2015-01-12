/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.customer.model.CustomerConfiguration;

@Named
@Stateless
@LocalBean
public class CustomerConfigurationRepository extends
         AbstractRepository<CustomerConfiguration>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "id asc";
   }

   public CustomerConfiguration load()
   {
      CustomerConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new CustomerConfiguration();
         c.setResize(false);
         c.setMaxWidthOrHeight(0);
         persist(c);
      }
      return c;
   }

}
