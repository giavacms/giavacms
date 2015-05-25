/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.paypalweb.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.paypalweb.model.PaypalConfiguration;

import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
public class PaypalConfigurationRepository extends
         BaseRepository<PaypalConfiguration>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      // TODO Auto-generated method stub
      return "id asc";
   }

   public PaypalConfiguration load() throws Exception
   {
      PaypalConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new PaypalConfiguration();
         persist(c);
      }
      return c;
   }

}
