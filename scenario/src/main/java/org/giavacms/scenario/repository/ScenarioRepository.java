package org.giavacms.scenario.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.TemplateImpl;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.model.Search;
import org.giavacms.scenario.model.Scenario;
import org.hibernate.Session;

@Named
@Stateless
@LocalBean
public class ScenarioRepository extends AbstractPageRepository<Scenario>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected String getDefaultOrderBy()
   {
      return "title asc";
   }

   @Override
   public Scenario fetch(Object key)
   {
      try
      {
         Scenario scenario = find(key);
         for (Document document : scenario.getDocuments())
         {
            document.getName();
         }

         for (Image image : scenario.getImages())
         {
            image.getName();
         }
         for (Product product : scenario.getProducts())
         {
            product.getTitle();
            for (Image image : product.getImages())
            {
               // logger.info("prod: " + product.getName() + " - img:"
               // + image.getFilename());
               image.getName();
               image.getFilename();
            }
         }
         scenario.getTemplate().toString();
         scenario.getTemplate().getTemplate().toString();
         return scenario;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @SuppressWarnings("unchecked")
   public List<Scenario> loadRandomList(int pageSize)
   {
      // TODO Auto-generated method stub
      String query = "SELECT e FROM Scenario e ORDER BY RAND()";
      Session session = (Session) getEm().getDelegate();

      List<Scenario> list = session.createQuery(query)
               .setMaxResults(pageSize).list();
      for (Scenario scenario : list)
      {
         if (scenario.getImages() != null)
         {
            for (Image img : scenario.getImages())
            {
               img.getId();
               img.getFilename();
               img.getFilePath();
            }
         }
      }
      return list;
   }

   @Override
   protected Scenario prePersist(Scenario n)
   {
      n.setClone(true);
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
      n.setDescription(HtmlUtils.normalizeHtml(n
               .getDescription()));
      return super.prePersist(n);
   }

   @Override
   protected Scenario preUpdate(Scenario n)
   {
      n.setClone(true);
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
      n.setDescription(HtmlUtils.normalizeHtml(n
               .getDescription()));
      return super.preUpdate(n);
   }

   @Override
   protected void applyRestrictions(Search<Scenario> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // always invoke this when extending abstract page repository
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   @Override
   public Scenario find(Object id)
   {
      return getEm().find(Scenario.class, id);
   }

   @Override
   protected StringBuffer getListNative(Search<Scenario> search, Map<String, Object> params, boolean count,
            int startRow, int pageSize, boolean completeFetch)
   {

      // aliases to use in the external query
      String pageAlias = "P";
      String templateImplAlias = "T";
      String scenarioAlias = "S";
      String imageAlias = "I";
      String productAlias = "R";
      String productPageAlias = "RP";

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
         sb.append(pageAlias).append(".title, ");
         sb.append(pageAlias).append(".description, ");
         sb.append(templateImplAlias).append(".id as templateImpl_id, ");
         sb.append(templateImplAlias).append(".mainPageId, ");
         sb.append(templateImplAlias).append(".mainPageTitle, ");
         sb.append(scenarioAlias).append(".preview, ");
         sb.append(imageAlias).append(".id AS imageId, ");
         sb.append(imageAlias).append(".fileName AS imageFile, ");
         sb.append(productAlias).append(".id AS productId, ");
         sb.append(productPageAlias).append(".title AS productTitle ");
         if (completeFetch)
         {
            // additional fields to retrieve only when fetching
         }
      }

      // master-from clause is the same in both count = true and count = false
      sb.append(" FROM ").append(Scenario.TABLE_NAME).append(" AS ").append(scenarioAlias);
      sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(pageAlias).append(" on ( ")
               .append(scenarioAlias).append(".id = ")
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
         if (Scenario.HAS_DETAILS)
         {
            sb.append(" LEFT JOIN ").append(Scenario.TABLE_NAME).append("_").append(Image.TABLE_NAME)
                     .append(" AS RI ON ( RI.").append(Scenario.TABLE_NAME).append("_id = ")
                     .append(scenarioAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Image.TABLE_NAME).append(" as ").append(imageAlias)
                     .append(" on ( ").append(imageAlias)
                     .append(".id = RI.images_id ) ");
            sb.append(" LEFT JOIN ").append(Scenario.TABLE_NAME).append("_").append(Product.TABLE_NAME)
                     .append(" AS SP ON ( SP.").append(Scenario.TABLE_NAME).append("_id = ")
                     .append(scenarioAlias)
                     .append(".id ) ");
            sb.append(" LEFT JOIN ").append(Product.TABLE_NAME).append(" as ").append(productAlias)
                     .append(" on ( ").append(productAlias)
                     .append(".id = SP.products_id  ) ");
            sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(productPageAlias)
                     .append(" on ( ").append(productAlias)
                     .append(".id = ").append(productPageAlias).append(".id ) ");
         }
      }

      String innerPageAlias = null;
      String innerTemplateImplAlias = null;
      String innerScenarioAlias = null;
      if (count)
      {
         // we don't need an inner query in case of count = true (because we only need distinct id to count,
         // disregarding result pagination) so aliases stay the same
         innerPageAlias = pageAlias;
         innerTemplateImplAlias = templateImplAlias;
         innerScenarioAlias = scenarioAlias;
      }
      else if (Scenario.HAS_DETAILS)
      {
         // we need different aliases for the inner query in case of count = false or multiple detail rows for each
         // master
         innerPageAlias = "P1";
         innerTemplateImplAlias = "T1";
         innerScenarioAlias = "S1";
         // inner query comes as an inner join, because mysql does not support limit in subquerys
         sb.append(" INNER JOIN ");

         // this is the opening bracket for the internal query
         sb.append(" ( ");

         sb.append(" select distinct ").append(innerPageAlias)
                  .append(".id from ");
         sb.append(Scenario.TABLE_NAME).append(" AS ").append(innerScenarioAlias);
         sb.append(" LEFT JOIN ").append(Page.TABLE_NAME).append(" as ").append(innerPageAlias).append(" on ( ")
                  .append(innerScenarioAlias).append(".id = ")
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
         innerScenarioAlias = scenarioAlias;
      }

      // we append filters right after the latest query, so that they apply to the external one in case count = true and
      // to the internal one in case count = false
      String separator = " where ";
      applyRestrictionsNative(search, innerPageAlias, innerTemplateImplAlias, innerScenarioAlias,
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

         if (Scenario.HAS_DETAILS)
         {
            // this is the closing bracket for the internal query
            sb.append(" ) ");
            // this is where external id selection applies
            sb.append(" as IN2 ON ").append(pageAlias).append(".ID = IN2.ID ");
            // we also need to sort external results to keep result order within each results page
            sb.append(" order by ").append(pageAlias).append(".title desc ");
            sb.append(", ").append(imageAlias).append(".id asc ");
         }

      }
      return sb;
   }

   private void applyRestrictionsNative(Search<Scenario> search, String innerPageAlias, String innerTemplateImplAlias,
            String innerScenarioAlias, String separator, StringBuffer sb, Map<String, Object> params)
   {
      String customLike = null;
      super.applyRestrictionsNative(search, innerPageAlias, separator, sb, params, customLike);
   }

   // @Override
   // sb.append(pageAlias).append(".id, ");
   // sb.append(pageAlias).append(".lang1id, ");
   // sb.append(pageAlias).append(".lang2id, ");
   // sb.append(pageAlias).append(".lang3id, ");
   // sb.append(pageAlias).append(".lang4id, ");
   // sb.append(pageAlias).append(".lang5id, ");
   // sb.append(pageAlias).append(".title, ");
   // sb.append(pageAlias).append(".description, ");
   // sb.append(templateImplAlias).append(".id as templateImpl_id, ");
   // sb.append(templateImplAlias).append(".mainPageId, ");
   // sb.append(templateImplAlias).append(".mainPageTitle, ");
   // sb.append(scenarioAlias).append(".preview ");
   @SuppressWarnings({ "rawtypes", "unchecked" })
   protected List<Scenario> extract(List resultList, boolean
            completeFetch)
   {
      Scenario scenario = null;
      Map<String, Set<Image>> images = new LinkedHashMap<String, Set<Image>>();
      Map<String, Set<Product>> products = new LinkedHashMap<String, Set<Product>>();
      Map<String, Scenario> scenarios = new LinkedHashMap<String, Scenario>();
      Iterator<Object[]> results = resultList.iterator();
      while (results.hasNext())
      {
         scenario = new Scenario();
         Object[] row = results.next();
         int i = 0;
         String id = (String) row[i];
         // if (id != null && !id.isEmpty())
         scenario.setId(id);
         i++;
         String lang1id = (String) row[i];
         scenario.setLang1id(lang1id);
         i++;
         String lang2id = (String) row[i];
         scenario.setLang2id(lang2id);
         i++;
         String lang3id = (String) row[i];
         scenario.setLang3id(lang3id);
         i++;
         String lang4id = (String) row[i];
         scenario.setLang4id(lang4id);
         i++;
         String lang5id = (String) row[i];
         scenario.setLang5id(lang5id);
         i++;
         String title = (String) row[i];
         // if (title != null && !title.isEmpty())
         scenario.setTitle(title);
         i++;
         String description = (String) row[i];
         // if (description != null && !description.isEmpty())
         scenario.setDescription(description);
         i++;
         Object template_impl_id = row[i];
         if (template_impl_id != null)
         {
            if (template_impl_id instanceof BigInteger)
            {
               scenario.getTemplate().setId(((BigInteger) template_impl_id).longValue());
               scenario.setTemplateId(((BigInteger) template_impl_id).longValue());
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
         scenario.getTemplate().setMainPageId(mainPageId);
         i++;
         String mainPageTitle = (String) row[i];
         scenario.getTemplate().setMainPageTitle(mainPageTitle);
         i++;
         String preview = (String) row[i];
         // if (preview != null && !preview.isEmpty())
         scenario.setPreview(preview);
         i++;
         Object image_id = row[i];
         Long imageId = null;
         if (image_id != null)
         {
            if (image_id instanceof BigInteger)
            {
               imageId = ((BigInteger) image_id).longValue();
            }
            else
            {
               logger.error("image_id instance of " + image_id.getClass().getCanonicalName());
            }
         }
         i++;
         String imagefileName = (String) row[i];
         i++;
         if (imageId != null)
         {
            Image image = new Image();
            image.setId(imageId);
            image.setFilename(imagefileName);
            Set<Image> scenario_images = images.get(id);
            if (scenario_images == null)
            {
               scenario_images = new HashSet<Image>();
               images.put(id, scenario_images);
            }
            scenario_images.add(image);
         }
         if (!scenarios.containsKey(id))
         {
            scenarios.put(id, scenario);
         }
         String productId = (String) row[i];
         i++;
         String productTitle = (String) row[i];
         i++;
         if (productId != null)
         {
            Product product = new Product();
            product.setId(productId);
            product.setTitle(productTitle);
            Set<Product> scenario_products = products.get(id);
            if (scenario_products == null)
            {
               scenario_products = new HashSet<Product>();
               products.put(id, scenario_products);
            }
            scenario_products.add(product);
         }

      }
      for (String id : images.keySet())
      {
         Scenario scen = null;
         if (scenarios.containsKey(id))
         {
            scen = scenarios.get(id);
            Set<Image> imgs = images.get(id);
            if (imgs != null)
            {
               for (Image img : imgs)
               {
                  scen.addImage(img);
               }
            }
         }
         else
         {
            logger.error("ERROR - IMGS CYCLE cannot find id:" + id);
         }

      }
      for (String id : products.keySet())
      {
         Scenario scen = null;
         if (scenarios.containsKey(id))
         {
            scen = scenarios.get(id);
            Set<Product> prods = products.get(id);
            if (prods != null)
            {
               for (Product prod : prods)
               {
                  scen.addProduct(prod);
               }
            }
         }
         else
         {
            logger.error("ERROR - IMGS CYCLE cannot find id:" + id);
         }

      }
      return new ArrayList<Scenario>(scenarios.values());
   }
}
