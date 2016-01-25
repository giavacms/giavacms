package org.giavacms.scenario.repository;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.api.model.Search;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.catalogue.model.Product;
import org.giavacms.scenario.model.Scenario;
import org.hibernate.Session;

@Named
@Stateless
public class ScenarioRepository extends BaseRepository<Scenario>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected String getDefaultOrderBy()
   {
      return "title asc";
   }

   @SuppressWarnings("unchecked")
   public List<Scenario> loadRandomList(int pageSize)
   {
      // TODO Auto-generated method stub
      String query = "SELECT e FROM Scenario e WHERE e.active = :ACTIVE ORDER BY RAND()";
      Session session = (Session) getEm().getDelegate();

      List<Scenario> list = session.createQuery(query).setParameter("ACTIVE", true)
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
   protected Scenario prePersist(Scenario n) throws Exception
   {
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }

      return super.prePersist(n);
   }

   @Override
   protected Scenario preUpdate(Scenario n) throws Exception
   {
      if (n.getDocuments() != null && n.getDocuments().size() == 0)
      {
         n.setDocuments(null);
      }
      if (n.getImages() != null && n.getImages().size() == 0)
      {
         n.setImages(null);
      }
      return super.preUpdate(n);
   }

   @Override
   protected void applyRestrictions(Search<Scenario> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      // ACTIVE
      if (true)
      {
         sb.append(separator).append(alias)
                  .append(".category.active = :categoryActive ");
         params.put("categoryActive", true);
         separator = " and ";
      }

      // NAME
      if (search.getLike().getName() != null
               && !search.getLike().getName().trim().isEmpty())
      {
         sb.append(separator).append(" upper ( ").append(alias).append(".name ) like :likeTitle ");
         params.put("likeTitle", likeParam(search.getLike().getName().trim().toUpperCase()));
         separator = " and ";
      }

      // CONTENT (ALSO SEARCHES IN TITLE)
      if (search.getLike().getDescription() != null
               && !search.getLike().getDescription().trim().isEmpty())
      {
         sb.append(separator);
         sb.append(" ( ");
         sb.append(" upper ( ").append(alias).append(".name ) like :likeContent ");
         sb.append(" or ");
         sb.append(" upper ( ").append(alias).append(".description ) like :likeContent ");
         params.put("likeContent", likeParam(search.getLike().getDescription().trim().toUpperCase()));
         separator = " and ";
      }

   }

   @SuppressWarnings("unchecked")
   public List<Image> getImages(String id)
   {
      return getEm()
               .createNativeQuery(
                        "SELECT I.id, I.active, I.description, I.filename, I.name, I.type FROM " + Image.TABLE_NAME
                                 + " I left join " + Scenario.IMAGES_JOINTABLE_NAME
                                 + " PI on (I.id = PI." + Scenario.IMAGE_FK + ") left join "
                                 + Scenario.TABLE_NAME
                                 + " P on (P.id=PI." + Scenario.TABLE_FK + ") "
                                 + " where P.id = :ID and I.active = :ACTIVE",
                        Image.class).setParameter("ID", id).setParameter("ACTIVE", true)
               .getResultList();
   }

   @SuppressWarnings("unchecked")
   public List<Document> getDocuments(String id)
   {
      return getEm()
               .createNativeQuery(
                        "SELECT D.id, D.active, D.description, D.filename, D.name, D.type  FROM " + Document.TABLE_NAME
                                 + " D left join " + Scenario.DOCUMENTS_JOINTABLE_NAME + " PD on (D.id=PD."
                                 + Scenario.DOCUMENT_FK
                                 + ") left join " + Scenario.TABLE_NAME + " P on (P.id=PD." + Scenario.TABLE_FK + ")"
                                 + " where P.id = :ID and D.active = :ACTIVE",
                        Document.class).setParameter("ID", id).setParameter("ACTIVE", true)
               .getResultList();
      // return getEm()
   }

   public void addImage(String scenarioId, Long imageId)
   {
      getEm().createNativeQuery(
               "INSERT INTO " + Scenario.IMAGES_JOINTABLE_NAME + "(" + Scenario.TABLE_FK + ", "
                        + Scenario.IMAGE_FK + ") VALUES (:scenarioId,:imageId) ")
               .setParameter("scenarioId", scenarioId).setParameter("imageId", imageId).executeUpdate();
   }

   public void addDocument(String scenarioId, Long documentId)
   {
      getEm().createNativeQuery(
               "INSERT INTO " + Scenario.DOCUMENTS_JOINTABLE_NAME + "(" + Scenario.TABLE_FK + ", "
                        + Scenario.DOCUMENT_FK + ") VALUES (:scenarioId,:documentId) ")
               .setParameter("scenarioId", scenarioId).setParameter("documentId", documentId).executeUpdate();
   }

   public void removeDocument(String scenarioId, Long documentId)
   {
      getEm().createNativeQuery(
               "DELETE FROM " + Scenario.DOCUMENTS_JOINTABLE_NAME + " where " + Scenario.TABLE_FK
                        + " = :scenarioId and "
                        + Scenario.DOCUMENT_FK + " = :documentId ")
               .setParameter("scenarioId", scenarioId).setParameter("documentId", documentId).executeUpdate();
   }

   public void removeImage(String scenarioId, Long imageId)
   {
      getEm().createNativeQuery(
               "DELETE FROM " + Scenario.IMAGES_JOINTABLE_NAME + " where " + Scenario.TABLE_FK
                        + " = :scenarioId and "
                        + Product.IMAGE_FK + " = :imageId ")
               .setParameter("scenarioId", scenarioId).setParameter("imageId", imageId).executeUpdate();
   }
}
