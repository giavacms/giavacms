/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.model.Search;

@Named
@Stateless
@LocalBean
public class CategoryRepository extends AbstractPageRepository<Category>
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
   public boolean delete(Object key)
   {
      try
      {
         Category category = getEm().find(getEntityType(), key);
         if (category != null)
         {
            category.setActive(false);
            getEm().merge(category);
         }
         return true;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return false;
      }
   }

   @Override
   protected void applyRestrictions(Search<Category> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   @Override
   protected Category prePersist(Category n)
   {
      n.setClone(true);
      n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
      n = super.prePersist(n);
      return n;
   }

   @Override
   protected Category preUpdate(Category n)
   {
      n.setClone(true);
      n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
      n = super.preUpdate(n);
      return n;
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
   @Override
   protected List<Category> extract(List resultList, boolean completeFetch)
   {
      Category category = null;
      Map<String, Map<String, Product>> products = new LinkedHashMap<String, Map<String, Product>>();
      Map<String, Category> categories = new LinkedHashMap<String, Category>();

      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         category = new Category();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         // if (id != null && !id.isEmpty())
         category.setId(id);
         i++;
         String lang1id = (String) row[i];
         category.setLang1id(lang1id);
         i++;
         String lang2id = (String) row[i];
         category.setLang2id(lang2id);
         i++;
         String lang3id = (String) row[i];
         category.setLang3id(lang3id);
         i++;
         String lang4id = (String) row[i];
         category.setLang4id(lang4id);
         i++;
         String lang5id = (String) row[i];
         category.setLang5id(lang5id);
         i++;
         Object clone = row[i];
         if (clone != null)
         {
            if (clone instanceof Short)
            {
               category.setClone(((Short) clone).intValue() > 0 ? true : false);
            }
            else if (clone instanceof Boolean)
            {
               category.setClone(((Boolean) clone).booleanValue());
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
         category.setTitle(title);
         i++;
         String description = (String) row[i];
         // if (description != null && !description.isEmpty())
         category.setDescription(description);
         i++;
         Object template_impl_id = row[i];
         if (template_impl_id != null)
         {
            if (template_impl_id instanceof BigInteger)
            {
               category.getTemplate().setId(((BigInteger) template_impl_id).longValue());
               category.setTemplateId(((BigInteger) template_impl_id).longValue());
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
         category.getTemplate().setMainPageId(mainPageId);
         i++;
         String mainPageTitle = (String) row[i];
         category.getTemplate().setMainPageTitle(mainPageTitle);
         i++;
         Object orderNum = row[i];
         if (orderNum != null)
         {
            if (orderNum instanceof BigInteger)
            {
               category.setOrderNum(((BigInteger) orderNum).intValue());
            }
            else if (orderNum instanceof Long)
            {
               category.setOrderNum(((Long) orderNum).intValue());
            }
            else if (orderNum instanceof Integer)
            {
               category.setOrderNum(((Integer) orderNum).intValue());
            }
            else if (orderNum instanceof Short)
            {
               category.setOrderNum(((Short) orderNum).intValue());
            }
            else
            {
               logger.error("orderNum instance of " + orderNum.getClass().getCanonicalName());
            }
         }
         else
         {
            logger.error("orderNum should not be null");
         }
         i++;
         if (completeFetch)
         {
            // extract additional fields
            String productId = (String) row[i];
            i++;
            String productName = (String) row[i];
            i++;
            Map<String, Product> category_products = null;
            if (productId != null && !productId.isEmpty())
            {
               if (products.containsKey(id))
               {
                  category_products = (Map<String, Product>) products.get(id);
               }
               else
               {
                  category_products = new LinkedHashMap<String, Product>();
                  products.put(id, category_products);
               }
            }
            if (category_products != null)
            {
               Product p = new Product();
               p.setId(productId);
               p.setTitle(productName);
               category_products.put(p.getId(), p);
            }
         }
         if (!categories.containsKey(id))
         {
            categories.put(id, category);
         }
      }

      if (completeFetch)
      {
         for (String id : products.keySet())
         {
            Category cat = null;
            if (categories.containsKey(id))
            {
               cat = categories.get(id);
               Map<String, Product> prods = products.get(id);
               if (prods != null)
               {
                  for (Product prod : prods.values())
                  {
                     prod.setCategory(cat);
                     cat.addProduct(prod);
                  }
               }
            }
            else
            {
               logger.error("ERROR - DOCS CYCLE cannot find id:" + id);
            }

         }
      }
      return new ArrayList<Category>(categories.values());
   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   @Override
   protected StringBuffer getListNative(Search<Category> search, Map<String, Object> params, boolean count,
            int startRow,
            int pageSize, boolean completeFetch)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String templateImplAlias = "TI";
      String categoryAlias = "C";
      String productAlias = "D";
      String productPageAlias = "PD";

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
         sb.append(categoryAlias).append(".orderNum ");
         if (completeFetch)
         {
            // additional fields to retrieve only when fetching
            sb.append(", ").append(productAlias).append(".id AS productId ");
            sb.append(", ").append(productPageAlias).append(".title AS productTitle ");
         }
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(Category.TABLE_NAME).append(" AS ").append(categoryAlias);
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(categoryAlias).append(".id = ")
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
         if (Category.HAS_DETAILS)
         {
            sb.append(" LEFT JOIN ").append(Product.TABLE_NAME).append(" AS ").append(productAlias)
                     .append(" ON ( ").append(productAlias).append(".category_id = ").append(categoryAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" AS ").append(productPageAlias)
                     .append(" ON ( ").append(productAlias).append(".id = ").append(productPageAlias)
                     .append(".id ) ");
         }
      }

      String innerPageAlias = null;
      String innerTemplateImplAlias = null;
      String innerCategoryAlias = null;

      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerTemplateImplAlias = templateImplAlias;
         innerCategoryAlias = categoryAlias;
      }
      else if (Category.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerTemplateImplAlias = "T1";
         innerCategoryAlias = "C1";

         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(Category.TABLE_NAME).append(" AS ").append(innerCategoryAlias);
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerCategoryAlias).append(".id = ")
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
         innerCategoryAlias = categoryAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerTemplateImplAlias, innerCategoryAlias,
               separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(innerCategoryAlias).append(".orderNum asc ");

         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

         if (Category.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(categoryAlias).append(".orderNum desc ");
            sb.append(", ").append(productAlias).append(".code asc ");
         }

      }
      return sb;
   }

   protected void applyRestrictionsNative(Search<Category> search, String pageAlias, String templateImplAlias,
            String categoryAlias,
            String separator, StringBuffer sb,
            Map<String, Object> params)
   {

      String customLike = null;
      super.applyRestrictionsNative(search, pageAlias, separator, sb, params, customLike);
   }

}
