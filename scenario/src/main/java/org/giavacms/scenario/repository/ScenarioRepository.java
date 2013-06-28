package org.giavacms.scenario.repository;

import java.util.List;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
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
   protected void applyRestrictions(Search<Scenario> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

      // always invoke this when extending abstract page repository
      super.applyRestrictions(search, alias, separator, sb, params);
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
         return scenario;
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return null;
      }
   }

   @Override
   public List<Scenario> getList(Search<Scenario> ricerca, int startRow,
            int pageSize)
   {
      // TODO Auto-generated method stub
      List<Scenario> list = super.getList(ricerca, startRow, pageSize);
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

}
