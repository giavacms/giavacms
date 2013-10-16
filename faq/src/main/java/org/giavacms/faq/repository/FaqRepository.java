package org.giavacms.faq.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.model.FaqCategory;

@Named
@Stateless
@LocalBean
public class FaqRepository extends AbstractPageRepository<Faq>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "faqCategory.orderNum asc,date desc";
   }

   @Override
   protected Faq prePersist(Faq faq)
   {
      faq.setClone(true);
      faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
      faq.setTitle(HtmlUtils.normalizeHtml(faq.getTitle()));
      return super.prePersist(faq);
   }

   @Override
   protected Faq preUpdate(Faq faq)
   {
      faq.setClone(true);
      faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
      faq.setTitle(HtmlUtils.normalizeHtml(faq.getTitle()));
      return super.preUpdate(faq);
   }

   @Override
   public int getListSize(Search<Faq> search)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = true;
      // the native query
      StringBuffer string_query = getListNative(search, params, count, 0, 0);
      Query query = getEm().createNativeQuery(string_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return ((BigInteger) query.getSingleResult()).intValue();
   }

   @Override
   public List<Faq> getList(Search<Faq> search, int startRow,
            int pageSize)
   {
      // parameters map - the same in both getList() and getListSize() usage
      Map<String, Object> params = new HashMap<String, Object>();
      // a flag to drive native query construction
      boolean count = false;
      // the native query
      StringBuffer stringbuffer_query = getListNative(search, params, count, startRow, pageSize);
      Query query = getEm().createNativeQuery(stringbuffer_query.toString());
      // substituition of parameters
      for (String param : params.keySet())
      {
         query.setParameter(param, params.get(param));
      }
      // result extraction
      return extract(query.getResultList());
   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   protected StringBuffer getListNative(Search<Faq> search, Map<String, Object> params, boolean count,
            int startRow, int pageSize)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String faqAlias = "F";
      String faqCategoryAlias = "FC";
      String faqCategoryPageAlias = "FCP";

      // query string buffer
      StringBuffer sb = new StringBuffer(
               "SELECT ");

      if (count)
      {
         // we only count distinct results in case of count = true
         sb.append(" count( distinct ").append(pageAlias).append(".id ) ");
      }
      else
      {
         // we select a cartesian product of master/details rows in case of count = false
         sb.append(pageAlias).append(".id, ");
         sb.append(pageAlias).append(".title, ");
         sb.append(faqAlias).append(".answer, ");
         sb.append(faqAlias).append(".date, ");
         sb.append(faqAlias).append(".faqCategory_id, ");
         sb.append(faqCategoryPageAlias).append(".title AS faqCategoryTitle, ");
         sb.append(faqCategoryAlias).append(".orderNum, ");
         sb.append(" I.fileName AS image ");
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(Faq.TABLE_NAME).append(" AS ").append(faqAlias);
      sb.append(" LEFT JOIN ").append(FaqCategory.TABLE_NAME).append(" AS ").append(faqCategoryAlias)
               .append(" ON ( ").append(faqCategoryAlias).append(".id = ").append(faqAlias)
               .append(".faqCategory_id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(faqAlias).append(".id = ")
               .append(pageAlias).append(".id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(faqCategoryPageAlias).append(" on ( ")
               .append(faqCategoryAlias).append(".id = ")
               .append(faqCategoryPageAlias).append(".id ) ");
      sb.append(" LEFT JOIN Image as I on ( I.id = ").append(faqCategoryAlias).append(".image_id ) ");

      if (count)
      {
         // we optimize the count query by avoiding useless left joins
      }
      else
      {
         // we need details-from clause in case of count = false
         if (Faq.HAS_DETAILS)
         {
            // (this is not the case with faqs)
            // sb.append(" LEFT JOIN RichContent_Document AS RD ON ( RD.RichContent_id = R.id ) ");
            // sb.append(" LEFT JOIN Document AS D ON ( RD.documents_id = D.id ) ");
            // sb.append(" LEFT JOIN RichContent_Image AS RI ON ( RI.RichContent_id = R.id ) ");
            // sb.append(" LEFT JOIN Image as I on ( I.id = RI.images_id ) ");
         }
      }

      String innerPageAlias = null;
      String innerFaqAlias = null;
      String innerFaqCategoryAlias = null;
      String innerFaqCategoryPageAlias = null;
      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerFaqAlias = faqAlias;
         innerFaqCategoryAlias = faqCategoryAlias;
         innerFaqCategoryPageAlias = faqCategoryPageAlias;
      }
      else if (Faq.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerFaqAlias = "F1";
         innerFaqCategoryAlias = "FC1";
         innerFaqCategoryPageAlias = "FCP1";
         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(Faq.TABLE_NAME).append(" AS ").append(innerFaqAlias);
         sb.append(" LEFT JOIN ").append(Faq.TABLE_NAME).append(" AS ").append(innerFaqCategoryAlias)
                  .append(" ON ( ").append(innerFaqCategoryAlias).append(".id = ").append(innerFaqAlias)
                  .append(".faqCategory_id ) ");
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerFaqAlias).append(".id = ")
                  .append(innerPageAlias).append(".id ) ");
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerFaqCategoryPageAlias)
                  .append(" on ( ")
                  .append(innerFaqCategoryAlias).append(".id = ")
                  .append(innerFaqCategoryPageAlias).append(".id ) ");
      }
      else
      {
         // we also don't need an inner query in case of master-data that has no multiple details
         // so aliases can stay the same
         innerPageAlias = pageAlias;
         innerFaqAlias = faqAlias;
         innerFaqCategoryAlias = faqCategoryAlias;
         innerFaqCategoryPageAlias = faqCategoryPageAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerFaqAlias, innerFaqCategoryAlias, innerFaqCategoryPageAlias,
               separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(innerPageAlias).append(".title desc ");
         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

         if (Faq.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(pageAlias).append(".title desc ");
         }

      }
      return sb;
   }

   protected void applyRestrictionsNative(Search<Faq> search, String pageAlias, String faqAlias,
            String faqCategoryAlias, String faqCategoryPageAlias, String separator, StringBuffer sb,
            Map<String, Object> params)
   {

      // CATEGORY ACTIVE
      if (true)
      {
         sb.append(separator).append(faqCategoryPageAlias)
                  .append(".active = :activeCategory");
         params.put("activeCategory", true);
         separator = " and ";
      }

      // CATEGORY TITLE
      if (search.getObj().getFaqCategory() != null
               && search.getObj().getFaqCategory().getTitle() != null
               && search.getObj().getFaqCategory().getTitle().trim().length() > 0)
      {
         sb.append(separator).append(faqCategoryPageAlias)
                  .append(".title = :NAMECAT ");
         params.put("NAMECAT", search.getObj().getFaqCategory().getTitle());
      }

      // CATEGORY ID
      if (search.getObj().getFaqCategory() != null
               && search.getObj().getFaqCategory().getId() != null
               && search.getObj().getFaqCategory().getId().trim().length() > 0)
      {
         sb.append(separator).append(faqCategoryAlias)
                  .append(".id = :IDCAT ");
         params.put("IDCAT", search.getObj().getFaqCategory().getId());
      }

      // Answer
      if (search.getObj().getAnswer() != null
               && !search.getObj().getAnswer().isEmpty())
      {
         sb.append(separator + " upper(").append(faqAlias)
                  .append(".answer) LIKE :ANSWER ");
         params.put("ANSWER", likeParam(search.getObj().getAnswer().trim()
                  .toUpperCase()));
      }

      String customLike = null;
      super.applyRestrictionsNative(search, faqCategoryPageAlias, separator, sb, params, customLike);

   }

   /**
    * // we select a cartesian product of master/details rows in case of count = false
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".title, ");
    * sb.append(faqAlias).append(".answer, "); sb.append(faqAlias).append(".date, ");
    * sb.append(faqAlias).append(".faqCategory_id, ");
    * sb.append(faqCategoryPageAlias).append(".title AS faqCategoryTitle, ");
    * sb.append(faqCategoryAlias).append(".orderNum, "); sb.append(" I.fileName AS image ");
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected List<Faq> extract(List resultList)
   {
      List<Faq> faqs = new ArrayList<Faq>();
      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         faqs.add(extract(results.next()));
      }
      return faqs;
   }

   /**
    * // we select a cartesian product of master/details rows in case of count = false
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".title, ");
    * sb.append(faqAlias).append(".answer, "); sb.append(faqAlias).append(".date, ");
    * sb.append(faqAlias).append(".faqCategory_id, ");
    * sb.append(faqCategoryPageAlias).append(".title AS faqCategoryTitle, ");
    * sb.append(faqCategoryAlias).append(".orderNum, "); sb.append(" I.fileName AS image ");
    */
   protected Faq extract(Object[] row)
   {
      Faq faq = new Faq();
      faq.setActive(true);
      int i = 0;
      String id = (String) row[i];
      // if (id != null && !id.isEmpty())
      faq.setId(id);
      i++;
      String title = (String) row[i];
      // if (title != null && !title.isEmpty())
      faq.setTitle(title);
      i++;
      String answer = (String) row[i];
      // if (answer != null && !answer.isEmpty())
      faq.setAnswer(answer);
      i++;
      Timestamp date = (Timestamp) row[i];
      if (date != null)
      {
         faq.setDate(new Date(date.getTime()));
      }
      i++;
      FaqCategory faqCategory = new FaqCategory();
      faqCategory.setActive(true);
      faq.setFaqCategory(faqCategory);
      String categoryId = (String) row[i];
      // if (categoryId != null && !categoryId.isEmpty())
      faqCategory.setId(categoryId);
      i++;
      String categoryTitle = (String) row[i];
      // if (categoryTitle != null && !categoryTitle.isEmpty())
      faqCategory.setTitle(categoryTitle);
      i++;
      Object orderNumer = row[i];
      if (orderNumer != null)
      {
         if (orderNumer instanceof Integer)
         {
            faqCategory.setOrderNum((Integer) row[i]);
         }
         else
         {
            logger.error("orderNum instance of " + orderNumer.getClass().getCanonicalName());
         }
      }
      i++;
      if (row[i] != null)
      {
         Image image = new Image();
         faqCategory.setImage(image);
         String filename = (String) row[i];
         // if (filename != null && !filename.isEmpty())
         image.setFilename(filename);
      }
      i++;
      return faq;
   }

   @Override
   public Faq fetch(Object key)
   {
      // aliases to use in the external query
      String pageAlias = "P";
      String faqAlias = "F";
      String faqCategoryAlias = "FC";
      String faqCategoryPageAlias = "FCP";

      // query string buffer
      StringBuffer sb = new StringBuffer(
               "SELECT ");

      sb.append(pageAlias).append(".id, ");
      sb.append(pageAlias).append(".title, ");
      sb.append(faqAlias).append(".answer, ");
      sb.append(faqAlias).append(".date, ");
      sb.append(faqAlias).append(".faqCategory_id, ");
      sb.append(faqCategoryPageAlias).append(".title AS faqCategoryTitle, ");
      sb.append(faqCategoryAlias).append(".orderNum, ");
      sb.append(" I.fileName AS image ");

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(Faq.TABLE_NAME).append(" AS ").append(faqAlias);
      sb.append(" LEFT JOIN ").append(FaqCategory.TABLE_NAME).append(" AS ").append(faqCategoryAlias)
               .append(" ON ( ").append(faqCategoryAlias).append(".id = ").append(faqAlias)
               .append(".faqCategory_id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(faqAlias).append(".id = ")
               .append(pageAlias).append(".id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(faqCategoryPageAlias).append(" on ( ")
               .append(faqCategoryAlias).append(".id = ")
               .append(faqCategoryPageAlias).append(".id ) ");

      sb.append(" where ").append(faqAlias).append(".id = :ID ");
      @SuppressWarnings("unchecked")
      Iterator<Object[]> results = getEm()
               .createNativeQuery(sb.toString()).setParameter("ID", key).getResultList().iterator();
      while (results.hasNext())
      {
         return extract(results.next());
      }
      return null;
   }
}
