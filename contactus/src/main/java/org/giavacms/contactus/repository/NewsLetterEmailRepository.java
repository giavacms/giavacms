package org.giavacms.contactus.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.contactus.model.NewsLetterEmail;

@Named
@Stateless
@LocalBean
public class NewsLetterEmailRepository extends AbstractRepository<NewsLetterEmail>
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
   protected void applyRestrictions(Search<NewsLetterEmail> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";

      if (search.getObj().getName() != null
               && !search.getObj().getName().trim().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".name LIKE :NAME ");
         params.put("NAME", likeParam(search.getObj().getName()));
      }
      if (search.getObj().getEmail() != null
               && !search.getObj().getEmail().trim().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".email LIKE :EMAIL ");
         params.put("EMAIL", likeParam(search.getObj().getEmail()));
      }
   }

   @Override
   public NewsLetterEmail fetch(Object key)
   {
      return super.fetch(key);
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }
}
