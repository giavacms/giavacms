/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.common.model.Search;
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

   @Override
   protected void applyRestrictions(Search<T> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // ACTIVE
      if (true)
      {
         sb.append(separator).append(alias).append(".active = :active");
         params.put("active", true);
         separator = " and ";
      }

      // BASE PAGE
      if (search.getObj().getTemplate() != null && search.getObj().getTemplate().getId() != null)
      {
         sb.append(separator).append(alias).append(".template.id = :BASEPAGE_TEMPLATE_ID ");
         params.put("BASEPAGE_TEMPLATE_ID", search.getObj().getTemplate().getId());
         separator = " and ";
      }

      // TITLE
      if (search.getObj().getTitle() != null
               && !search.getObj().getTitle().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".title ) like :title ");
         params.put("title", likeParam(search.getObj().getTitle().trim().toUpperCase()));
         separator = " and ";
      }

      // LINGUA
      if (search.getObj().getLang() > 0)
      {
         if (search.getObj().getLang() == 1)
         {
            sb.append(separator).append(alias).append(".id = ")
                     .append(alias).append(".lang1id ");
         }
         else if (search.getObj().getLang() == 2)
         {
            sb.append(separator).append(alias).append(".id = ")
                     .append(alias).append(".lang2id ");
         }
         else if (search.getObj().getLang() == 3)
         {
            sb.append(separator).append(alias).append(".id = ")
                     .append(alias).append(".lang3id ");
         }
         else if (search.getObj().getLang() == 4)
         {
            sb.append(separator).append(alias).append(".id = ")
                     .append(alias).append(".lang4id ");
         }
         else if (search.getObj().getLang() == 5)
         {
            sb.append(separator).append(alias).append(".id = ")
                     .append(alias).append(".lang5id ");
         }
      }

   }

}
