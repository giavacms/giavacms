/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerCategory;

@Named
@Stateless
@LocalBean
public class CustomerRepository extends AbstractPageRepository<Customer>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "listOrder asc";
   }

   @Override
   protected Customer prePersist(Customer n)
   {
      n.setClone(true);
      if (n.getCategory() != null
               && n.getCategory().getId() != null)
         n.setCategory(getEm().find(CustomerCategory.class,
                  n.getCategory().getId()));
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
      return n;
   }

   @Override
   protected Customer preUpdate(Customer n)
   {
      n.setClone(true);
      if (n.getCategory() != null
               && n.getCategory().getId() != null)
         n.setCategory(getEm().find(CustomerCategory.class,
                  n.getCategory().getId()));
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
      return n;
   }

   /**
    * In case of a main table with one-to-many collections to fetch at once
    * 
    * we need an external query to read results and an internal query to apply parameters and paginate results
    * 
    * we need just the external query to apply parameters and count the overall distinct results
    */
   protected StringBuffer getListNative(Search<Customer> search, Map<String, Object> params, boolean count,
            int startRow, int pageSize, boolean completeFetch)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String templateImplAlias = "T";
      String customerAlias = "C";
      String customerCategoryAlias = "CC";
      String imageAlias = "I";
      String documentAlias = "D";

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
         sb.append(customerAlias).append(".preview, ");
         sb.append(customerAlias).append(".address, ");
         sb.append(customerAlias).append(".web, ");
         sb.append(customerAlias).append(".contact, ");
         sb.append(customerAlias).append(".social, ");
         sb.append(customerAlias).append(".dimensions,  ");
         sb.append(customerAlias).append(".listOrder, ");
         sb.append(customerAlias).append(".area, ");
         sb.append(customerAlias).append(".category_id, ");
         sb.append(customerCategoryAlias).append(".name AS categoryName, ");
         sb.append(customerCategoryAlias).append(".description AS categoryDescription, ");
         sb.append(customerCategoryAlias).append(".orderNum AS categoryOrderNum, ");
         sb.append(imageAlias).append(".id AS imageId, ");
         sb.append(imageAlias).append(".fileName AS image,");
         sb.append(documentAlias).append(".id AS documentId, ");
         sb.append(documentAlias).append(".fileName AS document ");
         if (completeFetch)
         {
            // additional fields to retrieve only when fetching
         }
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(Customer.TABLE_NAME).append(" AS ").append(customerAlias);
      sb.append(" LEFT JOIN ").append(CustomerCategory.TABLE_NAME).append(" AS ").append(customerCategoryAlias)
               .append(" ON ( ").append(customerCategoryAlias).append(".id = ").append(customerAlias)
               .append(".category_id ) ");
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(customerAlias).append(".id = ")
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
         if (Customer.HAS_DETAILS)
         {
            sb.append(" LEFT JOIN ").append(Customer.TABLE_NAME).append("_").append(Document.TABLE_NAME)
                     .append(" AS RD ON ( RD.").append(Customer.TABLE_NAME).append("_id = ")
                     .append(customerAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Document.TABLE_NAME).append(" AS ").append(documentAlias)
                     .append(" ON ( RD.documents_id = ").append(documentAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Customer.TABLE_NAME).append("_").append(Image.TABLE_NAME)
                     .append(" AS RI ON ( RI.").append(Customer.TABLE_NAME).append("_id = ")
                     .append(customerAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Image.TABLE_NAME).append(" as ").append(imageAlias)
                     .append(" on ( ").append(imageAlias)
                     .append(".id = RI.images_id ) ");
         }
      }

      String innerPageAlias = null;
      String innerTemplateImplAlias = null;
      String innerCustomerAlias = null;
      String innerCustomerCategoryAlias = null;
      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerTemplateImplAlias = templateImplAlias;
         innerCustomerAlias = customerAlias;
         innerCustomerCategoryAlias = customerCategoryAlias;
      }
      else if (Customer.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerTemplateImplAlias = "T1";
         innerCustomerAlias = "C1";
         innerCustomerCategoryAlias = "CC1";
         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(Customer.TABLE_NAME).append(" AS ").append(innerCustomerAlias);
         sb.append(" LEFT JOIN ").append(CustomerCategory.TABLE_NAME).append(" AS ").append(innerCustomerCategoryAlias)
                  .append(" ON ( ").append(innerCustomerCategoryAlias).append(".id = ").append(innerCustomerAlias)
                  .append(".category_id ) ");
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerCustomerAlias).append(".id = ")
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
         innerCustomerAlias = customerAlias;
         innerCustomerCategoryAlias = customerCategoryAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerTemplateImplAlias, innerCustomerAlias,
               innerCustomerCategoryAlias, separator, sb,
               params);

      if (count)
      {
         // if we only need to count distinct results, we are over!
      }
      else
      {
         // we need to sort internal results to apply pagination
         sb.append(" order by ").append(innerCustomerAlias).append(".date desc ");

         // we apply limit-clause only if we want pagination
         if (pageSize > 0)
         {
            sb.append(" limit ").append(startRow).append(", ").append(pageSize).toString();
         }

         if (Customer.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(customerAlias).append(".date desc ");
            sb.append(", ").append(imageAlias).append(".id asc ");
            sb.append(", ").append(documentAlias).append(".id asc ");
         }

      }
      return sb;
   }

   protected void applyRestrictionsNative(Search<Customer> search, String pageAlias, String templateImplAlias,
            String customerAlias,
            String customerCategoryAlias, String separator, StringBuffer sb, Map<String, Object> params)
   {

      // ACTIVE TYPE
      if (true)
      {
         sb.append(separator).append(customerCategoryAlias)
                  .append(".active = :activeCategory");
         params.put("activeCategory", true);
         separator = " and ";
      }

      // CATEGORY NAME
      if (search.getObj().getCategory() != null
               && search.getObj().getCategory().getName() != null
               && search.getObj().getCategory().getName().trim().length() > 0)
      {
         sb.append(separator).append(customerCategoryAlias)
                  .append(".name = :NAMECAT ");
         params.put("NAMECAT", search.getObj().getCategory().getName());
         separator = " and ";
      }

      // CATEGORY ID
      if (search.getObj().getCategory() != null
               && search.getObj().getCategory().getId() != null
               && search.getObj().getCategory().getId() > 0)
      {
         sb.append(separator).append(customerCategoryAlias).append(".id = :IDCAT ");
         params.put("IDCAT", search.getObj().getCategory().getId());
         separator = " and ";
      }

      // NAME OR DESCRIPTION --> ALSO SEARCH IN CUSTOM FIELDS
      String customLike = null;
      if (search.getObj().getTitle() != null
               && !search.getObj().getTitle().isEmpty())
      {
         // sb.append(separator + " ( upper(").append(alias)
         // .append(".name) LIKE :NAMEPROD ");
         // params.put("NAMEPROD", likeParam(search.getObj().getName()
         // .toUpperCase()));
         // sb.append(" or ").append(" upper(").append(alias)
         // .append(".description ) LIKE :DESC").append(") ");
         // params.put("DESC", likeParam(search.getObj().getName()
         // .toUpperCase()));
         customLike = "upper ( " + pageAlias + ".description ) like :LIKETEXTCUSTOM ";
         params.put("LIKETEXTCUSTOM", likeParam(search.getObj().getTitle().trim().toUpperCase()));
      }

      super.applyRestrictionsNative(search, pageAlias, separator, sb, params, customLike);

   }

   /**
    * sb.append(pageAlias).append(".id, "); sb.append(pageAlias).append(".lang1id, ");
    * sb.append(pageAlias).append(".lang2id, "); sb.append(pageAlias).append(".lang3id, ");
    * sb.append(pageAlias).append(".lang4id, "); sb.append(pageAlias).append(".lang5id, ");
    * sb.append(pageAlias).append(".clone, "); sb.append(pageAlias).append(".title, ");
    * sb.append(pageAlias).append(".description, "); sb.append(templateImplAlias).append(".id as templateImpl_id, ");
    * sb.append(templateImplAlias).append(".mainPageId, "); sb.append(templateImplAlias).append(".mainPageTitle, ");
    * sb.append(customerAlias).append(".preview, "); sb.append(customerAlias).append(".address, ");
    * sb.append(customerAlias).append(".web, "); sb.append(customerAlias).append(".contact, ");
    * sb.append(customerAlias).append(".social, "); sb.append(customerAlias).append(".dimensions,  ");
    * sb.append(customerAlias).append(".listOrder, "); sb.append(customerAlias).append(".area, ");
    * sb.append(customerAlias).append(".category_id, "); sb.append(customerCategoryAlias).append(".name AS category, ");
    * sb.append(imageAlias).append(".id AS imageId, "); sb.append(imageAlias).append(".fileName AS image,");
    * sb.append(documentAlias).append(".id AS documentId, "); sb.append(documentAlias).append(".fileName AS document ");
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected List<Customer> extract(List resultList, boolean completeFetch)
   {
      Customer customer = null;
      Map<String, Map<Long, Image>> images = new LinkedHashMap<String, Map<Long, Image>>();
      Map<String, Map<Long, Document>> documents = new LinkedHashMap<String, Map<Long, Document>>();
      Map<String, Customer> customers = new LinkedHashMap<String, Customer>();

      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         customer = new Customer();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         // if (id != null && !id.isEmpty())
         customer.setId(id);
         i++;
         String lang1id = (String) row[i];
         customer.setLang1id(lang1id);
         i++;
         String lang2id = (String) row[i];
         customer.setLang2id(lang2id);
         i++;
         String lang3id = (String) row[i];
         customer.setLang3id(lang3id);
         i++;
         String lang4id = (String) row[i];
         customer.setLang4id(lang4id);
         i++;
         String lang5id = (String) row[i];
         customer.setLang5id(lang5id);
         i++;
         Object clone = row[i];
         if (clone != null)
         {
            if (clone instanceof Boolean)
            {
               customer.setClone(((Boolean) clone).booleanValue());
            }
            else if (clone instanceof Short)
            {
               customer.setClone(((Short) clone).intValue() > 0 ? true : false);
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
         customer.setTitle(title);
         i++;
         String description = (String) row[i];
         // if (description != null && !description.isEmpty())
         customer.setDescription(description);
         i++;
         Object template_impl_id = row[i];
         if (template_impl_id != null)
         {
            if (template_impl_id instanceof BigInteger)
            {
               customer.getTemplate().setId(((BigInteger) template_impl_id).longValue());
               customer.setTemplateId(((BigInteger) template_impl_id).longValue());
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
         customer.getTemplate().setMainPageId(mainPageId);
         i++;
         String mainPageTitle = (String) row[i];
         customer.getTemplate().setMainPageTitle(mainPageTitle);
         i++;
         String preview = (String) row[i];
         customer.setPreview(preview);
         i++;
         String address = (String) row[i];
         customer.setAddress(address);
         i++;
         String web = (String) row[i];
         customer.setWeb(web);
         i++;
         String contact = (String) row[i];
         customer.setContact(contact);
         i++;
         String social = (String) row[i];
         customer.setSocial(social);
         i++;
         String dimensions = (String) row[i];
         customer.setDimensions(dimensions);
         i++;
         Object listOrder = (Object) row[i];
         if (listOrder != null)
         {
            if (listOrder instanceof Integer)
            {
               customer.setListOrder((Integer) listOrder);
            }
            else if (listOrder instanceof Long)
            {
               customer.setListOrder(((Long) listOrder).intValue());
            }
            else if (listOrder instanceof BigDecimal)
            {
               customer.setListOrder(((BigDecimal) listOrder).intValue());
            }
            else
            {
               logger.error("listOrder instance of " + listOrder.getClass().getCanonicalName());
            }
         }
         i++;
         String area = (String) row[i];
         customer.setArea(area);
         i++;
         Object category_id = row[i];
         if (category_id != null)
         {
            if (category_id instanceof BigInteger)
            {
               customer.getCategory().setId(((BigInteger) category_id).longValue());
            }
            else
            {
               logger.error("category_id instance of " + category_id.getClass().getCanonicalName());
            }
         }
         else
         {
            logger.error("category_id should not be null");
         }
         i++;
         String categoryName = (String) row[i];
         if (categoryName != null && !categoryName.isEmpty())
            customer.getCategory().setName(categoryName);
         i++;
         String categoryDescription = (String) row[i];
         if (categoryDescription != null && !categoryDescription.isEmpty())
            customer.getCategory().setDescription(categoryDescription);
         i++;
         Object orderNum = (Object) row[i];
         if (orderNum != null)
         {
            if (orderNum instanceof Integer)
            {
               customer.getCategory().setOrderNum((Integer) orderNum);
            }
            else if (orderNum instanceof Long)
            {
               customer.getCategory().setOrderNum(((Long) orderNum).intValue());
            }
            else if (orderNum instanceof BigDecimal)
            {
               customer.getCategory().setOrderNum(((BigDecimal) orderNum).intValue());
            }
            else
            {
               logger.error("category.orderNum instance of " + orderNum.getClass().getCanonicalName());
            }
         }
         i++;
         Image image = null;
         Object imageId = row[i];
         if (imageId != null)
         {
            if (imageId instanceof BigInteger)
            {
               image = new Image();
               image.setId(((BigInteger) imageId).longValue());
            }
            else
            {
               logger.error("imageId instance of " + imageId.getClass().getCanonicalName());
            }
         }
         i++;
         String imagefileName = (String) row[i];
         if (image != null && imagefileName != null && !imagefileName.isEmpty())
         {
            image.setFilename(imagefileName);
            if (images.containsKey(id))
            {
               Map<Long, Image> map = images.get(id);
               map.put(image.getId()
                        , image);
            }
            else
            {
               Map<Long, Image> map = new LinkedHashMap<Long, Image>();
               map.put(image.getId(), image);
               images.put(id, map);
            }
         }
         i++;
         Document document = null;
         Object documentId = row[i];
         if (documentId != null)
         {
            if (documentId instanceof BigInteger)
            {
               document = new Document();
               document.setId(((BigInteger) documentId).longValue());
            }
            else
            {
               logger.error("documentId instance of " + documentId.getClass().getCanonicalName());
            }
         }
         i++;
         String documentfileName = (String) row[i];
         if (document != null && documentfileName != null && !documentfileName.isEmpty())
         {
            document.setFilename(documentfileName);
            if (documents.containsKey(id))
            {
               Map<Long, Document> map = documents.get(id);
               map.put(document.getId(), document);
            }
            else
            {
               Map<Long, Document> map = new LinkedHashMap<Long, Document>();
               map.put(document.getId(), document);
               documents.put(id, map);
            }
         }
         i++;
         if (completeFetch)
         {
            // extract additional fields
         }
         if (!customers.containsKey(id))
         {
            customers.put(id, customer);
         }

      }
      for (String id : documents.keySet())
      {
         Customer cust = null;
         if (customers.containsKey(id))
         {
            cust = customers.get(id);
            Map<Long, Document> docs = documents.get(id);
            if (docs != null)
            {
               for (Long docId : docs.keySet())
               {
                  cust.addDocument(docs.get(docId));
               }
            }
         }
         else
         {
            logger.error("ERROR - DOCS CYCLE cannot find id:" + id);
         }

      }
      for (String id : images.keySet())
      {
         Customer cust = null;
         if (customers.containsKey(id))
         {
            cust = customers.get(id);
            Map<Long, Image> imgs = images.get(id);
            if (imgs != null)
            {
               for (Long imgId : imgs.keySet())
               {
                  cust.addImage(imgs.get(imgId));
               }
            }
         }
         else
         {
            logger.error("ERROR - IMGS CYCLE cannot find id:" + id);
         }

      }
      return new ArrayList<Customer>(customers.values());
   }

}
