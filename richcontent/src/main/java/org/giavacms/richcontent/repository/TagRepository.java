package org.giavacms.richcontent.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.Tag;

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

      // CONTENT
      if (search.getObj().getRichContent() != null
               && search.getObj().getRichContent().getId() != null
               && search.getObj().getRichContent().getId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.id = :RICHCONTENTID1 ");
         params.put("RICHCONTENTID1", search.getObj().getRichContent().getId().trim());
         separator = " and ";
      }

      // CONTENT
      if (search.getObj().getRichContentId() != null
               && search.getObj().getRichContentId().trim().length() > 0)
      {
         sb.append(separator).append(alias)
                  .append(".richContent.id = :RICHCONTENTID2 ");
         params.put("RICHCONTENTID2", search.getObj().getRichContentId().trim());
         separator = " and ";
      }

      // NAME
      if (search.getObj().getTagName() != null
               && search.getObj().getTagName().trim().length() > 0)
      {
         sb.append(separator).append(" upper ( ").append(alias)
                  .append(".tagName ) like :TAGNAME ");
         params.put("TAGNAME", likeParam(search.getObj().getTagName().trim().toUpperCase()));
         separator = " and ";
      }

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   protected Tag construct(List<String> fieldNames, List<Object> fieldValues)
   {
      Tag t = new Tag();
      t.setRichContent(new RichContent());
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

   public void set(String richContentId, List<String> tagList, Date date)
   {
      getEm().createQuery("delete from " + Tag.class.getSimpleName() + " t where t.richContentId = :RICHCONTENTID ")
               .setParameter("RICHCONTENTID", richContentId).executeUpdate();
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
         getEm().persist(new Tag(tagName, richContentId, day, month, year));
      }
   }

}
