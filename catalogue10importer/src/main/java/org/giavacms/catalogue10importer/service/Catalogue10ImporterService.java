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

      Page defaultCategoryBasePage = null;
      try
      {
         defaultCategoryBasePage = (Page) em
                  .createQuery(
                           "select p from " + Page.class.getSimpleName()
                                    + " p where p.extension = :EXTENSION and p.clone = :CLONE ")
                  .setParameter("EXTENSION", Category.EXTENSION).setParameter("CLONE", false).setMaxResults(1)
                  .getSingleResult();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(
                  "Deve esistere almeno una pagina base da usare come default per le categorie importate");
      }

      Page defaultProductBasePage = null;
      try
      {
         defaultCategoryBasePage = (Page) em
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

      List<OldCategory> oldCategories = em.createQuery("select oc from " + OldCategory.class.getSimpleName() + " oc ")
               .getResultList();

      Map<String, Category> categories = doImport(em, oldCategories, defaultCategoryBasePage);

      List<Long> oldProductIds = em.createQuery("select op.id from " + OldProduct.class.getSimpleName() + " op ")
               .getResultList();

      for (Long oldProductId : oldProductIds)
      {
         OldProduct oldProduct = fetch(em, oldProductId);
         doImport(em, oldProduct, categories, defaultProductBasePage);
      }

   }

   private Map<String, Category> doImport(EntityManager em, List<OldCategory> oldCategories, Page defaultBasePage)
   {
      List<Category> categoryTypes = categoryRepository.getAllList();
      Map<String, Category> map = new HashMap<String, Category>();
      for (Category category : categoryTypes)
      {
         if (category.isActive())
         {
            map.put(category.getTitle(), category);
         }
      }

      for (OldCategory oldCategory : oldCategories)
      {
         if (map.get(oldCategory.getName()) == null)
         {
            Category c = new Category();
            c.setActive(true);
            c.setTitle(oldCategory.getName());
            c.setDescription(oldCategory.getDescription());
            c.setOrderNum(oldCategory.getOrderNum());
            c = categoryRepository.persist(c);
            if (c == null)
            {
               throw new RuntimeException("Errore nel salvataggio della categoria: " + oldCategory);
            }
            else
            {
               map.put(c.getTitle(), c);
            }
         }
      }
      return map;
   }

   private void doImport(EntityManager em, OldProduct op, Map<String, Category> categoriesMap, Page defaultBasePage)
   {
      Product p = new Product();
      p.setActive(op.isActive());
      p.setTitle(op.getName());
      p.setPreview(op.getPreview());
      p.setDescription(op.getDescription());
      p.setCategory(categoriesMap.get(op.getCategory().getName()));
      p.setDimensions(op.getDimensions());
      p.setCode(op.getCode());
      p.setClone(true);
      p.setExtension(Product.EXTENSION);
      // non posso settare in fase di persist dei documenti o delle immagini gia esistenti. le associo in fase di update
      p.setDocuments(null);
      p.setImages(null);
      // da aggiornare una volta generato l'id
      p.setLang1id(null);
      p.setTemplate(defaultBasePage.getTemplate());
      p = productRepository.persist(p);
      if (p == null)
      {
         throw new RuntimeException("Errore nel salvataggio della prodotto #" + op.getId() + ": " + op.getName());
      }
      // em.createQuery("update " + RichContent.class.getSimpleName() + " c set c.lang1id = c.id where c.id = :ID ")
      // .setParameter("ID", rc.getId()).executeUpdate();
      if (op.getDocuments() != null)
      {
         p.setDocuments(new ArrayList<Document>());
         for (Document d : op.getDocuments())
         {
            Document nd = new Document();
            nd.setId(d.getId());
            nd.setActive(d.isActive());
            nd.setData(d.getData());
            nd.setDescription(d.getDescription());
            nd.setFilename(d.getFilename());
            nd.setName(d.getName());
            nd.setType(d.getType());
            p.getDocuments().add(nd);
         }
      }
      if (op.getImages() != null)
      {
         p.setImages(new ArrayList<Image>());
         for (Image i : op.getImages())
         {
            Image ni = new Image();
            ni.setId(i.getId());
            ni.setActive(i.isActive());
            ni.setData(i.getData());
            ni.setDescription(i.getDescription());
            ni.setFilename(i.getFilename());
            ni.setName(i.getName());
            ni.setType(i.getType());
            p.getImages().add(ni);
         }
      }
      p.setLang1id(p.getId());
      productRepository.update(p);

   }

   private OldProduct fetch(EntityManager em, Long productId)
   {
      OldProduct op = em.find(OldProduct.class, productId);
      if (op.getCategory() != null)
      {
         op.getCategory().toString();
      }
      if (op.getImages() != null)
      {
         for (Image rni : op.getImages())
         {
            rni.toString();
         }
      }
      if (op.getDocuments() != null)
      {
         for (Document rnd : op.getDocuments())
         {
            rnd.toString();
         }
      }
      return op;
   }
}
