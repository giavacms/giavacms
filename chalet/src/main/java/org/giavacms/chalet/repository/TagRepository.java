package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.api.repository.AbstractRepository;
import org.giavacms.chalet.model.Tag;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class TagRepository extends AbstractRepository<Tag>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected EntityManager getEm()
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
      if (search.getObj().getTagName() != null
               && search.getObj().getTagName().trim().length() > 0)
      {
         sb.append(separator).append(" upper ( ").append(alias)
                  .append(".tagName ) like :TAGNAME ");
         params.put("TAGNAME", likeParam(search.getObj().getTagName().trim().toUpperCase()));
         separator = " and ";
      }

      // ............................................... CONTENT TYPE fields

      // ............................................... CONTENT fields

      // ACTIVE CONTENT
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".chalet.active = :activeContent ");
         params.put("activeContent", true);
         separator = " and ";
      }

      // TITLE
      if (search.getObj().getChalet() != null && search.getObj().getChalet().getName() != null
               && !search.getObj().getChalet().getName().trim().isEmpty())
      {
         boolean likeSearch = likeSearch(likeParam(search.getObj().getChalet().getName().trim().toUpperCase()),
                  alias, separator,
                  sb, params);
         if (likeSearch)
         {
            separator = " and ";
         }
      }

      // CHALETID 1
      if (search.getObj().getChalet() != null
               && search.getObj().getChalet().getId() != null
               && search.getObj().getChalet().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".chalet.id = :CHALETID1 ");
         params.put("CHALETID1", search.getObj().getChalet().getId().trim());
         separator = " and ";
      }

      // CHALETID 2
      if (search.getObj().getChaletId() != null
               && search.getObj().getChaletId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".chaletId.id = :CHALETID ");
         params.put("CHALETID", search.getObj().getChaletId().trim());
         separator = " and ";
      }

      // TAG
      if (search.getObj().getChalet() != null && search.getObj().getChalet().getTag() != null
               && search.getObj().getChalet().getTag().trim().length() > 0)
      {
         sb.append(separator).append(alias).append(".chalet.id in ( ");
         sb.append(" select distinct rt.chalet.id from ").append(Tag.class.getSimpleName())
                  .append(" rt where rt.tagName = :CHALETTAGNAME ");
         sb.append(" ) ");
         params.put("CHALETTAGNAME", search.getObj().getChalet().getTag().trim());
         separator = " and ";
      }

      // TAG LIKE
      if (search.getObj().getChalet() != null && search.getObj().getChalet().getTagList().size() > 0)
      {
         sb.append(separator).append(" ( ");
         for (int i = 0; i < search.getObj().getChalet().getTagList().size(); i++)
         {
            sb.append(i > 0 ? " or " : "");

            // da provare quale versione piu' efficiente
            boolean usaJoin = false;
            if (usaJoin)
            {
               sb.append(alias).append(".chalet.id in ( ");
               sb.append(" select distinct rt.chalet.id from ").append(Tag.class.getSimpleName())
                        .append(" rt where upper ( rt.tagName ) like :TAGLIKE").append(i).append(" ");
               sb.append(" ) ");
            }
            else
            {
               sb.append(" upper ( ").append(alias).append(".chalet.tags ) like :TAGLIKE").append(i).append(" ");
            }

            params.put("TAGLIKE" + i, likeParam(search.getObj().getChalet().getTag().trim().toUpperCase()));
         }
         separator = " and ";
      }

   }

   public void set(String chaletId, List<String> tagList, Date date)
   {
      getEm().createQuery("delete from " + Tag.class.getSimpleName() + " t where t.chaletId = :CHALETID ")
               .setParameter("CHALETID", chaletId).executeUpdate();
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
         getEm().persist(new Tag(tagName, chaletId, day, month, year));
      }
   }

   protected boolean likeSearch(String likeText, String alias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {
      sb.append(separator).append(" upper ( ").append(alias).append(".chalet.name ) like :name ");
      params.put("name", likeText);
      return true;
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
}
