/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.catalogue.model.Category;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
      n.setDescription(n.getDescription());
      n = super.prePersist(n);
      return n;
   }

   @Override
   protected Category preUpdate(Category n) throws Exception
   {
      n.setDescription(n.getDescription());
      n = super.preUpdate(n);
      return n;
   }

}
