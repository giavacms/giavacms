/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.MenuGroup;
import org.giavacms.base.model.MenuItem;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;


@Named
@Stateless
@LocalBean
public class MenuRepository extends AbstractRepository<MenuGroup>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   public EntityManager getEm()
   {
      return em;
   }

   protected Class<MenuGroup> getEntityType()
   {
      return MenuGroup.class;
   }

   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   public MenuItem findItem(Long id)
   {
      return getEm().find(MenuItem.class, id);
   }

   public boolean updateItem(MenuItem mi)
   {
      try
      {
         getEm().merge(mi);
         return true;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
         e.printStackTrace();
         return false;
      }
   }

   /**
    * Override this if needed
    * 
    * @param object
    * @return
    */

   protected MenuGroup preUpdate(MenuGroup mg)
   {
      for (MenuItem mi : mg.getList())
      {
         if (mi.getId() == null)
         {
            em.persist(mi);
         }
         else
         {
            em.merge(mi);
         }
      }
      processActiveMenuItems(mg);
      return mg;
   }

   public List<MenuGroup> getAllList()
   {
      List<MenuGroup> result = super.getAllList();
      processActiveMenuItems(result);
      return result;
   }

   @Override
   public List<MenuGroup> getList(Search<MenuGroup> ricerca, int startRow,
            int pageSize)
   {
      List<MenuGroup> result = super.getList(ricerca, startRow, pageSize);
      processActiveMenuItems(result);
      return result;
   }

   private void processActiveMenuItems(List<MenuGroup> result)
   {
      if (result != null)
      {
         for (MenuGroup mg : result)
         {
            processActiveMenuItems(mg);
         }
      }
   }

   private void processActiveMenuItems(MenuGroup mg)
   {
      mg.setActiveList(null);
      if (mg.getList() != null)
      {
         for (MenuItem mi : mg.getList())
         {
            if (mi.isActive())
            {
               mg.getActiveList().add(mi);
            }
         }
      }
      Collections.sort(mg.getActiveList(), new Comparator<MenuItem>()
      {
         public int compare(MenuItem o1, MenuItem o2)
         {
            return o1.getSortOrder() == null ? -1 : o1.getSortOrder()
                     .compareTo(o2.getSortOrder());
         }
      });
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "path asc";
   }

}
