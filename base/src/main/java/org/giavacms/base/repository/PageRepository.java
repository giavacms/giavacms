/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import org.giavacms.base.model.Page;
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
      Page ret = null;
      try
      {
         ret = (Page) em
                  .createQuery(
                           "select p from Page p left join fetch p.template ti left join fetch ti.template t where p.id = :ID ")
                  .setParameter("ID", id).setMaxResults(1).getSingleResult();
         em.refresh(ret);
         // i18n support
         ret.setLang(ret.getId().equals(ret.getLang1id()) ? 1 : ret.getId()
                  .equals(ret.getLang2id()) ? 2 : ret.getId().equals(
                  ret.getLang3id()) ? 3 : ret.getId()
                  .equals(ret.getLang4id()) ? 4 : ret.getId().equals(
                  ret.getLang5id()) ? 5 : 0);
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return ret;
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
         logger.log(Level.SEVERE, null, e);
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
   // logger.log(Level.SEVERE, null, e);
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
         logger.log(Level.SEVERE, null, e);
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
