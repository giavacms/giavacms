package org.giavacms.base.repository;

import org.giavacms.api.repository.AbstractRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseRepository<T> extends AbstractRepository<T>
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
   protected String getDefaultOrderBy()
   {
      return "id desc";
   }

   public String makeUniqueKey(String key, String table)
   {
      String keyNotUsed = key;
      boolean found = false;
      int i = 0;
      while (!found)
      {
         logger.info("key to search: " + keyNotUsed);
         Object pageCount = getEm()
                  .createNativeQuery("select count(p.id) from " + table + " p where p.id = :KEY")
                  .setParameter("KEY", keyNotUsed).getSingleResult();
         logger.info("found " + pageCount + " pages with id: " + keyNotUsed);
         if (pageCount != null && Integer.parseInt(pageCount.toString()) > 0)
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