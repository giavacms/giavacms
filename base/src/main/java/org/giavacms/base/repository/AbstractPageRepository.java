/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.logging.Level;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.common.repository.AbstractRepository;

public abstract class AbstractPageRepository<T extends Page> extends
         AbstractRepository<T>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Inject
   TemplateRepository templateRepository;
   @Inject
   TemplateImplRepository templateImplRepository;

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
   protected T prePersist(T page)
   {
      // create a unique title to be used as the page identifier
      String idTitle = PageUtils.createPageId(page.getTitle());
      String idFinal = super.testKey(idTitle);
      page.setId(idFinal);

      if (!page.isClone())
      {
         // page id of a brand new page will become the templateImpl's backward reference to its original main page
         page.getTemplate().setMainPageId(idTitle);
         templateImplRepository.persist(page.getTemplate());
      }

      return page;
   }

   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   protected T preUpdate(T page)
   {
      if (!page.isClone())
      {
         // when updating a non-clone page the user can change page.template.template.id
         templateImplRepository.update(page.getTemplate());
      }
      return page;
   }

   @Override
   public boolean delete(Object key)
   {
      try
      {
         T obj = getEm().find(getEntityType(), key);
         if (obj != null)
         {
            obj.setActive(false);
            getEm().remove(obj);
         }
         return true;
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, null, e);
         return false;
      }
   }

}
