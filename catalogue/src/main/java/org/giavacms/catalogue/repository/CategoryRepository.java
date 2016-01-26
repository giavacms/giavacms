/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.api.model.Search;
import org.giavacms.api.util.IdUtils;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.catalogue.model.Category;

@Named
@Stateless
@LocalBean
public class CategoryRepository extends BaseRepository<Category>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected String getDefaultOrderBy()
   {
      return "orderNum asc";
   }

   @Override
   public void delete(Object key) throws Exception
   {

      Category category = getEm().find(getEntityType(), key);
      if (category != null)
      {
         category.setActive(false);
         getEm().merge(category);
      }
   }

   @Override
   protected Category prePersist(Category n) throws Exception
   {
      String idTitle = IdUtils.createPageId(n.getName());
      String idFinal = makeUniqueKey(idTitle, Category.TABLE_NAME);
      n.setId(idFinal);
      return n;
   }

   @Override
   protected void applyRestrictions(Search<Category> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // ACTIVE TYPE
      if (true)
      {
         sb.append(separator).append(alias).append(".active = :active ");
         params.put("active", true);
         separator = " and ";
      }

      // TITLE
      if (search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeName ");
         params.put("likeName", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }
      // DESCRIPTION
      if (search.getLike().getDescription() != null
               && !search.getLike().getDescription().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".description ) like :likeDescription ");
         params.put("likeDescription", likeParam(search.getLike().getDescription().trim().toUpperCase()));
         separator = " and ";
      }
   }

}
