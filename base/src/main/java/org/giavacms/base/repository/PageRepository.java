/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.math.BigInteger;
import java.util.ArrayList;
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

   @Override
   protected void applyRestrictions(Search<Page> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      if (search.getObj().getTemplate() != null
               && search.getObj().getTemplate().getTemplate() != null
               && search.getObj().getTemplate().getTemplate()
                        .getSearchStatico() != null)
      {
         sb.append(separator).append(alias)
                  .append(".template.template.statico = :statico ");
         params.put("statico", search.getObj().getTemplate().getTemplate()
                  .getSearchStatico());
         separator = " and ";
      }

      if (search.getObj().getTemplate() != null
               && search.getObj().getTemplate().getTemplate() != null
               && search.getObj().getTemplate().getTemplate()
                        .getId() != null)
      {
         sb.append(separator).append(alias)
                  .append(".template.template.id = :idTemplate ");
         params.put("idTemplate", search.getObj().getTemplate().getTemplate()
                  .getId());
         separator = " and ";
      }

      // SOLO PAGINE VERE E PROPRIE
      if (!search.getObj().isExtended())
      {

         // NO ESTENSIONI
         sb.append(separator).append(alias).append(".extension is null ");
         separator = " and ";

         // NO CLONI DI PAGINE BASE (DOVREBBE ESSERE SUPERFLUO)
         sb.append(separator).append(alias).append(".clone = :clone ");
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
            sb.append(separator).append(alias).append(".extension = :extension ");
            params.put("extension", search.getObj().getExtension());
            separator = " and ";
         }

         // SOLO PAGINE BASE O ANCHE CLONI DI PAGINE BASE
         if (!search.getObj().isClone())
         {
            sb.append(separator).append(alias).append(".clone = :clone ");
            params.put("clone", false);
            separator = " and ";
         }
      }

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   public Page fetch(Object id)
   {
      logger.info("fetchPage: " + id);
      Page page = null;
      try
      {
         // ret = (Page) em
         // .createQuery(
         // "select p from Page p left join fetch p.template ti left join fetch ti.template t where p.id = :ID ")
         // .setParameter("ID", id).setMaxResults(1).getSingleResult();
         // em.refresh(ret);

         @SuppressWarnings("unchecked")
         Iterator<Object[]> results = getEm()
                  .createNativeQuery(
                           "select "
                                    + " p.id as p_id, p.lang1id, p.lang2id, p.lang3id, p.lang4id, p.lang5id, p.active as p_active, p.clone, p.description, p.extension, p.title, "
                                    +
                                    " ti.id as ti_id, ti.active as ti_active, ti.col1, ti.col2, ti.col3, ti.header, ti.footer, ti.mainPageId, "
                                    +
                                    " t.id as t_id, t.active as t_active, t.col1_start, t.col1_stop, t.col2_start, t.col2_stop, t.col3_start, t.col3_stop, t.header_start, t.header_stop, t.footer_start, t.footer_stop, t.name as t_name, t.statico "
                                    + " from "
                                    + Page.TABLE_NAME
                                    + " p," + TemplateImpl.TABLE_NAME + " ti, " + Template.TABLE_NAME
                                    + " t where p.id = :ID and p.template_id = ti.id and ti.template_id = t.id "
                  )
                  .setParameter("ID", id).getResultList().iterator();
         while (results.hasNext())
         {
            Object[] row = results.next();
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
            page = new Page();
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
            break;
         }

         // i18n support
         page.setLang(page.getId().equals(page.getLang1id()) ? 1 : page.getId()
                  .equals(page.getLang2id()) ? 2 : page.getId().equals(
                  page.getLang3id()) ? 3 : page.getId()
                  .equals(page.getLang4id()) ? 4 : page.getId().equals(
                  page.getLang5id()) ? 5 : 0);
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return page;
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

   // @Override
   // public boolean delete(Object key) {
   // try {
   // Page obj = getEm().find(getEntityType(), key);
   // if (obj != null) {
   // obj.setActive(false);
   // getEm().remove(obj);
   // }
   // return true;
   // } catch (Exception e) {
   // logger.error(e.getMessage(), e);
   // return false;
   // }
   // }

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
         StringBuffer sbq = new StringBuffer("select p from "
                  + Page.class.getSimpleName() + " p where p.clone = :CLONE and p.extension ");
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
         return q.getResultList();
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
                  .createQuery(
                           "select p.title from " + Page.class.getSimpleName()
                                    + " p where p.template.id = :TID and p.clone = :CLONE ")
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

}
