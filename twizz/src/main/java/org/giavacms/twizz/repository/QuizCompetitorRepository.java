package org.giavacms.twizz.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.twizz.model.QuizCompetitor;

@Named
@Stateless
@LocalBean
public class QuizCompetitorRepository extends AbstractRepository<QuizCompetitor>
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   @Override
   protected EntityManager getEm()
   {
      return em;
   }

   @Override
   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @Override
   protected String getDefaultOrderBy()
   {
      return " date asc ";
   }

   @Override
   protected void applyRestrictions(Search<QuizCompetitor> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

   }

   public QuizCompetitor findByPhone(String phone, boolean onlyNotConfirmed)
   {
      try
      {
         /*
          * his method returns the index of search string in specified string. String positions are 1-based. If string
          * is not found then it returns "0".
          */
         String query = "";
         if (!onlyNotConfirmed)
         {
            query = "select q from " + QuizCompetitor.class.getSimpleName()
                     + " q where (LOCATE(q.phone, :PHONE_NUMBER) > 0) OR (LOCATE(:PHONE_NUMBER, q.phone) > 0)";
         }
         else
         {
            query = "select q from "
                     + QuizCompetitor.class.getSimpleName()
                     + " q where (LOCATE(q.phone, :PHONE_NUMBER) > 0) OR (LOCATE(:PHONE_NUMBER, q.phone) > 0) AND q.confirmationDate is null";
         }
         List<QuizCompetitor> quizCompetitors = (List<QuizCompetitor>) getEm()
                  .createQuery(query)
                  .setParameter("PHONE_NUMBER", phone).getResultList();
         if (quizCompetitors != null && quizCompetitors.size() > 0)
            return quizCompetitors.get(0);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return null;
   }
}
