/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.PageConfiguration;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class PageConfigurationRepository extends
         AbstractRepository<PageConfiguration>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected EntityManager getEm()
   {
      return em;
   }

   public PageConfiguration load()
   {
      PageConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new PageConfiguration();
         persist(c);
      }
      return c;
   }

   @Override
   protected String getDefaultOrderBy()
   {

      return "id desc";
   }

}
