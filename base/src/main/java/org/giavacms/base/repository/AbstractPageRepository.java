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
import org.giavacms.common.util.StringUtils;

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
      String idFinal = testKey(idTitle);
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
            getEm().merge(obj);
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
         boolean likeSearch = likeSearch(likeParam(search.getObj().getTitle().trim().toUpperCase()), alias, separator,
                  sb, params);
         if (likeSearch)
         {
            separator = " and ";
         }
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

   /**
    * Override this to perform like search both in title and other fields...
    * 
    * @param trim
    * @param alias
    * @param separator
    * @param sb
    * @param params
    */
   protected boolean likeSearch(String likeText, String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      sb.append(separator).append(" upper ( ").append(alias).append(".title ) like :title ");
      // params.put("title", StringUtils.clean(likeText)); ...% would be removed!!
      params.put("title", likeText);
      return true;
   }

   /**
    * key uniqueness must be tested agains all pages type, not only agains actual <T> pages
    */
   @Override
   public String testKey(String key)
   {
      String keyNotUsed = key;
      boolean found = false;
      int i = 0;
      while (!found)
      {
         logger.info("key to search: " + keyNotUsed);
         Long pageCount = (Long) getEm()
                  .createQuery("select count(p.id) from " + Page.class.getSimpleName() + " p where p.id = :KEY")
                  .setParameter("KEY", keyNotUsed).getSingleResult();
         logger.info("found " + pageCount + " pages with id: " + keyNotUsed);
         if (pageCount != null && pageCount > 0)
         {
            i++;
            keyNotUsed = key + "-" + i;
         }
         else
         {
            found = true;
            return keyNotUsed;
         }
      }
      return "";
   }

}
