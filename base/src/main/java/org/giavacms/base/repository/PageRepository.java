/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.model.Search;

@Named
@Stateless
@LocalBean
public class PageRepository
         extends AbstractPageRepository<Page>
{

   private static final long serialVersionUID = 1L;

   protected void applyRestrictionsNative(Search<Page> search, String pageAlias, String templateImplAlias,
            String templateAlias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // TEMPLATE STATICO
      if (search.getObj().getTemplate() != null
               && search.getObj().getTemplate().getTemplate() != null
               && search.getObj().getTemplate().getTemplate()
                        .getSearchStatico() != null)
      {
         sb.append(separator).append(templateAlias)
                  .append(".statico = :statico ");
         params.put("statico", search.getObj().getTemplate().getTemplate()
                  .getSearchStatico());
         separator = " and ";
      }

      // TEMPLATE TEMPLATE ID
      if (search.getObj().getTemplate() != null
               && search.getObj().getTemplate().getTemplate() != null
               && search.getObj().getTemplate().getTemplate()
                        .getId() != null)
      {
         sb.append(separator).append(templateAlias)
                  .append(".id = :idTemplate ");
         params.put("idTemplate", search.getObj().getTemplate().getTemplate()
                  .getId());
         separator = " and ";
      }

      // SOLO PAGINE VERE E PROPRIE
      if (!search.getObj().isExtended())
      {

         // NO ESTENSIONI
         sb.append(separator).append(pageAlias).append(".extension is null ");
         separator = " and ";

         // NO CLONI DI PAGINE BASE (DOVREBBE ESSERE SUPERFLUO)
         sb.append(separator).append(pageAlias).append(".clone = :clone ");
         params.put("clone", false);
         separator = " and ";
      }
      // RICERCA ESTENSIONI
      else
      {
         // DI CHE TIPO
         if (search.getObj().getExtension() != null
                  && !search.getObj().getExtension().trim().isEmpty())
         {
            sb.append(separator).append(pageAlias).append(".extension = :extension ");
            params.put("extension", search.getObj().getExtension());
            separator = " and ";
         }

         // SOLO PAGINE BASE O ANCHE CLONI DI PAGINE BASE
         if (!search.getObj().isClone())
         {
            sb.append(separator).append(pageAlias).append(".clone = :clone ");
            params.put("clone", false);
            separator = " and ";
         }
      }

      String customLike = null;
      super.applyRestrictionsNative(search, pageAlias, separator, sb, params, customLike);

   }

   @Override
   public Page find(Object key)
   {
      logger.info("findPage: " + key);
      return fetch(key);
   }

   @Override
   public Page fetch(Object id)
   {
      try
      {
         logger.info("fetchPage: " + id);
         Search<Page> sp = new Search<Page>(Page.class);
         sp.getObj().setId((String) id);
         return getList(sp, 0, 1).get(0);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   public boolean reallyDelete(Object key)
   {
      try
      {
         Page obj = getEm().find(getEntityType(), key);
         getEm().remove(obj);
         return true;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return false;
      }
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "title asc";
   }

   @SuppressWarnings("unchecked")
   public List<Page> getExtensions(String extension)
   {
      try
      {
         StringBuffer sbq = new StringBuffer("select p.id, p.title from "
                  + Page.TABLE_NAME + " p where p.clone = :CLONE and p.extension ");
         if (extension != null && extension.trim().length() > 0)
         {
            sbq.append(" = :EXTENSION ");
         }
         else
         {
            sbq.append(" is null ");
         }
         Query q = getEm().createQuery(sbq.toString()).setParameter("CLONE", false);
         if (extension != null && extension.trim().length() > 0)
         {
            q.setParameter("EXTENSION", extension);
         }
         List<Page> result = new ArrayList<Page>();
         @SuppressWarnings("rawtypes")
         List resultList = q.getResultList();
         Iterator<Object[]> results = resultList.iterator();
         while (results.hasNext())
         {
            Page p = new Page();
            Object[] row = results.next();
            p.setId(row[0].toString());
            p.setTitle("" + row[1]);
            result.add(p);
         }
         return result;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return new ArrayList<Page>();
      }
   }

   public String getBasePageTitleByTemplateImplId(Long templateImplId)
   {
      try
      {
         return (String) getEm()
                  .createNativeQuery(
                           "select p.title from " + Page.TABLE_NAME
                                    + " p where p.template_id = :TID and p.clone = :CLONE ")
                  .setParameter("TID", templateImplId).setParameter("CLONE", false).getSingleResult();
      }
      catch (Exception e)
      {
         return null;
      }
   }

   @Override
   protected String getBaseList(Class<? extends Object> clazz, String alias, boolean count)
   {
      if (count)
      {
         return "select count(" + alias + ") from " + clazz.getSimpleName()
                  + " " + alias + " ";
      }
      else
      {
         return "select " + alias + " from " + clazz.getSimpleName() + " "
                  + alias + " left join fetch " + alias + ".template ti ";
      }
   }

   @Override
   public int getListSize(Search<Page> search)
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
   public List<Page> getList(Search<Page> search, int startRow,
            int pageSize)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = false;
      // a flag to tell native query whether to fetch all additional fields
      boolean completeFetch = false;
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

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private List<Page> extract(List resultList, boolean completeFetch)
   {
      List<Page> pages = new ArrayList<Page>();
      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         pages.add(extract(results.next(), completeFetch));
      }
      return pages;
   }

   /**
    * // we select a cartesian product of master/details rows in case of count = false
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".title, ");
    * sb.append(faqAlias).append(".answer, "); sb.append(faqAlias).append(".date, ");
    * sb.append(faqAlias).append(".faqCategory_id, ");
    * sb.append(faqCategoryPageAlias).append(".title AS faqCategoryTitle, ");
    * sb.append(faqCategoryAlias).append(".orderNum, "); sb.append(" I.fileName AS image ");
    */
   protected Page extract(Object[] row, boolean completeFetch)
   {
      int i = 0;
      // page
      String p_id = (String) row[i];
      i++;
      String lang1id = (String) row[i];
      i++;
      String lang2id = (String) row[i];
      i++;
      String lang3id = (String) row[i];
      i++;
      String lang4id = (String) row[i];
      i++;
      String lang5id = (String) row[i];
      i++;
      boolean p_active = false;
      if (row[i] != null && row[i] instanceof Short)
      {
         p_active = ((Short) row[i]) > 0 ? true : false;
      }
      else if (row[i] != null && row[i] instanceof Boolean)
      {
         p_active = ((Boolean) row[i]).booleanValue();
      }
      i++;
      boolean clone = false;
      if (row[i] != null && row[i] instanceof Short)
      {
         clone = ((Short) row[i]) > 0 ? true : false;
      }
      else if (row[i] != null && row[i] instanceof Boolean)
      {
         clone = ((Boolean) row[i]).booleanValue();
      }
      i++;
      String description = (String) row[i];
      i++;
      String extension = (String) row[i];
      i++;
      String title = (String) row[i];
      i++;
      // template_impl
      Long ti_id = null;
      if (row[i] != null && row[i] instanceof BigInteger)
      {
         ti_id = ((BigInteger) row[i]).longValue();
      }
      i++;
      boolean ti_active = false;
      if (row[i] != null && row[i] instanceof Short)
      {
         ti_active = ((Short) row[i]) > 0 ? true : false;
      }
      else if (row[i] != null && row[i] instanceof Boolean)
      {
         ti_active = ((Boolean) row[i]).booleanValue();
      }
      i++;
      String ti_col1 = (String) row[i];
      i++;
      String ti_col2 = (String) row[i];
      i++;
      String ti_col3 = (String) row[i];
      i++;
      String ti_header = (String) row[i];
      i++;
      String ti_footer = (String) row[i];
      i++;
      String ti_mainPageId = (String) row[i];
      i++;
      // template
      Long t_id = null;
      if (row[i] != null && row[i] instanceof BigInteger)
      {
         t_id = ((BigInteger) row[i]).longValue();
      }
      i++;
      boolean t_active = false;
      if (row[i] != null && row[i] instanceof Short)
      {
         t_active = ((Short) row[i]) > 0 ? true : false;
      }
      else if (row[i] != null && row[i] instanceof Boolean)
      {
         t_active = ((Boolean) row[i]).booleanValue();
      }
      i++;
      String t_col1_start = (String) row[i];
      i++;
      String t_col1_stop = (String) row[i];
      i++;
      String t_col2_start = (String) row[i];
      i++;
      String t_col2_stop = (String) row[i];
      i++;
      String t_col3_start = (String) row[i];
      i++;
      String t_col3_stop = (String) row[i];
      i++;
      String t_header_start = (String) row[i];
      i++;
      String t_header_stop = (String) row[i];
      i++;
      String t_footer_start = (String) row[i];
      i++;
      String t_footer_stop = (String) row[i];
      i++;
      String t_name = (String) row[i];
      i++;
      boolean t_statico = false;
      if (row[i] != null && row[i] instanceof Short)
      {
         t_statico = ((Short) row[i]) > 0 ? true : false;
      }
      else if (row[i] != null && row[i] instanceof Boolean)
      {
         t_statico = ((Boolean) row[i]).booleanValue();
      }
      i++;
      // let's put it all together
      Page page = new Page();
      page.setActive(p_active);
      page.setClone(clone);
      page.setDescription(description);
      page.setExtension(extension);
      page.setId(p_id);
      page.setLang1id(lang1id);
      page.setLang2id(lang2id);
      page.setLang3id(lang3id);
      page.setLang4id(lang4id);
      page.setLang5id(lang5id);
      page.setTemplate(new TemplateImpl());
      page.setTemplateId(ti_id);
      page.setTitle(title);
      page.getTemplate().setActive(ti_active);
      page.getTemplate().setCol1(ti_col1);
      page.getTemplate().setCol2(ti_col2);
      page.getTemplate().setCol3(ti_col3);
      page.getTemplate().setFooter(ti_footer);
      page.getTemplate().setHeader(ti_header);
      page.getTemplate().setId(ti_id);
      page.getTemplate().setMainPageId(ti_mainPageId);
      page.getTemplate().setTemplate(new Template());
      page.getTemplate().getTemplate().setActive(t_active);
      page.getTemplate().getTemplate().setCol1_start(t_col1_start);
      page.getTemplate().getTemplate().setCol1_stop(t_col1_stop);
      page.getTemplate().getTemplate().setCol2_start(t_col2_start);
      page.getTemplate().getTemplate().setCol2_stop(t_col2_stop);
      page.getTemplate().getTemplate().setCol3_start(t_col3_start);
      page.getTemplate().getTemplate().setCol3_stop(t_col3_stop);
      page.getTemplate().getTemplate().setFooter_start(t_footer_start);
      page.getTemplate().getTemplate().setFooter_stop(t_footer_stop);
      page.getTemplate().getTemplate().setHeader_start(t_header_start);
      page.getTemplate().getTemplate().setHeader_stop(t_header_stop);
      page.getTemplate().getTemplate().setId(t_id);
      page.getTemplate().getTemplate().setName(t_name);
      page.getTemplate().getTemplate().setStatico(t_statico);

      // i18n support
      page.setLang(page.getId().equals(page.getLang1id()) ? 1 : page.getId()
               .equals(page.getLang2id()) ? 2 : page.getId().equals(
               page.getLang3id()) ? 3 : page.getId()
               .equals(page.getLang4id()) ? 4 : page.getId().equals(
               page.getLang5id()) ? 5 : 0);

      return page;

   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   protected StringBuffer getListNative(Search<Page> search, Map<String, Object> params, boolean count, int startRow,
            int pageSize, boolean completeFetch)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String templateImplAlias = "TI";
      String templateAlias = "T";

      // query string buffer
      StringBuffer sb = new StringBuffer(
               "SELECT ");

      if (count)
      {
         // we only count distinct results in case of count = true
         sb.append(" count( distinct ").append(pageAlias).append(".id ) ");
      }
      else
      {
         sb.append(pageAlias).append(".id as p_id, ");
         sb.append(pageAlias).append(".lang1id, ");
         sb.append(pageAlias).append(".lang2id, ");
         sb.append(pageAlias).append(".lang3id, ");
         sb.append(pageAlias).append(".lang4id, ");
         sb.append(pageAlias).append(".lang5id, ");
         sb.append(pageAlias).append(".active as p_active, ");
         sb.append(pageAlias).append(".clone, ");
         sb.append(pageAlias).append(".description, ");
         sb.append(pageAlias).append(".extension, ");
         sb.append(pageAlias).append(".title, ");
         sb.append(templateImplAlias).append(".id as ti_id, ");
         sb.append(templateImplAlias).append(".active as ti_active, ");
         sb.append(templateImplAlias).append(".col1, ");
         sb.append(templateImplAlias).append(".col2, ");
         sb.append(templateImplAlias).append(".col3, ");
         sb.append(templateImplAlias).append(".header, ");
         sb.append(templateImplAlias).append(".footer, ");
         sb.append(templateImplAlias).append(".mainPageId, ");
         sb.append(templateAlias).append(".id as t_id, ");
         sb.append(templateAlias).append(".active as t_active, ");
         sb.append(templateAlias).append(".col1_start, ");
         sb.append(templateAlias).append(".col1_stop, ");
         sb.append(templateAlias).append(".col2_start, ");
         sb.append(templateAlias).append(".col2_stop, ");
         sb.append(templateAlias).append(".col3_start, ");
         sb.append(templateAlias).append(".col3_stop, ");
         sb.append(templateAlias).append(".header_start, ");
         sb.append(templateAlias).append(".header_stop, ");
         sb.append(templateAlias).append(".footer_start, ");
         sb.append(templateAlias).append(".footer_stop, ");
         sb.append(templateAlias).append(".name as t_name, ");
         sb.append(templateAlias).append(".statico ");
         if (completeFetch)
         {
            // additional fields to retrieve only when fetching
         }
      }

      sb.append(" FROM ").append(Page.TABLE_NAME).append(" ").append(pageAlias);
      sb.append(" LEFT JOIN ");
      sb.append(TemplateImpl.TABLE_NAME).append(" ").append(templateImplAlias).append(" on ").append(pageAlias)
               .append(".template_id = ").append(templateImplAlias).append(".id ");
      sb.append(" LEFT JOIN ");
      sb.append(Template.TABLE_NAME).append(" ").append(templateAlias).append(" on ").append(templateImplAlias)
               .append(".template_id = ").append(templateAlias).append(".id ");

      String separator = " where ";
      applyRestrictionsNative(search, pageAlias, templateImplAlias, templateAlias,
               separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(pageAlias).append(".title asc ");

         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

      }
      return sb;
   }

}