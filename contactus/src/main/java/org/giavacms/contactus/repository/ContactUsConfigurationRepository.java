/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.contactus.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.annotation.LogOperation;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.contactus.model.ContactUsConfiguration;

@Named
@Stateless
@LocalBean
public class ContactUsConfigurationRepository extends AbstractRepository<ContactUsConfiguration>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   public EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected void applyRestrictions(Search<ContactUsConfiguration> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      if (search.getObj().getDescription() != null && search.getObj().getDescription().trim().length() > 0)
      {
         sb.append(separator).append(" ( upper( ").append(alias).append(".email ) like :DESC or upper ( ")
                  .append(alias).append(".description ) like :DESC )");
         params.put("DESC", likeParam(search.getObj().getDescription().trim().toUpperCase()));
         separator = " and ";
      }
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "email asc";
   }

   @Override
   @LogOperation
   public boolean delete(Object key)
   {
      ContactUsConfiguration c = find(key);
      if (c != null)
      {
         c.setActive(false);
         update(c);
         return true;
      }
      return false;
   }
}
