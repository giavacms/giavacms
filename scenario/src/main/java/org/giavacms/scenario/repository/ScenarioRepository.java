package org.giavacms.scenario.repository;

import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.scenario.model.Scenario;
import org.hibernate.Session;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Named
@Stateless
@LocalBean
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
}
