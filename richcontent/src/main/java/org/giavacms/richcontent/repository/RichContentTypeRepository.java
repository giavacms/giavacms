package org.giavacms.richcontent.repository;

import org.giavacms.api.model.Search;
import org.giavacms.api.repository.AbstractRepository;
import org.giavacms.richcontent.model.RichContentType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Map;

@Named
@Stateless
@LocalBean
public class RichContentTypeRepository extends AbstractRepository<RichContentType>
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
      return "name asc";
   }

   @Override
   protected void applyRestrictions(Search<RichContentType> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {
      sb.append(separator).append(alias).append(".active = :active");
      params.put("active", true);
      separator = " and ";
      if (search.getObj().getName() != null
               && !search.getObj().getName().isEmpty())
      {
         sb.append(separator).append(alias)
                  .append(".name = :NAME ");
         params.put("NAME", search.getObj().getName()
                  .trim());
      }

      if (search.getLike().getName() != null
               && !search.getLike().getName().isEmpty())
      {
         sb.append(separator).append(" upper(").append(alias)
                  .append(".name ) like :NAMELIKE ");
         params.put("NAMELIKE", likeParam(search.getLike().getName().trim()
                  .toUpperCase()));
      }

   }

   @Override
   public void delete(Object key) throws Exception
   {
      RichContentType richContentType = getEm().find(getEntityType(), key);
      if (richContentType != null)
      {
         richContentType.setActive(false);
         getEm().merge(richContentType);
      }
   }

}
