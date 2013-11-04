package org.giavacms.twizz.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.twizz.model.Partecipation;

@Named
@Stateless
@LocalBean
public class PartecipationRepository extends AbstractRepository<Partecipation>
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
      return "quizCompetitor.points desc";
   }

   @Override
   protected void applyRestrictions(Search<Partecipation> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
   {

   }

}
