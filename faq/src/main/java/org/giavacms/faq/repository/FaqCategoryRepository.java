package org.giavacms.faq.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.model.FaqCategory;

@Named
@Stateless
@LocalBean
public class FaqCategoryRepository extends AbstractPageRepository<FaqCategory>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "orderNum asc";
   }

   @Override
   protected FaqCategory prePersist(FaqCategory n)
   {
      n.setClone(true);
      return super.prePersist(n);
   }

   @Override
   protected FaqCategory preUpdate(FaqCategory n)
   {
      n.setClone(true);
      return super.preUpdate(n);
   }

   public String translate(String currentLangValue, int currentLang,
            int alternativeLang, String noValueOption)
   {
      String alternativeLangValue = null;
      try
      {
         FaqCategory fq = find(currentLangValue);
         if (fq != null)
         {
            switch (alternativeLang)
            {
            case 1:
               alternativeLangValue = fq.getLang1id();
               break;
            case 2:
               alternativeLangValue = fq.getLang2id();
               break;
            case 3:
               alternativeLangValue = fq.getLang3id();
               break;
            case 4:
               alternativeLangValue = fq.getLang4id();
               break;
            case 5:
               alternativeLangValue = fq.getLang5id();
               break;
            default:
               break;
            }
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
      }
      if (alternativeLangValue == null
               || alternativeLangValue.trim().isEmpty())
      {
         alternativeLangValue = noValueOption;
      }
      return alternativeLangValue;
   }

   /**
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".lang1id, ");
    * sb.append(pageAlias).append(".lang2id, "); sb.append(pageAlias).append(".lang3id, ");
    * sb.append(pageAlias).append(".lang4id, ");
    * sb.append(pageAlias).append(".lang5id, ");sb.append(pageAlias).append(".title, ");
    * sb.append(pageAlias).append(".description, "); sb.append(templateImplAlias).append(".id as templateImpl_id, ");
    * sb.append(templateImplAlias).append(".mainPageId, "); sb.append(templateImplAlias).append(".mainPageTitle, "); if
    * (completeFetch) { // additional fields to retrieve only when fetching
    * sb.append(productAlias).append(".id AS productId, "); sb.append(productAlias).append(".title AS productTitle, ");
    * }
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected List<FaqCategory> extract(List resultList, boolean completeFetch)
   {
      FaqCategory faqCategory = null;
      Map<String, Set<Faq>> faqs = new LinkedHashMap<String, Set<Faq>>();
      Map<String, FaqCategory> categories = new LinkedHashMap<String, FaqCategory>();

      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         faqCategory = new FaqCategory();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         // if (id != null && !id.isEmpty())
         faqCategory.setId(id);
         i++;
         String lang1id = (String) row[i];
         faqCategory.setLang1id(lang1id);
         i++;
         String lang2id = (String) row[i];
         faqCategory.setLang2id(lang2id);
         i++;
         String lang3id = (String) row[i];
         faqCategory.setLang3id(lang3id);
         i++;
         String lang4id = (String) row[i];
         faqCategory.setLang4id(lang4id);
         i++;
         String lang5id = (String) row[i];
         faqCategory.setLang5id(lang5id);
         i++;
         Object clone = row[i];
         if (clone != null)
         {
            if (clone instanceof Boolean)
            {
               faqCategory.setClone(((Boolean) clone).booleanValue());
            }
            if (clone instanceof Short)
            {
               faqCategory.setClone(((Short) clone).intValue() > 0 ? true : false);
            }
            else
            {
               logger.error("clone instance of " + clone.getClass().getCanonicalName());
            }
         }
         else
         {
            logger.error("clone should not be null");
         }
         i++;
         String title = (String) row[i];
         // if (title != null && !title.isEmpty())
         faqCategory.setTitle(title);
         i++;
         String description = (String) row[i];
         // if (description != null && !description.isEmpty())
         faqCategory.setDescription(description);
         i++;
         Object template_impl_id = row[i];
         if (template_impl_id != null)
         {
            if (template_impl_id instanceof BigInteger)
            {
               faqCategory.getTemplate().setId(((BigInteger) template_impl_id).longValue());
               faqCategory.setTemplateId(((BigInteger) template_impl_id).longValue());
            }
            else
            {
               logger.error("templateImpl_id instance of " + template_impl_id.getClass().getCanonicalName());
            }
         }
         else
         {
            logger.error("templateImpl_id should not be null");
         }
         i++;
         String mainPageId = (String) row[i];
         faqCategory.getTemplate().setMainPageId(mainPageId);
         i++;
         String mainPageTitle = (String) row[i];
         faqCategory.getTemplate().setMainPageTitle(mainPageTitle);
         i++;
         String imageFilename = (String) row[i];
         if (imageFilename != null)
         {
            faqCategory.setImage(new Image());
            faqCategory.getImage().setFilename(imageFilename);
         }
         i++;
         if (completeFetch)
         {
            // extract additional fields
            String faqId = (String) row[i];
            i++;
            Timestamp date = (Timestamp) row[i];
            Date faqDate = null;
            if (date != null)
            {
               faqDate = new Date(date.getTime());
            }
            i++;
            String faqAnswer = (String) row[i];
            i++;
            String faqName = (String) row[i];
            i++;
            Set<Faq> category_faqs = null;
            if (faqId != null && !faqId.isEmpty())
            {
               if (faqs.containsKey(id))
               {
                  category_faqs = (HashSet<Faq>) faqs.get(id);
               }
               else
               {
                  category_faqs = new HashSet<Faq>();
                  faqs.put(id, category_faqs);
               }
            }
            if (category_faqs != null)
            {
               Faq f = new Faq();
               f.setId(faqId);
               f.setAnswer(faqAnswer);
               f.setDate(faqDate);
               f.setTitle(faqName);
               category_faqs.add(f);
            }
         }
         if (!categories.containsKey(id))
         {
            categories.put(id, faqCategory);
         }
      }

      if (completeFetch)
      {
         for (String id : faqs.keySet())
         {
            FaqCategory cat = null;
            if (categories.containsKey(id))
            {
               cat = categories.get(id);
               Set<Faq> fs = faqs.get(id);
               if (fs != null)
               {
                  for (Faq f : fs)
                  {
                     f.setFaqCategory(cat);
                     cat.addFaq(f);
                  }
               }
            }
            else
            {
               logger.error("ERROR - DOCS CYCLE cannot find id:" + id);
            }

         }
      }
      return new ArrayList<FaqCategory>(categories.values());
   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   protected StringBuffer getListNative(Search<FaqCategory> search, Map<String, Object> params, boolean count,
            int startRow,
            int pageSize, boolean completeFetch)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String templateImplAlias = "TI";
      String faqCategoryAlias = "C";
      String imageAlias = "I";
      String faqAlias = "D";
      String faqPageAlias = "PD";

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
         sb.append(pageAlias).append(".lang1id, ");
         sb.append(pageAlias).append(".lang2id, ");
         sb.append(pageAlias).append(".lang3id, ");
         sb.append(pageAlias).append(".lang4id, ");
         sb.append(pageAlias).append(".lang5id, ");
         sb.append(pageAlias).append(".clone, ");
         sb.append(pageAlias).append(".title, ");
         sb.append(pageAlias).append(".description, ");
         sb.append(templateImplAlias).append(".id as templateImpl_id, ");
         sb.append(templateImplAlias).append(".mainPageId, ");
         sb.append(templateImplAlias).append(".mainPageTitle, ");
         sb.append(imageAlias).append(".fileName AS image ");
         if (completeFetch)
         {
            // additional fields to retrieve only when fetching
            sb.append(", ").append(faqAlias).append(".id AS faqId, ");
            sb.append(faqAlias).append(".date AS faqDate, ");
            sb.append(faqAlias).append(".answer AS faqAnswer, ");
            sb.append(faqPageAlias).append(".title AS faqTitle ");
         }
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(FaqCategory.TABLE_NAME).append(" AS ").append(faqCategoryAlias);
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(faqCategoryAlias).append(".id = ")
               .append(pageAlias).append(".id ) ");
      sb.append(" LEFT JOIN ").append(TemplateImpl.TABLE_NAME).append(" as ").append(templateImplAlias)
               .append(" on ( ")
               .append(templateImplAlias).append(".id = ")
               .append(pageAlias).append(".template_id ) ");

      if (count)
      {
         // we optimize the count query by avoiding useless left joins
      }
      else
      {
         // we need details-from clause in case of count = false
         if (FaqCategory.HAS_DETAILS)
         {
            sb.append(" LEFT JOIN ").append(Image.TABLE_NAME).append(" as ").append(imageAlias)
                     .append(" on ( ").append(imageAlias)
                     .append(".id = ").append(faqCategoryAlias).append(".image_id ) ");
            sb.append(" LEFT JOIN ").append(Faq.TABLE_NAME).append(" AS ").append(faqAlias)
                     .append(" ON ( ").append(faqAlias).append(".faqCategory_id = ").append(faqCategoryAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" AS ").append(faqPageAlias)
                     .append(" ON ( ").append(faqAlias).append(".id = ").append(faqPageAlias)
                     .append(".id ) ");
         }
      }

      String innerPageAlias = null;
      String innerTemplateImplAlias = null;
      String innerFaqCategoryAlias = null;

      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerTemplateImplAlias = templateImplAlias;
         innerFaqCategoryAlias = faqCategoryAlias;
      }
      else if (FaqCategory.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerTemplateImplAlias = "T1";
         innerFaqCategoryAlias = "C1";

         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(FaqCategory.TABLE_NAME).append(" AS ").append(innerFaqCategoryAlias);
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerFaqCategoryAlias).append(".id = ")
                  .append(innerPageAlias).append(".id ) ");
         sb.append(" LEFT JOIN ").append(TemplateImpl.TABLE_NAME).append(" as ").append(innerTemplateImplAlias)
                  .append(" on ( ")
                  .append(innerTemplateImplAlias).append(".id = ")
                  .append(innerPageAlias).append(".template_id ) ");
      }
      else
      {
         // we also don't need an inner query in case of master-data that has no multiple details
         // so aliases can stay the same
         innerPageAlias = pageAlias;
         innerTemplateImplAlias = templateImplAlias;
         innerFaqCategoryAlias = faqCategoryAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerTemplateImplAlias, innerFaqCategoryAlias,
               separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(innerFaqCategoryAlias).append(".orderNum asc ");

         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

         if (FaqCategory.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(faqCategoryAlias).append(".orderNum desc ");
            sb.append(", ").append(faqAlias).append(".date desc ");
         }

      }
      return sb;
   }

   protected void applyRestrictionsNative(Search<FaqCategory> search, String pageAlias, String templateImplAlias,
            String categoryAlias,
            String separator, StringBuffer sb,
            Map<String, Object> params)
   {

      String customLike = null;
      super.applyRestrictionsNative(search, pageAlias, separator, sb, params, customLike);
   }

}
