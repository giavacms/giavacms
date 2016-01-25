package org.giavacms.base.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.api.model.Search;
import org.giavacms.api.repository.AbstractRepository;
import org.giavacms.base.model.Tag;

@Named
@Stateless
@LocalBean
public class TagRepository extends AbstractRepository<Tag>
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
      return "tagName asc";
   }

   /**
    * criteri di default, comuni a tutti, ma specializzabili da ogni EJB tramite overriding
    */

   @Override
   protected void applyRestrictions(Search<Tag> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // ............................................... TAG fields

      // NAME
      if (search.getLike().getTagName() != null
               && search.getLike().getTagName().trim().length() > 0)
      {
         sb.append(separator).append(" upper ( ").append(alias)
                  .append(".tagName ) like :TAGNAME ");
         params.put("TAGNAME", likeParam(search.getLike().getTagName().trim().toUpperCase()));
         separator = " and ";
      }

      // ............................................... CONTENT TYPE fields

      // ACTIVE TYPE
      if (search.getObj().getEntityType() != null && search.getObj().getEntityType().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".entityType = :entityType ");
         params.put("entityType", search.getObj().getEntityType());
         separator = " and ";

         sb.append(separator).append(alias)
                  .append(".entityId in ( ");

         String entityAlias = " ea ";
         String and = " and ";
         sb.append(" select distinct ").append(entityAlias).append(".id ");
         sb.append(" from ").append(search.getObj().getEntityType()).append(entityAlias);
         sb.append(" where ").append(entityAlias).append(".active = :active ");

         if (search.getObj().getSubTypeField() != null && !search.getObj().getSubTypeField().trim().isEmpty())
         {
            if (search.getObj().getSubTypeValue() != null && !search.getObj().getSubTypeValue().trim().isEmpty())
            {
               sb.append(and).append(entityAlias).append(".").append(search.getObj().getSubTypeField())
                        .append(" = :subTypeValue ");
               params.put("subTypeValue", search.getObj().getSubTypeValue().trim());
            }
         }

         sb.append(" ) ");
         separator = " and ";
      }
   }

   @Override
   protected Tag construct(List<String> fieldNames, List<Object> fieldValues)
   {
      Tag t = new Tag();
      for (int i = 0; i < fieldNames.size(); i++)
      {
         if ("tagName".equals(fieldNames.get(i)))
         {
            t.setTagName((String) fieldValues.get(i));
         }
         else if ("day".equals(fieldNames.get(i)))
         {
            t.setDay((Integer) fieldValues.get(i));
         }
         else if ("month".equals(fieldNames.get(i)))
         {
            t.setMonth((Integer) fieldValues.get(i));
         }
         else if ("year".equals(fieldNames.get(i)))
         {
            t.setYear((Integer) fieldValues.get(i));
         }
      }
      return t;
   }

   public void set(String entityType, String entityId, List<String> tagList, Date date)
   {
      getEm().createQuery(
               "delete from " + Tag.class.getSimpleName()
                        + " t where t.entityType = :entityType and t.entityId = :entityId ")
               .setParameter("entityType", entityType).setParameter("entityId", entityId).executeUpdate();
      if (date == null)
      {
         date = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int month = cal.get(Calendar.MONTH) + 1;
      int year = cal.get(Calendar.YEAR);
      for (String tagName : tagList)
      {
         getEm().persist(new Tag(tagName, entityType, entityId, day, month, year));
      }
   }

}
