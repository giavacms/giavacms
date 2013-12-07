/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.paypalweb.model.ShoppingCart;

@Named
@Stateless
@LocalBean
public class ShoppingCartRepository extends
         AbstractRepository<ShoppingCart>
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
      return "id desc";
   }

   @Override
   protected void applyRestrictions(Search<ShoppingCart> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      if (search.getObj().getBillingAddress() != null && search.getObj().getBillingAddress().getLastName() != null
               && !search.getObj().getBillingAddress().getLastName().isEmpty())
      {
         sb.append(separator).append(alias).append(".billingAddress.lastName LIKE :lastName ");
         params.put("lastName", likeParam(search.getObj().getBillingAddress().getLastName()));
      }

      if (search.getObj().getId() != null)
      {
         sb.append(separator).append(alias).append(".id = :id ");
         params.put("id", search.getObj().getId());
      }
   }
}
