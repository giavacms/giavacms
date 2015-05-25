/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.paypalweb.model.ShoppingCart;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Map;

@Named
@Stateless
public class ShoppingCartRepository extends
         BaseRepository<ShoppingCart>
{

   private static final long serialVersionUID = 1L;

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
