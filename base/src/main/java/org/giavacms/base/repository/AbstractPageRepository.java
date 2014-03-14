/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;

public abstract class AbstractPageRepository<T extends Page> extends
         AbstractRepository<T>
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
   protected T prePersist(T page)
   {
      // create a unique title to be used as the page identifier
      String idTitle = PageUtils.createPageId(page.getTitle());
      String idFinal = testKey(idTitle);
      page.setId(idFinal);

      if (page.isClone())
      {
         // clone pages can change their base page by changing the templateImpl.id they are associated to. must be
         // refetched here to avoid cascading problems
         if (page.getTemplateId() != null)
         {
            page.setTemplate(getEm().find(TemplateImpl.class, page.getTemplateId()));
         }
      }
      else
      {
         // page id of a brand new page will become the templateImpl's backward reference to its original main page
         page.getTemplate().setMainPageId(idTitle);
         page.getTemplate().setMainPageTitle(page.getTitle());
         getEm().persist(page.getTemplate());
      }

      return page;
   }

   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   protected T preUpdate(T page)
   {
      if (page.isClone())
      {
         // clone pages can change their base page by changing the templateImpl.id they are associated to. must be
         // refetched here to avoid cascading problems
         if (page.getTemplateId() != null)
         {
            page.setTemplate(getEm().find(TemplateImpl.class, page.getTemplateId()));
         }
      }
      else
      {
         // when updating a base (non-clone) page, instead, the user can change page.template and
         // page.template.template.id
         page.getTemplate().setMainPageTitle(page.getTitle());
         getEm().merge(page.getTemplate());
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
         logger.error(e.getMessage(), e);
         return false;
      }
   }

   public boolean destroy(T t)
   {
      throw new RuntimeException("not implemented");
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

      // PAGE
      if (search.getObj().getId() != null && search.getObj().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".id = :BASEPAGE_ID ");
         params.put("BASEPAGE_ID", search.getObj().getId());
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

   protected void applyRestrictionsNative(Search<T> search, String pageAlias,
            String separator, StringBuffer sb, Map<String, Object> params, String customLike)
   {

      // ACTIVE
      if (true)
      {
         sb.append(separator).append(pageAlias).append(".active = :active");
         params.put("active", true);
         separator = " and ";
      }

      // PAGE ID
      if (search.getObj().getId() != null && search.getObj().getId().trim().length() > 0)
      {
         sb.append(separator).append(pageAlias).append(".id = :BASEPAGE_ID ");
         params.put("BASEPAGE_ID", search.getObj().getId());
         separator = " and ";
      }

      // BASE PAGE
      if (search.getObj().getTemplate() != null && search.getObj().getTemplate().getId() != null)
      {
         sb.append(separator).append(pageAlias).append(".template_id = :BASEPAGE_TEMPLATE_ID ");
         params.put("BASEPAGE_TEMPLATE_ID", search.getObj().getTemplate().getId());
         separator = " and ";
      }

      // TITLE
      if (search.getObj().getTitle() != null
               && !search.getObj().getTitle().trim().isEmpty())
      {
         boolean likeSearch = likeSearchNative(likeParam(search.getObj().getTitle().trim().toUpperCase()), pageAlias,
                  separator,
                  sb, params, customLike);
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
            sb.append(separator).append(pageAlias).append(".id = ")
                     .append(pageAlias).append(".lang1id ");
         }
         else if (search.getObj().getLang() == 2)
         {
            sb.append(separator).append(pageAlias).append(".id = ")
                     .append(pageAlias).append(".lang2id ");
         }
         else if (search.getObj().getLang() == 3)
         {
            sb.append(separator).append(pageAlias).append(".id = ")
                     .append(pageAlias).append(".lang3id ");
         }
         else if (search.getObj().getLang() == 4)
         {
            sb.append(separator).append(pageAlias).append(".id = ")
                     .append(pageAlias).append(".lang4id ");
         }
         else if (search.getObj().getLang() == 5)
         {
            sb.append(separator).append(pageAlias).append(".id = ")
                     .append(pageAlias).append(".lang5id ");
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

   protected boolean likeSearchNative(String likeText, String pageAlias,
            String separator, StringBuffer sb, Map<String, Object> params, String alternativeLike)
   {
      sb.append(separator).append(" ( ");

      sb.append(" upper ( ").append(pageAlias).append(".title ) LIKE :LIKETEXT ");
      if (alternativeLike != null)
      {
         sb.append(" or ").append(alternativeLike);
      }
      sb.append(" ) ");

      // params.put("LIKETEXT", StringUtils.clean(likeText));
      params.put("LIKETEXT", likeText);

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

   @Override
   public T find(Object id)
   {
      try
      {
         logger.info("findPage: " + id);
         Search<T> sp = new Search<T>(getEntityType());
         sp.getObj().setId((String) id);
         boolean completeFetch = false;
         List<T> list = getList(sp, 0, 1, completeFetch);
         return list == null ? null : list.size() == 0 ? null : list.get(0);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   public T fetch(Object id)
   {
      try
      {
         logger.info("fetchPage: " + id);
         Search<T> sp = new Search<T>(getEntityType());
         sp.getObj().setId((String) id);
         boolean completeFetch = true;
         List<T> list = getList(sp, 0, 1, completeFetch);
         return list == null ? null : list.size() == 0 ? null : list.get(0);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   public int getListSize(Search<T> search)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = true;
      // a flag to tell native query whether to fetch all additional fields
      boolean completeFetch = false;
      // the native query
      StringBuffer string_query = getListNative(search, params, count, 0, 0, completeFetch);
      Query query = getEm().createNativeQuery(string_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return ((BigInteger) query.getSingleResult()).intValue();
   }

   @Override
   public List<T> getList(Search<T> search, int startRow,
            int pageSize)
   {
      // a flag to tell native query whether to fetch all additional fields
      boolean completeFetch = false;
      return getList(search, startRow, pageSize, completeFetch);
   }

   protected List<T> getList(Search<T> search, int startRow,
            int pageSize, boolean completeFetch)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = false;
      // the native query
      StringBuffer stringbuffer_query = getListNative(search, params, count, startRow, pageSize, completeFetch);
      Query query = getEm().createNativeQuery(stringbuffer_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return extract(query.getResultList(), completeFetch);
   }

   @SuppressWarnings("rawtypes")
   abstract protected List<T> extract(List resultList, boolean completeFetch);

   abstract protected StringBuffer getListNative(Search<T> search, Map<String, Object> params, boolean count,
            int startRow,
            int pageSize, boolean completeFetch);

   public boolean moveDependencies(String id, String id2)
   {
      logger.debug("nothing to move");
      return true;
   }

}
