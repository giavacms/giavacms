package org.giavacms.catalogue10importer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.catalogue10importer.model.OldCategory;
import org.giavacms.catalogue10importer.model.OldProduct;
import org.jboss.ejb3.annotation.TransactionTimeout;

@Named
@Stateless
@LocalBean
public class Catalogue10ImporterService
{

   @Inject
   ProductRepository productRepository;
   @Inject
   CategoryRepository categoryRepository;

   @SuppressWarnings("unchecked")
   @TransactionTimeout(unit = TimeUnit.MINUTES, value = 120L)
   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
   public void doImport()
   {
      EntityManager em = productRepository.getEm();

      Page defaultBasePage = null;
      try
      {
         defaultBasePage = (Page) em
                  .createQuery(
                           "select p from " + Page.class.getSimpleName()
                                    + " p where p.extension = :EXTENSION and p.clone = :CLONE ")
                  .setParameter("EXTENSION", Product.EXTENSION).setParameter("CLONE", false).setMaxResults(1)
                  .getSingleResult();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(
                  "Deve esistere almeno una pagina base da usare come default per i prodotti importati");
      }

      List<String> richNewsTypes = em.createQuery(
               "select distinct(rnt.name) from " + OldCategory.class.getSimpleName() + " rnt ")
               .getResultList();

      Map<String, OldCategory> richContentTypes = doImport(em, richNewsTypes, defaultBasePage);

      List<String> richNewsIds = em.createQuery("select rn.id from " + RichNews.class.getSimpleName() + " rn ")
               .getResultList();

      for (String richNewsId : richNewsIds)
      {
         RichNews richNews = fetch(em, richNewsId);
         doImport(em, richNews, richContentTypes, defaultBasePage);
      }

   }

   private Map<String, OldCategory> doImport(EntityManager em, List<String> categories, Page defaultBasePage)
   {
      List<Category> categoryTypes = categoryRepository.getAllList();
      Map<String, Category> map = new HashMap<String, Category>();
      for (Category category : categoryTypes)
      {
         if (category.isActive())
         {
            map.put(category.getName(), category);
         }
      }

      for (String category : categories)
      {
         if (map.get(category) == null)
         {
            Category rct = new Category();
            rct.setActive(true);
            rct.setName(category);
            rct.setPage(new Page());
            rct.getPage().setId(defaultBasePage.getId());
            rct = categoryRepository.persist(rct);
            if (rct == null)
            {
               throw new RuntimeException("Errore nel salvataggio della categoria: " + category);
            }
            else
            {
               map.put(rct.getName(), rct);
            }
         }
      }
      return map;
   }

   private void doImport(EntityManager em, OldProduct rn, Map<String, Category> categoriesMap, Page defaultBasePage)
   {
      Product rc = new Product();
      rc.setActive(rn.isActive());
      rc.setAuthor(rn.getAuthor());
      rc.setClone(true);
      rc.setContent(rn.getContent());
      rc.setDate(rn.getDate());
      rc.setDescription(rn.getPreview());
      rc.setDescription(rn.getTitle());
      rc.setExtension(RichContent.EXTENSION);
      rc.setHighlight(false);
      // non posso settare in fase di persist dei documenti o delle immagini gia esistenti. le associo in fase di update
      rc.setDocuments(null);
      rc.setImages(null);
      // da aggiornare una volta generato l'id
      rc.setLang1id(null);
      rc.setPreview(rn.getPreview());
      rc.setCategory(categoriesMap.get(rn.getCategory().getName()));
      rc.setTemplate(defaultBasePage.getTemplate());
      rc.setTitle(rn.getTitle());
      rc = productRepository.persist(rc);
      if (rc == null)
      {
         throw new RuntimeException("Errore nel salvataggio della news #" + rn.getId() + ": " + rn.getTitle());
      }
      // em.createQuery("update " + RichContent.class.getSimpleName() + " c set c.lang1id = c.id where c.id = :ID ")
      // .setParameter("ID", rc.getId()).executeUpdate();
      if (rn.getDocuments() != null)
      {
         rc.setDocuments(new ArrayList<Document>());
         for (Document d : rn.getDocuments())
         {
            Document nd = new Document();
            nd.setId(d.getId());
            nd.setActive(d.isActive());
            nd.setData(d.getData());
            nd.setDescription(d.getDescription());
            nd.setFilename(d.getFilename());
            nd.setName(d.getName());
            nd.setType(d.getType());
            rc.getDocuments().add(nd);
         }
      }
      if (rn.getImages() != null)
      {
         rc.setImages(new ArrayList<Image>());
         for (Image i : rn.getImages())
         {
            Image ni = new Image();
            ni.setId(i.getId());
            ni.setActive(i.isActive());
            ni.setData(i.getData());
            ni.setDescription(i.getDescription());
            ni.setFilename(i.getFilename());
            ni.setName(i.getName());
            ni.setType(i.getType());
            rc.getImages().add(ni);
         }
      }
      rc.setLang1id(rc.getId());
      productRepository.update(rc);

   }

   private OldProduct fetch(EntityManager em, String productId)
   {
      OldProduct rn = em.find(OldProduct.class, productId);
      if (rn.getImages() != null)
      {
         for (Image rni : rn.getImages())
         {
            rni.toString();
         }
      }
      if (rn.getDocuments() != null)
      {
         for (Document rnd : rn.getDocuments())
         {
            rnd.toString();
         }
      }
      return rn;
   }
}
