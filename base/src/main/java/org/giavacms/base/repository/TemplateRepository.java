/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.Template;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;


@Named
@Stateless
@LocalBean
public class TemplateRepository extends AbstractRepository<Template> implements
         Serializable
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
   protected String getDefaultOrderBy()
   {
      return "name asc";
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected void applyRestrictions(Search<Template> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      if (search.getObj().getSearchStatico() != null)
      {
         sb.append(separator).append(alias).append(".statico = :statico ");
         params.put("statico", search.getObj().getSearchStatico());
      }

      if (search.getObj().getName() != null
               && !search.getObj().getName().isEmpty())
      {
         sb.append(separator).append(alias).append(".name like :name ");
         params.put("name", likeParam(search.getObj().getName()));
      }
   }

   @Override
   protected Template prePersist(Template t)
   {
      // closeHtmlTags(t);
      return t;
   }

   @Override
   protected Template preUpdate(Template t)
   {
      // closeHtmlTags(t);
      return t;
   }

   @Override
   public boolean delete(Object key)
   {
      try
      {
         Template obj = getEm().find(getEntityType(), key);
         if (obj != null)
         {
            obj.setActive(false);
            getEm().remove(obj);
         }
         return true;
      }
      catch (Exception e)
      {
logger.error(e.getMessage(),e);
         return false;
      }
   }

   public boolean reallyDelete(Object key)
   {
      try
      {
         Template obj = getEm().find(getEntityType(), key);
         getEm().remove(obj);
         return true;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return false;
      }
   }
}
