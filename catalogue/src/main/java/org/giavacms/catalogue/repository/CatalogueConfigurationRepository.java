/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.catalogue.model.CatalogueConfiguration;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class CatalogueConfigurationRepository extends
         AbstractRepository<CatalogueConfiguration>
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

   public CatalogueConfiguration load()
   {
      CatalogueConfiguration c = null;
      try
      {
         c = find(1L);
      }
      catch (Exception e)
      {
      }
      if (c == null)
      {
         c = new CatalogueConfiguration();
         c.setResize(false);
         c.setMaxWidthOrHeight(0);
         persist(c);
      }
      return c;
   }

}
